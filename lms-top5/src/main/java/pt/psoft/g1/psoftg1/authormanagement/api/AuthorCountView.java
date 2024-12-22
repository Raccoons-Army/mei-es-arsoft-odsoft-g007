package pt.psoft.g1.psoftg1.authormanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "An author and its total lendings")
@AllArgsConstructor
@NoArgsConstructor
public class AuthorCountView {
    private String authorName;
    private Long lendingCount;
}
