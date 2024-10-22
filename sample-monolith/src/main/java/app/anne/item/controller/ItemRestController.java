package app.anne.item.controller;

import app.anne.item.application.queryservices.BookQueryService;
import app.anne.item.domain.model.aggregates.ItemId;
import app.anne.item.domain.model.aggregates.OwnerId;
import app.anne.item.infrastructure.ItemRecord;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.anne.item.application.commandservices.ItemCommandService;
import app.anne.item.application.queryservices.ItemQueryService;
import app.anne.item.controller.dto.ItemResponse;
import app.anne.item.controller.dto.CreateItemResource;
import app.anne.item.controller.dto.Book;

import java.util.List;

@CrossOrigin
@Slf4j
@RestController
public class ItemRestController {

    private final ItemQueryService itemQueryService;
    private final ItemCommandService  itemCommandService;
    private final BookQueryService bookQueryService;

    public ItemRestController(ItemQueryService itemQueryService,
                              ItemCommandService itemCommandService,
                              BookQueryService bookQueryService) {
        this.itemQueryService = itemQueryService;
        this.itemCommandService = itemCommandService;
        this.bookQueryService = bookQueryService;
    }

    @PostMapping("/items")
    public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody CreateItemResource createItemResource) {
        ItemRecord item = itemCommandService.createItem(createItemResource);
        return new ResponseEntity<>(new ItemResponse(item), HttpStatus.OK);
    }

    @GetMapping("/items")
    public List<ItemResponse> listItems() {
        return itemQueryService.listItems()
            .stream()
            .map(ItemResponse::new)
            .toList();
    }

    @GetMapping("/items/search")
    public ResponseEntity<List<Book>> getItemByIsbn(@RequestParam String isbn) {
        List<Book> books;
        try {
            books = bookQueryService.findBookByIsbn(isbn);
        } catch (Exception e) {
            log.error("GET /items/search", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/owner/{userId}/items/{itemId}")
    public ResponseEntity<ItemResponse> getItem(@PathVariable("userId") String ownerId, @PathVariable("itemId") String itemId) {
        ItemRecord item = itemQueryService.findByItemId(new OwnerId(ownerId), new ItemId(itemId));
        return new ResponseEntity<>(new ItemResponse(item), HttpStatus.OK);
    }

    @GetMapping("/owner/{userId}/items")
    public List<ItemResponse> getAllItems(@PathVariable("userId") String ownerId) {
        return itemQueryService.getAllItems(new OwnerId(ownerId))
            .stream()
            .map(ItemResponse::new)
            .toList();
    }

    @DeleteMapping("/owner/{userId}/items/{itemId}")
    public void deleteItem(@PathVariable("userId") String ownerId, @PathVariable String itemId) {
        itemCommandService.deleteItem(new OwnerId(ownerId), new ItemId(itemId));
    }
}