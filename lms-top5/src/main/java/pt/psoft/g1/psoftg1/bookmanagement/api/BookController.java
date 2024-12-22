package pt.psoft.g1.psoftg1.bookmanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pt.psoft.g1.psoftg1.bookmanagement.services.TopBookService;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;

@Tag(name = "Books", description = "Endpoints for getting the top Books")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final TopBookService topBookService;

    private final BookViewMapper bookViewMapper;

    @Operation(summary = "Gets the top 5 books lent")
    @GetMapping("top5")
    public ListResponse<BookView> getTop5BooksLent() {
        final var list = topBookService.findTopX();
        if (list.isEmpty())
            throw new NotFoundException("No books to show");

        return new ListResponse<>(bookViewMapper.toBookView(list));
    }

}

