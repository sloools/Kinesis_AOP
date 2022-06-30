package com.example.kinesisaop.kinesis.aop

import com.example.kinesisaop.KinesisProperties
import com.example.kinesisaop.model.Record
import com.example.kinesisaop.model.SampleRequest
import com.example.kinesisaop.produce.KinesisProduceService
import com.fasterxml.jackson.databind.ObjectMapper
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Around
import org.springframework.stereotype.Component
import org.aspectj.lang.annotation.Aspect
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

@Aspect
@Component
class LoggerAop(
    private val kinesisProperties: KinesisProperties,
    private val kinsisProduceService: KinesisProduceService
) {

    private val classList = listOf<KClass<*>> (
        Record::class // Should Add Request Class When Using Annotation
    )

    val mapper = ObjectMapper()

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

    // Get all properties of @RequestBody Object and send log
    @After("@annotation(SetKinesisLogger)")
    fun setKinesisLoggerScanRequestBody(joinPoint: JoinPoint) {
        joinPoint.args.forEach {
            val request = mapper.convertValue(it, it.javaClass) // Get @RequestBody Object
            val requestClass = request.javaClass.kotlin

            if (requestClass in classList) {
                val recordProperties = getAllProperties(request)

                val record = Record(recordName = recordProperties.get("name"))

                kinsisProduceService.send(
                    streamName = kinesisProperties.streamName,
                    shardName = "shard_1",
                    record = record
                )
            }
        }
    }

    private fun getAllProperties(request: Any): Map<String, String> {
        val propMap = mutableMapOf<String, String>()

        try {
            request.javaClass.kotlin.declaredMemberProperties.forEach { prop ->
                val fieldName = prop.name
                val propValue = prop.get(request).toString()
                propMap[fieldName] = propValue
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return propMap
    }
}
