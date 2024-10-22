package app.anne.util;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import java.util.List;

public class SqsService {
    public static void sendMessage(String messageBody) {
        SqsClient sqsClient = SqsClient.builder()
            .httpClient(UrlConnectionHttpClient.create())
            .region(Region.US_WEST_2)
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .build();

        try {
            GetQueueUrlResponse getQueueUrlResponse = sqsClient
                .getQueueUrl(GetQueueUrlRequest.builder().queueName("anne-sqs").build());

            String queueUrl = getQueueUrlResponse.queueUrl();
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .delaySeconds(5)
                .build();

            sqsClient.sendMessage(sendMsgRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
