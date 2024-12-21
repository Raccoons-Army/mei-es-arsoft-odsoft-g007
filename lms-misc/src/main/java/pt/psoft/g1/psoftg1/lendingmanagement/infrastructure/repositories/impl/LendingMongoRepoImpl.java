package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.MongoLendingModel;
import pt.psoft.g1.psoftg1.lendingmanagement.mapper.LendingMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class LendingMongoRepoImpl implements LendingRepository {

    private final MongoTemplate mt;
    private final LendingMapper lendingMapper;

    @Override
    public List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn) {
        Query query = new Query();
        query.addCriteria(Criteria.where("book.isbn").is(isbn)
                .and("readerDetails.readerNumber").is(readerNumber));
        List<MongoLendingModel> list = mt.find(query, MongoLendingModel.class);

        return lendingMapper.fromMongoLendingModel(list);
    }

    @Override
    public Double getAverageDuration() {
        // Define the projection operation using $subtract to compute the duration in milliseconds
        ProjectionOperation projectionOperation = Aggregation.project()
                .and("startDate").as("startDate")
                .and("returnedDate").as("returnedDate")
                .and(ArithmeticOperators.Subtract.valueOf("returnedDate").subtract("startDate")).as("duration");

        // Build the aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("returnedDate").exists(true)), // Filter only returned lendings
                projectionOperation,
                Aggregation.group().avg("duration").as("averageDuration")
        );

        AggregationResults<AverageDurationResult> result = mt.aggregate(aggregation, MongoLendingModel.class, AverageDurationResult.class);
        return result.getUniqueMappedResult() != null ? result.getUniqueMappedResult().getAverageDuration() : null;
    }

    @Override
    public Double getAvgLendingDurationByIsbn(String isbn) {
        // Define the aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                // Match lendings where the book ISBN matches the provided ISBN and the book has been returned
                Aggregation.match(Criteria.where("book.isbn.isbn").is(isbn).and("returnedDate").exists(true)),

                // Project the duration by subtracting startDate from returnedDate
                Aggregation.project("startDate", "returnedDate")
                        .and(ArithmeticOperators.Subtract.valueOf("returnedDate").subtract("startDate")).as("duration"),

                // Group and calculate the average duration
                Aggregation.group().avg("duration").as("averageDuration")
        );

        // Perform the aggregation and get the result
        AggregationResults<AverageDurationResult> result = mt.aggregate(aggregation, MongoLendingModel.class, AverageDurationResult.class);
        return result.getUniqueMappedResult() != null ? result.getUniqueMappedResult().getAverageDuration() : null;
    }

    @Override
    public List<Lending> getOverdue(Page page) {
        Query query = new Query();
        query.addCriteria(Criteria.where("returnedDate").is(null)
                .and("limitDate").lt(LocalDate.now()));
        query.with(Sort.by(Sort.Order.asc("limitDate"))); // Order by limitDate, oldest first
        query.with(PageRequest.of(page.getNumber() - 1, page.getLimit())); // Pagination
        List<MongoLendingModel> list = mt.find(query, MongoLendingModel.class);

        return lendingMapper.fromMongoLendingModel(list);
    }

    @Override
    public Lending save(Lending lending) {
        MongoLendingModel mongoLending = lendingMapper.toMongoLendingModel(lending);
        MongoLendingModel savedLending = mt.save(mongoLending);

        return lendingMapper.fromMongoLendingModel(savedLending);
    }

    @Override
    public void delete(Lending lending) {
        MongoLendingModel mongoLending = lendingMapper.toMongoLendingModel(lending);

        mt.remove(mongoLending);
    }

    @Override
    public List<Lending> findAll() {
        List<MongoLendingModel> mongoLendings = mt.findAll(MongoLendingModel.class);

        return lendingMapper.fromMongoLendingModel(mongoLendings);
    }

    @Override
    public Optional<Lending> findById(Long id) {
        MongoLendingModel mongoLending = mt.findById(id, MongoLendingModel.class);

        return Optional.ofNullable(mongoLending)
                .map(lendingMapper::fromMongoLendingModel);
    }

    @Override
    public Optional<Lending> findByLendingNumber(String lendingNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("lendingNumber").is(lendingNumber));
        MongoLendingModel mongoLending = mt.findOne(query, MongoLendingModel.class);

        return Optional.ofNullable(mongoLending)
                .map(lendingMapper::fromMongoLendingModel);
    }

    // Inner class to hold average duration results
    private static class AverageDurationResult {
        private Double averageDuration;

        public Double getAverageDuration() {
            return averageDuration;
        }

        public void setAverageDuration(Double averageDuration) {
            this.averageDuration = averageDuration;
        }
    }
}
