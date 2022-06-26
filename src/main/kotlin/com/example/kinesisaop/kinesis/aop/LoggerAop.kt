package com.example.kinesisaop.kinesis.aop

import com.example.kinesisaop.model.Record
import com.example.kinesisaop.produce.KinesisProduceService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Around
import org.springframework.stereotype.Component
import org.aspectj.lang.annotation.Aspect

@Aspect
@Component
class LoggerAop(
    private val kinsisProduceService: KinesisProduceService
) {

    @Around("@annotation(SetKinesisLogger)")
    fun setKinesisLogger(joinPoint: JoinPoint) {
        println("Before AOP")
        val record = Record("Sample", "20220626")

        kinsisProduceService.send(
            streamName = "SampleName",
            shardName = "shard_1",
            record = record
        )
        println("After AOP")
    }
}
