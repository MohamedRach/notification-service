package org.com.NotificationService.service;

import lombok.extern.slf4j.Slf4j;
import org.com.NotificationService.DTO.DnsRecord;
import org.com.NotificationService.DTO.EmailChanelCreateDto;
import org.com.NotificationService.DTO.EmailChanelCreateRespDto;
import org.com.NotificationService.DTO.EmailChanelStatusRespDto;
import org.com.NotificationService.exception.DomainNotFoundException;
import org.com.NotificationService.exception.EmailChanelCreationException;
import org.com.NotificationService.exception.EmailIdentityAlreadyExistsException;
import org.com.NotificationService.model.EmailChanel;
import org.com.NotificationService.repository.EmailChanelRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.AlreadyExistsException;
import software.amazon.awssdk.services.sesv2.model.CreateConfigurationSetRequest;
import software.amazon.awssdk.services.sesv2.model.CreateEmailIdentityRequest;
import software.amazon.awssdk.services.sesv2.model.CreateEmailIdentityResponse;
import software.amazon.awssdk.services.sesv2.model.GetEmailIdentityRequest;
import software.amazon.awssdk.services.sesv2.model.GetEmailIdentityResponse;
import software.amazon.awssdk.services.sesv2.model.NotFoundException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class EmailChanelService {

    private final EmailChanelRepository emailChanelRepository;
    private final SesV2Client ses;

    public EmailChanelService(EmailChanelRepository emailChanelRepository, SesV2Client ses) {
        this.emailChanelRepository = emailChanelRepository;
        this.ses = ses;
    }

    public EmailChanelCreateRespDto createEmailChanel(EmailChanelCreateDto emailChanel) {
        String domain = emailChanel.getDomain();
        String configSetName = emailChanel.getName() + "_" + UUID.randomUUID();

        createConfigurationSet(configSetName);
        List<String> dkimTokens = createIdentity(domain, configSetName);
        persistEmailChanel(emailChanel);

        return buildCreateResponse(domain, dkimTokens);
    }

    private void createConfigurationSet(String configSetName) {
        try {
            ses.createConfigurationSet(CreateConfigurationSetRequest.builder()
                    .configurationSetName(configSetName)
                    .build());
        } catch (AlreadyExistsException e) {
            log.info("Configuration set {} already exists, reusing it", configSetName);
        } catch (AwsServiceException | SdkException e) {
            throw new EmailChanelCreationException("Failed to create SES configuration set: " + configSetName, e);
        }
    }

    private List<String> createIdentity(String domain, String configSetName) {
        try {
            CreateEmailIdentityResponse resp = ses.createEmailIdentity(
                    CreateEmailIdentityRequest.builder()
                            .emailIdentity(domain)
                            .configurationSetName(configSetName)
                            .build());

            return resp.dkimAttributes().tokens();
        } catch (AlreadyExistsException e) {
            throw new EmailIdentityAlreadyExistsException("An email identity already exists for domain: " + domain);
        } catch (SdkException e) {
            throw new EmailChanelCreationException("Failed to create SES email identity for domain: " + domain, e);
        }
    }

    private void persistEmailChanel(EmailChanelCreateDto emailChanel) {
        try {
            EmailChanel emailChanel1 = EmailChanel.builder()
                    .emailIdentity(emailChanel.getEmailIdentity())
                    .emailAddress(emailChanel.getEmailAddress())
                    .domain(emailChanel.getDomain())
                    .name(emailChanel.getName())
                    .build();
            emailChanelRepository.save(emailChanel1);
        } catch (DataAccessException e) {
            throw new EmailChanelCreationException("Failed to save email channel for domain: " + emailChanel.getDomain(), e);
        }
    }

    public EmailChanelStatusRespDto getEmailChanelStatus(String domain) {
        try {
            GetEmailIdentityResponse resp = ses.getEmailIdentity(
                    GetEmailIdentityRequest.builder()
                            .emailIdentity(domain)
                            .build());

            String verificationStatus = resp.verifiedForSendingStatus() ? "SUCCESS" : "PENDING";
            String dkimStatus = resp.dkimAttributes() != null && resp.dkimAttributes().status() != null
                    ? resp.dkimAttributes().status().toString()
                    : "NOT_STARTED";

            return new EmailChanelStatusRespDto(domain, verificationStatus, dkimStatus);
        } catch (NotFoundException e) {
            throw new DomainNotFoundException("Email identity not found for domain: " + domain);
        }
    }

    private EmailChanelCreateRespDto buildCreateResponse(String domain, List<String> dkimTokens) {
        List<DnsRecord> dnsRecords = dkimTokens.stream()
                .map(token -> new DnsRecord(
                        "CNAME",
                        token + "._domainkey." + domain,
                        token + ".dkim.amazonses.com"))
                .toList();
        return new EmailChanelCreateRespDto(domain, "PENDING_DNS", dnsRecords);
    }

}
