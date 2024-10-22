package app.anne.item.application.queryservices;

import app.anne.item.controller.dto.Book;
import app.anne.item.infrastructure.BookExternalRepository;
import app.anne.item.infrastructure.BookInternalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BookQueryService {

    private final BookInternalRepository bookInternalRepo;
    private final BookExternalRepository bookExternalRepo;

    public BookQueryService(BookInternalRepository bookInternalRepo, BookExternalRepository bookExternalRepo) {
        this.bookInternalRepo = bookInternalRepo;
        this.bookExternalRepo = bookExternalRepo;
    }

    public List<Book> findBookByIsbn(String isbn) {
        List<Book> books = bookInternalRepo.findBookByIsbn(isbn);
        if (!books.isEmpty()) {
            return books;
        }

        List<Book> booksFromExternal = bookExternalRepo.findBookByIsbn(isbn);

        if (!booksFromExternal.isEmpty()) {
            try {
                bookInternalRepo.updateBooks(booksFromExternal);
            } catch (Exception e) {
                log.error("Failed to save books to internal repository", e);
            }
        }

        return booksFromExternal;
    }
}
