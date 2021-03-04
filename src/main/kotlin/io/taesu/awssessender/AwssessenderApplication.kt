package io.taesu.awssessender

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.model.SendMessageResult
import io.taesu.awssessender.email.EmailSender
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component


@SpringBootApplication
class AwssessenderApplication

fun main(args: Array<String>) {
    runApplication<AwssessenderApplication>(*args)
}

@Component
class Runner(private val emailSender: EmailSender,
             private val amazonSQS: AmazonSQSAsync) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        emailSender.send()
        sendMessage("""
            {
                "publisher": "taesu",
                "message": "Hello"
            }
        """.trimIndent())
    }


    fun sendMessage(message: String?): SendMessageResult? {
        return amazonSQS.sendMessage("{SQS_ENDPOINT}", message)
    }
}