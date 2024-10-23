//package app.anne.util;
//
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
//import com.amazonaws.services.dynamodbv2.model.Projection;
//import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
//import com.amazonaws.services.dynamodbv2.util.TableUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class AutoCreateDynamoDBTable {
//
//    @Autowired
//    static
//    DynamoDBMapper dynamoDBMapper;
//
//    @Autowired
//    static
//    AmazonDynamoDB amazonDynamoDB;
//
//    public static void createTable(Class request) {
//        CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(request)
//                .withProvisionedThroughput(new ProvisionedThroughput(1L,1L));
//
//        createTableRequest.getGlobalSecondaryIndexes().forEach(
//                idx -> idx.withProvisionedThroughput(new ProvisionedThroughput(1L, 1L))
//                        .withProjection(new Projection().withProjectionType("ALL"))
//        );
//        TableUtils.createTableIfNotExists(amazonDynamoDB, createTableRequest);
//    }
//}
