package org.com.NotificationService.client;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
@EnableConfigurationProperties(AwsProperties.class)
public class AwsClientsConfig {

    /**
     * Static keys only when both are configured (LocalStack, quick local runs).
     * Otherwise: environment variables, ~/.aws profile, or an IAM role.
     */
    @Bean
    AwsCredentialsProvider awsCredentialsProvider(AwsProperties props) {
        AwsProperties.Credentials c = props.credentials();
        if (c != null && StringUtils.hasText(c.accessKey()) && StringUtils.hasText(c.secretKey())) {
            return StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(c.accessKey(), c.secretKey()));
        }
        return DefaultCredentialsProvider.create();
    }

    @Bean
    SchedulerClient schedulerClient(AwsProperties props, AwsCredentialsProvider credentials) {
        return build(SchedulerClient.builder(), props, credentials);
    }

    @Bean
    SnsClient snsClient(AwsProperties props, AwsCredentialsProvider credentials) {
        return build(SnsClient.builder(), props, credentials);
    }

    /** Region, credentials and optional endpoint override are the same for every client. */
    private static <B extends AwsClientBuilder<B, C>, C> C build(
            B builder, AwsProperties props, AwsCredentialsProvider credentials) {

        builder.region(Region.of(props.region()))
                .credentialsProvider(credentials);

        if (props.endpoint() != null) {
            builder.endpointOverride(props.endpoint());
        }
        return builder.build();
    }
}
