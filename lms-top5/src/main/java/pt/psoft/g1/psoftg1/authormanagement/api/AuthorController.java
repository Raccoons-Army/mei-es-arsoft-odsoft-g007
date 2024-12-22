package pt.psoft.g1.psoftg1.authormanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorCountDTO;
import pt.psoft.g1.psoftg1.authormanagement.services.TopAuthorService;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;


@Tag(name = "Author", description = "Endpoints for managing Authors")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {
    private final TopAuthorService topAuthorService;
    private final AuthorViewMapper authorViewMapper;

    @Operation(summary = "Know the Top 5 authors which have the most lent books")
    @GetMapping("/top5")
    public ListResponse<AuthorCountDTO> getTop5() {
        final var list = topAuthorService.findTopX();

        if (list.isEmpty())
            throw new NotFoundException("No authors to show");

        return new ListResponse<>(authorViewMapper.toAuthorView(list));
    }

}
