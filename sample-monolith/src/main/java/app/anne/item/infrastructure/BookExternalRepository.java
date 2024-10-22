package app.anne.item.infrastructure;

import app.anne.item.controller.dto.AladinResponse;
import app.anne.item.controller.dto.Book;
import app.anne.item.domain.exception.BookExternalQueryException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Repository
public class BookExternalRepository {
    private final RestTemplate restTemplate;
    private final String queryUrl;

    public BookExternalRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        String apiKey = System.getenv("ALADIN_API_KEY");
        this.queryUrl = String.format("http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=%s&MaxResults=10&start=1&SearchTarget=Book&output=js&Version=20131101", apiKey);
    }

    public List<Book> findBookByIsbn(String isbn) {
        String url = queryUrl + String.format("&Query=%s", isbn);
        ResponseEntity<AladinResponse> response = restTemplate.getForEntity(url, AladinResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BookExternalQueryException();
        }

        if (response.getBody() == null || response.getBody().getItem() == null) {
            return Collections.emptyList();
        }

        List<Book> books = response.getBody().getItem();
        for (Book book : books) {
            String[] tokens = book.getTitle().split(" - ");
            book.setTitle(tokens[0]);
            if (tokens.length > 1) {
                book.setSubTitle(tokens[1]);
            }
            book.setImageUrl(book.getCover().replace("coversum", "cover500"));
        }

        return books;
    }
}
