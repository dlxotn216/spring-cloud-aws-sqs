package io.taesu.awssessender.queue

import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener
import org.springframework.stereotype.Component

/**
 * Created by itaesu on 2021/03/04.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Component
class MessageConsumer {
    @SqsListener(value = ["SQS_ENDPOINT"])
    fun consume(json: String) {
        println("message consumed: [${json}]")
    }

    @SqsListener(value = ["{DEAD_LETTER_SQS_ENDPOINT}"])
    fun consumeDeadLetter(json: String) {
        println("message consumeDeadLetter: [${json}]")
    }
}