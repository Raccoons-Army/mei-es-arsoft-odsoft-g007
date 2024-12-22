package pt.psoft.g1.psoftg1.authormanagement.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorCountDTO {
    private String name;
    private Long lendingCount;
}
