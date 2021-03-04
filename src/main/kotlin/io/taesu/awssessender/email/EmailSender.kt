package io.taesu.awssessender.email

import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.javamail.MimeMessagePreparator
import org.springframework.stereotype.Component
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.ses.SesAsyncClient
import software.amazon.awssdk.services.ses.model.RawMessage
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest
import java.io.ByteArrayOutputStream
import java.util.*
import javax.mail.Session
import javax.mail.internet.MimeMessage

/**
 * Created by itaesu on 2021/03/04.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Component
class EmailSender(val sesAsyncClient: SesAsyncClient) {

    fun send() {
        try {
            val session: Session = Session.getDefaultInstance(Properties())
            val message = MimeMessage(session)
            val preparator = MimeMessagePreparator {
                MimeMessageHelper(it, true, "UTF-8").apply {
                    setSubject("subject")
                    setText("hello", true)
                    setFrom("no-reply@crscube.io")
                    setTo("taesu@crscube.co.kr")
                }
            }
            preparator.prepare(message)

            val outputStream = ByteArrayOutputStream().use {
                message.writeTo(it)
                it
            }

            val build: SendRawEmailRequest = SendRawEmailRequest.builder()
                    .destinations(arrayListOf("unknown@abcmydomainunknown.co.kr"))
                    .rawMessage(RawMessage.builder().data(SdkBytes.fromByteArray(outputStream.toByteArray())).build())
                    .source("no-reply@crscube.io").build()
            val sendEmailResult = sesAsyncClient.sendRawEmail(build).get()
            println("${sendEmailResult.messageId()} finished")
        } catch (e: Exception) {
            println(e.message)
        }
    }

}