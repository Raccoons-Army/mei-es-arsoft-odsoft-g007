package pt.psoft.g1.psoftg1.bookmanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pt.psoft.g1.psoftg1.bookmanagement.services.Top5BookService;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;

@Tag(name = "Books", description = "Endpoints for getting the top5 Books")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final Top5BookService top5BookService;

    private final BookViewMapper bookViewMapper;

    @Operation(summary = "Gets the top 5 books lent")
    @GetMapping("top5")
    public ListResponse<BookView> getTop5BooksLent() {
        return new ListResponse<>(bookViewMapper.toBookView(top5BookService.findTop5BooksLent()));
    }

}

