package app.anne.util.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface EnsureDynamoDbTable {
    String tableName();
    String partitionKeyName();
    String partitionKeyType();
}
