//package app.anne.util;
//
//import app.anne.util.annotations.EnsureDynamoDbTable;
//import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//@Aspect
//@Slf4j
//public class EnsureDynamoDbTableAspect {
//    @Autowired
//    private DynamoDbService dynamoDbService;
//
//    @Before("@annotation(ensureDynamoDbTable)")
//    public void ensureTable(JoinPoint joinPoint, EnsureDynamoDbTable ensureDynamoDbTable) {
//        String tableName = ensureDynamoDbTable.tableName();
//        String partitionKeyName = ensureDynamoDbTable.partitionKeyName();
//        String partitionKeyType = ensureDynamoDbTable.partitionKeyType();
//
//        if (!dynamoDbService.tableExists(tableName)) {
//            ScalarAttributeType scalarAttributeType = ScalarAttributeType.valueOf(partitionKeyType);
//            dynamoDbService.createTable(tableName, partitionKeyName, scalarAttributeType);
//            dynamoDbService.waitForTableCreation(tableName);
//        }
//    }
//}
