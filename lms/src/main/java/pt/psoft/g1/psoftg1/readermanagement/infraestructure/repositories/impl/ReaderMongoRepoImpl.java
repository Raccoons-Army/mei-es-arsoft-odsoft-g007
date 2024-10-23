package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ReaderMongoRepoImpl implements ReaderRepository {

    private final MongoTemplate mt;

    @Override
    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("readerNumber").is(readerNumber));
        return Optional.ofNullable(mt.findOne(query, ReaderDetails.class));
    }

    @Override
    public List<ReaderDetails> findByPhoneNumber(String phoneNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("phoneNumber").is(phoneNumber));
        return mt.find(query, ReaderDetails.class);
    }

    @Override
    public Optional<ReaderDetails> findByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user.username").is(username)); // Assuming ReaderDetails embeds User or references it
        return Optional.ofNullable(mt.findOne(query, ReaderDetails.class));
    }

    @Override
    public Optional<ReaderDetails> findByUserId(Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user.id").is(userId)); // Adjust if User is referenced differently
        return Optional.ofNullable(mt.findOne(query, ReaderDetails.class));
    }

    @Override
    public int getCountFromCurrentYear() {
        int currentYear = java.time.LocalDate.now().getYear();
        Query query = new Query();
        query.addCriteria(Criteria.where("user.createdAt").gte(java.time.LocalDate.of(currentYear, 1, 1))
                .lt(java.time.LocalDate.of(currentYear + 1, 1, 1)));
        long count = mt.count(query, ReaderDetails.class);
        return (int) count;
    }

    @Override
    public Page<ReaderDetails> findTopReaders(Pageable pageable) {
        Query query = new Query();
        query.with(pageable);
        query.addCriteria(Criteria.where("lendings").exists(true)); // Assuming lendings are stored as a reference in ReaderDetails

        List<ReaderDetails> readerDetails = mt.find(query, ReaderDetails.class);

        // Count total documents matching the criteria
        long total = mt.count(query, ReaderDetails.class);

        return new PageImpl<>(readerDetails, pageable, total);
    }

    @Override
    public Page<ReaderBookCountDTO> findTopByGenre(Pageable pageable, String genre, LocalDate startDate, LocalDate endDate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("lendings.book.genre").is(genre)
                .and("lendings.startDate").gte(startDate)
                .and("lendings.startDate").lte(endDate));
        query.with(pageable);

        List<ReaderBookCountDTO> results = mt.find(query, ReaderBookCountDTO.class);
        long total = mt.count(query, ReaderBookCountDTO.class);

        return new PageImpl<>(results, pageable, total);
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
    public ReaderDetails save(ReaderDetails entity) {
        return mt.save(entity);
    }

    @Override
    public void delete(ReaderDetails entity) {
        mt.remove(entity);
    }

    @Override
    public List<ReaderDetails> findAll() {
        return mt.findAll(ReaderDetails.class);
    }

    @Override
    public Optional<ReaderDetails> findById(Long id) {
        return Optional.ofNullable(mt.findById(id, ReaderDetails.class));
    }
}
