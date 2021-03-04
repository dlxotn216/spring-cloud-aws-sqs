package io.taesu.awssessender.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ses.SesAsyncClient


/**
 * Created by itaesu on 2021/03/04.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Configuration
class AwsConfig {

    @Bean
    fun sesAsyncClient(): SesAsyncClient {
        return SesAsyncClient.builder()
                .region(Region.of("us-west-2"))
                .credentialsProvider(ProfileCredentialsProvider.builder()
                        .profileName("dev")
                        .build())
                .build()
    }

    @Bean
    fun amazonSQS(): AmazonSQSAsync {
        return AmazonSQSAsyncClientBuilder
                .standard()
                .withEndpointConfiguration(
                        AwsClientBuilder.EndpointConfiguration(
                                "{SQS_ENDPOINT}",
                                "{SQS_REGION}"
                        )
                )
                .withCredentials(AWSStaticCredentialsProvider(
                        BasicAWSCredentials("{ACCESS_KEY}", "{SECRET_KEY}")
                ))
                .build()
    }

    @Bean
    fun simpleMessageListenerContainerFactory(amazonSQSAsync: AmazonSQSAsync) = SimpleMessageListenerContainerFactory().apply {
        setAmazonSqs(amazonSQSAsync)
        setMaxNumberOfMessages(10)
        setWaitTimeOut(20) // Long polling 설정
    }
}