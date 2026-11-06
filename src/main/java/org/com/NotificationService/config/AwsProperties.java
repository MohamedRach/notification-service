package org.com.NotificationService.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.net.URI;

@Validated
@ConfigurationProperties(prefix = "notification.aws")
public record AwsProperties(
        @NotBlank String region,

        /** Optional. Set to http://localhost:4566 to use LocalStack. */
        URI endpoint,

        /** Optional. When absent, the default AWS credentials chain is used. */
        Credentials credentials,

        @NotNull @Valid Topics topics,

        @NotNull @Valid Scheduler scheduler
) {

    public record Credentials(String accessKey, String secretKey) {
    }

    public record Topics(
            @NotBlank String emailArn,
            @NotBlank String smsArn
    ) {
    }

    public record Scheduler(
            /** IAM role that EventBridge Scheduler assumes to publish to the SNS topics. */
            @NotBlank String roleArn,
            @DefaultValue("default") String groupName
    ) {
    }
}

