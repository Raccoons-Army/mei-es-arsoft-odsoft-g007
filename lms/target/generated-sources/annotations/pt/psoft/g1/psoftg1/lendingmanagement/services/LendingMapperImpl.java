package pt.psoft.g1.psoftg1.lendingmanagement.services;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-17T19:55:15+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Amazon.com Inc.)"
)
@Component
public class LendingMapperImpl extends LendingMapper {

    @Override
    public void update(SetLendingReturnedRequest request, Lending lending) {
        if ( request == null ) {
            return;
        }
    }
}
