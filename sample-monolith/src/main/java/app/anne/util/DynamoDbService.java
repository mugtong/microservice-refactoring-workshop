//package app.anne.util;
//
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.document.DynamoDB;
//import com.amazonaws.services.dynamodbv2.document.ItemCollection;
//import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
//import com.amazonaws.services.dynamodbv2.document.Table;
//import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
//import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
//import com.amazonaws.services.dynamodbv2.model.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Service
//public class DynamoDbService {
//
//    @Autowired
//    private AmazonDynamoDB amazonDynamoDB;
//
//    private DynamoDB dynamoDB() {
//        return new DynamoDB(amazonDynamoDB);
//    }
//
//    public boolean tableExists(String tableName) {
//        List<String> tableNames = amazonDynamoDB.listTables().getTableNames();
//        return tableNames.contains(tableName);
//    }
//
//    public void createTable(String tableName, String partitionKeyName, ScalarAttributeType partitionKeyType) {
//        // Define the key schema
//        List<KeySchemaElement> keySchema = Arrays.asList(
//                new KeySchemaElement(partitionKeyName, KeyType.HASH)
//        );
//
//        // Define the attribute definition
//        List<AttributeDefinition> attributeDefinitions = Arrays.asList(
//                new AttributeDefinition(partitionKeyName, partitionKeyType)
//        );
//
//        // Create the table
//        CreateTableRequest createTableRequest = new CreateTableRequest()
//                .withTableName(tableName)
//                .withKeySchema(keySchema)
//                .withAttributeDefinitions(attributeDefinitions)
//                .withBillingMode(BillingMode.PAY_PER_REQUEST);
//
//        amazonDynamoDB.createTable(createTableRequest);
//    }
//
//    public void waitForTableCreation(String tableName) {
//        Table table = dynamoDB().getTable(tableName);
//        try {
//            table.waitForActive();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void queryItems(String tableName, String partitionKeyName, String partitionKeyValue) {
//        if (!tableExists(tableName)) {
//            createTable(tableName, partitionKeyName, ScalarAttributeType.S);
//            waitForTableCreation(tableName);
//        }
//
//        System.out.println("Partition key value: " + partitionKeyValue);
//
//        Table table = dynamoDB().getTable(tableName);
//
//        QuerySpec querySpec = new QuerySpec()
//                .withKeyConditionExpression(partitionKeyName + " = :val")
//                .withValueMap(new ValueMap().withString(":val", partitionKeyValue));
//
//        ItemCollection<QueryOutcome> items = table.query(querySpec);
//
//        items.forEach(System.out::println);
//    }
//}
