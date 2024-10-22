package app.anne.place.infrastructure;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
@Data
public class GeoRecord {
    private String type;
    private String x;
    private String y;

    @DynamoDbAttribute("type")
    public String getType() {
        return type;
    }

    @DynamoDbAttribute("x")
    public String getX() {
        return x;
    }

    @DynamoDbAttribute("y")
    public String getY() {
        return y;
    }
}
