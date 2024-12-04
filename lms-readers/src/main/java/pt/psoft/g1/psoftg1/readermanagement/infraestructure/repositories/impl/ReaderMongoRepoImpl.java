package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.MongoReaderDetailsModel;
import pt.psoft.g1.psoftg1.readermanagement.mapper.ReaderDetailsMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;
import pt.psoft.g1.psoftg1.shared.dbSchema.MongoPhotoModel;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.MongoUserModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ReaderMongoRepoImpl implements ReaderRepository {

    private final MongoTemplate mt;
    private final ReaderDetailsMapper readerDetailsMapper;

    @Override
    public List<ReaderDetails> findByPhoneNumber(String phoneNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("phoneNumber").is(phoneNumber));
        List<MongoReaderDetailsModel> list = mt.find(query, MongoReaderDetailsModel.class);

        return readerDetailsMapper.fromMongoReaderDetailsModel(list);
    }

    @Override
    public Optional<ReaderDetails> findByUsername(String username) {
        // Step 1: Find the MongoUserModel by username
        Query userQuery = new Query();
        userQuery.addCriteria(Criteria.where("username").is(username));
        MongoUserModel mongoUser = mt.findOne(userQuery, MongoUserModel.class);

        if (mongoUser == null) {
            return Optional.empty();
        }

        // Step 2: Use the found MongoUserModel's ID to query MongoReaderDetailsModel
        Query readerDetailsQuery = new Query();
        readerDetailsQuery.addCriteria(Criteria.where("reader.$id").is(new ObjectId(mongoUser.getPk())));

        MongoReaderDetailsModel mongoReaderDetails = mt.findOne(readerDetailsQuery, MongoReaderDetailsModel.class);

        // Map the MongoReaderDetailsModel to ReaderDetails if found
        return Optional.ofNullable(mongoReaderDetails)
                .map(readerDetailsMapper::fromMongoReaderDetailsModel);
    }

    @Override
    public int getCountFromCurrentYear() {
        int currentYear = java.time.LocalDate.now().getYear();
        Query query = new Query();
        query.addCriteria(Criteria.where("user.createdAt").gte(java.time.LocalDate.of(currentYear, 1, 1))
                .lt(java.time.LocalDate.of(currentYear + 1, 1, 1)));
        long count = mt.count(query, MongoReaderDetailsModel.class);
        return (int) count;
    }

    @Override
    public Page<ReaderDetails> findTopReaders(Pageable pageable) {
        Query query = new Query();
        query.with(pageable);
        query.addCriteria(Criteria.where("lendings").exists(true)); // Assuming lendings are stored as a reference in ReaderDetails

        List<MongoReaderDetailsModel> list = mt.find(query, MongoReaderDetailsModel.class);

        // Count total documents matching the criteria
        long total = mt.count(query, MongoReaderDetailsModel.class);

        List<ReaderDetails> readerDetails = readerDetailsMapper.fromMongoReaderDetailsModel(list);

        return new PageImpl<>(readerDetails, pageable, total);
    }

    @Override
    public List<ReaderDetails> searchReaderDetails(pt.psoft.g1.psoftg1.shared.services.Page page, SearchReadersQuery query) {
        Query mongoQuery = new Query();
        Criteria criteria = new Criteria();

        if (query.getName() != null && !query.getName().isEmpty()) {
            criteria.orOperator(Criteria.where("user.name").regex(query.getName(), "i"));
        }
        if (query.getEmail() != null && !query.getEmail().isEmpty()) {
            criteria.orOperator(Criteria.where("user.username").is(query.getEmail()));
        }
        if (query.getPhoneNumber() != null && !query.getPhoneNumber().isEmpty()) {
            criteria.orOperator(Criteria.where("phoneNumber").is(query.getPhoneNumber()));
        }

        if (criteria != null) {
            mongoQuery.addCriteria(criteria);
        }

        mongoQuery.skip((long) (page.getNumber() - 1) * page.getLimit()).limit(page.getLimit());

        return mt.find(mongoQuery, ReaderDetails.class);
    }

    @Override
    public ReaderDetails save(ReaderDetails readerDetails) {
        // Map ReaderDetails to its MongoDB model
        MongoReaderDetailsModel mongoReaderDetails = readerDetailsMapper.toMongoReaderDetailsModel(readerDetails);

        // Save the photo first if it exists
        if (mongoReaderDetails.getPhoto() != null) {
            MongoPhotoModel photo = mongoReaderDetails.getPhoto();

            // Save the photo and get the saved instance back
            photo = mt.save(photo); // Save the photo first

            // Set the saved photo back to the mongoReaderDetails
            mongoReaderDetails.setPhoto(photo);
        }

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
    public Optional<ReaderDetails> findById(Long id) {
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
