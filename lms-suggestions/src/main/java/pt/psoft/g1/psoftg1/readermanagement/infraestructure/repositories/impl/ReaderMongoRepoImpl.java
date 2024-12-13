package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.MongoReaderDetailsModel;
import pt.psoft.g1.psoftg1.readermanagement.mapper.ReaderDetailsMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ReaderMongoRepoImpl implements ReaderRepository {

    private final MongoTemplate mt;
    private final ReaderDetailsMapper readerDetailsMapper;

    @Override
    public ReaderDetails save(ReaderDetails readerDetails) {
        // Map ReaderDetails to its MongoDB model
        MongoReaderDetailsModel mongoReaderDetails = readerDetailsMapper.toMongoReaderDetailsModel(readerDetails);

        MongoReaderDetailsModel savedReaderDetails = mt.save(mongoReaderDetails);
        return readerDetailsMapper.fromMongoReaderDetailsModel(savedReaderDetails);
    }


    @Override
    public void delete(ReaderDetails readerDetails) {
        MongoReaderDetailsModel mongoReaderDetails = readerDetailsMapper.toMongoReaderDetailsModel(readerDetails);

        mt.remove(mongoReaderDetails);
    }

    @Override
    public List<ReaderDetails> findAll() {
        List<MongoReaderDetailsModel> mongoReaderDetailsList = mt.findAll(MongoReaderDetailsModel.class);

        return readerDetailsMapper.fromMongoReaderDetailsModel(mongoReaderDetailsList);
    }

    @Override
    public Optional<ReaderDetails> findById(String id) {
        MongoReaderDetailsModel mongoReaderDetails = mt.findById(id, MongoReaderDetailsModel.class);

        return Optional.ofNullable(mongoReaderDetails)
                .map(readerDetailsMapper::fromMongoReaderDetailsModel);
    }

    @Override
    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("readerNumber").is(readerNumber));
        MongoReaderDetailsModel mongoReaderDetails = mt.findOne(query, MongoReaderDetailsModel.class);

        return Optional.ofNullable(mongoReaderDetails)
                .map(readerDetailsMapper::fromMongoReaderDetailsModel);
    }
}
