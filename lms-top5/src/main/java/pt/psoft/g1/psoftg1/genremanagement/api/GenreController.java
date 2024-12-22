package pt.psoft.g1.psoftg1.genremanagement.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.TopGenreService;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;

@Tag(name = "Genres", description = "Endpoints for getting the top Genres")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
public class GenreController {
    private final TopGenreService topGenreService;
    private final GenreViewMapper genreViewMapper;

    @GetMapping("/top5")
    public ListResponse<GenreCountDTO> getTop() {
        final var list = topGenreService.findTopX();

        if (list.isEmpty())
            throw new NotFoundException("No genres to show");

        return new ListResponse<>(genreViewMapper.toGenreCountDTO(list));
    }
}
