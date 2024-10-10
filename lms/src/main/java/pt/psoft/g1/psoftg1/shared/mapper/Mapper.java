package pt.psoft.g1.psoftg1.shared.mapper;

// J is the JPA entity, M is the MongoDB entity and D is the domain model
public interface Mapper <J, M, D> {
    // Converts a JPA entity to a domain model
    D fromJpaToDomain(J jpaEntity);

    // Converts a MongoDB entity to a domain model
    D fromMongoToDomain(M mongoEntity);

    // Converts a domain model to a JPA entity
    J fromDomainToJpa(D domainEntity);

    // Converts a domain model to a MongoDB entity
    M fromDomainToMongo(D domainEntity);
}
