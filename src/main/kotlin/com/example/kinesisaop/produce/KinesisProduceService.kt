package com.example.kinesisaop.produce

import com.example.kinesisaop.KinesisConfig
import com.example.kinesisaop.model.Record
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse

@Service
class KinesisProduceService(
    private val kinesisConfig: KinesisConfig
) {
    val mapper = ObjectMapper()

    fun send(streamName: String, shardName: String, record: Record): PutRecordResponse? {
        val byte = mapper.writeValueAsString(record).toByteArray()

        return kinesisConfig.getClient()?.putRecord(
            PutRecordRequest.builder()
                .streamName(streamName)
                .partitionKey(shardName)
                .data(SdkBytes.fromByteArray(byte))
                .build()
        )
    }
}