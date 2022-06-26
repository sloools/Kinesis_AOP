package com.example.kinesisaop.controller
import com.example.kinesisaop.kinesis.aop.SetKinesisLogger
import com.example.kinesisaop.model.SampleRequest
import org.springframework.web.bind.annotation.*

@RestController
class SampleController {

    @GetMapping("/sample")
    @SetKinesisLogger // Add @SetKinesisLogger Annotation To Record Log
    fun getSample(
        @RequestBody sampleRequest: SampleRequest
    ): String {
        return "Sample Log Test"
    }
}