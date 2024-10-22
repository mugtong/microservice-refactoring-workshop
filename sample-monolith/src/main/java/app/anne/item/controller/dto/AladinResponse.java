package app.anne.item.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AladinResponse {
    private String version;
    private String logo;
    private String title;
    private String subTitle;
    private String link;
    private String pubDate;
    private Integer totalResults;
    private Integer startIndex;
    private Integer itemsPerPage;
    private String query;
    private Integer searchCategoryId;
    private String searchCategoryName;
    private List<Book> item;
}
