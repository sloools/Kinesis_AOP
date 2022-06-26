package com.example.kinesisaop

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "aws.kinesis")
data class KinesisProperties(
    val roleArn: String,
    val streamName: String
)