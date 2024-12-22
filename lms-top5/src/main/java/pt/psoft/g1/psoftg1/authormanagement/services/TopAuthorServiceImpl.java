package pt.psoft.g1.psoftg1.authormanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorCountView;
import pt.psoft.g1.psoftg1.authormanagement.model.TopAuthor;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.repositories.TopAuthorRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class TopAuthorServiceImpl implements TopAuthorService {

    @Value("${topAuthors}")
    private int topAuthorsLimit;

    private final AuthorRepository authorRepository;
    private final TopAuthorRepository topAuthorRepository;

    @Override
    public List<TopAuthor> findTopX() {
        return topAuthorRepository.findAll();
    }

    @Override
    @Transactional
    public void updateTopX() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        Pageable pageableRules = PageRequest.of(0, topAuthorsLimit);
        // fetch top 5 most lent authors
        List<AuthorCountView> topAuthors = authorRepository.findTopXAuthorByLendings(pageableRules).getContent();

        if(topAuthors.isEmpty()) {
            return;
        }

        // Clear existing records
        topAuthorRepository.deleteAll();

        // Save new top authors
        for (AuthorCountView e : topAuthors) {
            TopAuthor topAuthor = new TopAuthor(e.getAuthorName(), e.getLendingCount());
            topAuthorRepository.save(topAuthor);
        }
    }
}
