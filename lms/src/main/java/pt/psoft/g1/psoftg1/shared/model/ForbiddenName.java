package pt.psoft.g1.psoftg1.shared.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ForbiddenName{

    private Long pk;

    private String forbiddenName;

    public ForbiddenName(String forbiddenName) {
        this.forbiddenName = forbiddenName;
    }
}
