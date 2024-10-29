package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.JpaLendingModel;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.MongoLendingModel;
import pt.psoft.g1.psoftg1.lendingmanagement.mapper.LendingMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LendingMongoRepoImpl implements LendingRepository {

    private final MongoTemplate mt;
    private final LendingMapper lendingMapper;

    @Override
    public List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn) {
        Query query = new Query();
        query.addCriteria(Criteria.where("book.isbn.isbn").is(isbn)
                .and("readerDetails.readerNumber.readerNumber").is(readerNumber));
        return mt.find(query, Lending.class);
    }

    @Override
    public int getCountFromCurrentYear() {
        Query query = new Query(Criteria.where("startDate").gte(LocalDate.now().withDayOfYear(1))); // Start of the current year
        return (int) mt.count(query, Lending.class);
    }

    @Override
    public List<Lending> listOutstandingByReaderNumber(String readerNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("readerDetails.readerNumber.readerNumber").is(readerNumber)
                .and("returnedDate").is(null));
        return mt.find(query, Lending.class);
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

        AggregationResults<AverageDurationResult> result = mt.aggregate(aggregation, Lending.class, AverageDurationResult.class);
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
        AggregationResults<AverageDurationResult> result = mt.aggregate(aggregation, Lending.class, AverageDurationResult.class);
        return result.getUniqueMappedResult() != null ? result.getUniqueMappedResult().getAverageDuration() : null;
    }

    @Override
    public List<Lending> getOverdue(Page page) {
        Query query = new Query();
        query.addCriteria(Criteria.where("returnedDate").is(null)
                .and("limitDate").lt(LocalDate.now()));
        query.with(Sort.by(Sort.Order.asc("limitDate"))); // Order by limitDate, oldest first
        query.with(PageRequest.of(page.getNumber() - 1, page.getLimit())); // Pagination
        return mt.find(query, Lending.class);
    }

    @Override
    public List<Lending> searchLendings(Page page, String readerNumber, String isbn, Boolean returned, LocalDate startDate, LocalDate endDate) {
        Query query = new Query();

        // Build dynamic criteria
        Criteria criteria = new Criteria();
        if (StringUtils.hasText(readerNumber)) {
            criteria.and("readerDetails.readerNumber.readerNumber").regex(readerNumber); // Use regex for partial matching
        }
        if (StringUtils.hasText(isbn)) {
            criteria.and("book.isbn.isbn").regex(isbn); // Use regex for partial matching
        }
        if (returned != null) {
            if (returned) {
                criteria.and("returnedDate").ne(null);
            } else {
                criteria.and("returnedDate").is(null);
            }
        }
        if (startDate != null) {
            criteria.and("startDate").gte(startDate);
        }
        if (endDate != null) {
            criteria.and("startDate").lte(endDate);
        }
        query.addCriteria(criteria);
        query.with(Sort.by(Sort.Order.asc("lendingNumber"))); // Order by lendingNumber
        query.with(PageRequest.of(page.getNumber() - 1, page.getLimit())); // Pagination

        return mt.find(query, Lending.class);
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

        return mongoLendings.stream()
                .map(lendingMapper::fromMongoLendingModel)
                .collect(Collectors.toList());
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
        query.addCriteria(Criteria.where("lendingNumber.lendingNumber").is(lendingNumber));
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
