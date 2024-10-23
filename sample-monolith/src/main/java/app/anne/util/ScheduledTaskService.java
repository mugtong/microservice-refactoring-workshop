package app.anne.util;

import app.anne.item.application.commandservices.ItemCommandService;
import app.anne.item.domain.model.aggregates.OwnedItemStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

@Service
public class ScheduledTaskService {
    private final ItemCommandService itemCommandService;
    SqsClient sqsClient = SqsClient.builder()
        .httpClient(UrlConnectionHttpClient.create())
        .region(Region.US_WEST_2)
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .build();

    public ScheduledTaskService(ItemCommandService itemCommandService) {
        this.itemCommandService = itemCommandService;
    }

    @Scheduled(fixedRate = 10000)
    public void performScheduledTask() {
        System.out.println("Scheduled task executed at " + System.currentTimeMillis());
        GetQueueUrlResponse getQueueUrlResponse = sqsClient
            .getQueueUrl(GetQueueUrlRequest.builder().queueName("anne-sqs").build());

        String queueUrl = getQueueUrlResponse.queueUrl();

        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
            .queueUrl(queueUrl)
            .maxNumberOfMessages(10)
            .waitTimeSeconds(20)
            .build();

        System.out.println("Scheduled task executed at " + queueUrl);
        ReceiveMessageResponse response = sqsClient.receiveMessage(receiveRequest);

        for (Message message : response.messages()) {
            try {
                // Parse the message
                System.out.println("Scheduled task executed at " + message.body());
                String[] parts = message.body().split(",");  // or split(":") or split("\\|")
                if (parts.length >= 2) {
                    String itemId = parts[0];
                    String requesterId = parts[1];
                    // Optional: String timestamp = parts[2];  // if using pipe format

                    // Process the message
                    System.out.println(itemId + " " + requesterId);

                    itemCommandService.updateItemStatus(itemId, OwnedItemStatus.AWAY);
                }

                // Delete after successful processing
                DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(message.receiptHandle())
                    .build();

                DeleteMessageResponse deleteResponse = sqsClient.deleteMessage(deleteRequest);

                System.out.println("Scheduled task executed at " + deleteResponse.responseMetadata().requestId());
            } catch (Exception e) {
//						log.error("Error processing message: {}", message.messageId(), e);
            }
        }
    }
}