package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

@RequiredArgsConstructor
public class UserMongoRepoImpl {
    public final MongoTemplate mongoTemplate;
}
