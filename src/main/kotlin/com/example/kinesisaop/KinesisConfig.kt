package com.example.kinesisaop

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.kinesis.KinesisClient
import software.amazon.awssdk.services.sts.StsClient
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest

@Configuration
class KinesisConfig(
    private val kinesisProperties: KinesisProperties
) {

    fun getStsClient(): StaticCredentialsProvider? {
        val roleArn = kinesisProperties.roleArn

        val stsClient = StsClient.builder().build()

        val roleRequest = AssumeRoleRequest.builder()
            .roleArn(roleArn)
            .roleSessionName("coinfra-krdev")
            .build()

        val credentials = stsClient.assumeRole(roleRequest).credentials()

        val sessionCredentials = AwsSessionCredentials.create(
            credentials.accessKeyId(),
            credentials.secretAccessKey(),
            credentials.sessionToken()
        )

        return StaticCredentialsProvider.create(sessionCredentials)
    }

    @Bean
    fun getClient(): KinesisClient? {
        return KinesisClient.builder()
            .credentialsProvider(getStsClient())
            .region(Region.AP_NORTHEAST_2)
            .build()
    }
}