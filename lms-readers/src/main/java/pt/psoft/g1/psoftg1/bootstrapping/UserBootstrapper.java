package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Profile("bootstrap")
@Order(1)
public class UserBootstrapper implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ReaderRepository readerRepository;
    private final GenreRepository genreRepository;
    private final JdbcTemplate jdbcTemplate;
    private List<String> queriesToExecute = new ArrayList<>();

    @Override
    @Transactional
    public void run(final String... args) throws InstantiationException {
        createReaders();
        executeQueries();
    }

    private void createReaders() {
        Genre genre1 = new Genre("Fantasia");
        Genre genre2 = new Genre("Infantil");

        if(genreRepository.findByString("Fantasia").isEmpty()) {
            genreRepository.save(genre1);
        }

        if(genreRepository.findByString("Infantil").isEmpty()) {
            genreRepository.save(genre2);
        }

        //Reader1 - Manuel
        if (userRepository.findByUsername("manuel@gmail.com").isEmpty()) {
            User user = new User("manuel@gmail.com");
            userRepository.save(user);

            Optional<ReaderDetails> readerDetails1 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/1");
            Optional<Genre> g1 = genreRepository.findByString("Fantasia");
            Optional<Genre> g2 = genreRepository.findByString("Infantil");

            List<String> interestList = new ArrayList<>();
            g1.ifPresent(genre -> interestList.add(genre.getGenre()));
            g2.ifPresent(genre -> interestList.add(genre.getGenre()));

            if (readerDetails1.isEmpty()) {
                ReaderDetails r1 = new ReaderDetails(
                        "1",
                        "2000-01-01",
                        "919191919",
                        true,
                        true,
                        true,
                        "readerPhotoTest.jpg", user.getUsername(), interestList);

                readerRepository.save(r1);
            }
        }

        // Reader2 - João
        if (userRepository.findByUsername("joao@gmail.com").isEmpty()) {
            User user = new User("joao@gmail.com");
            userRepository.save(user);

            Optional<ReaderDetails> readerDetails2 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/2");
            if (readerDetails2.isEmpty()) {
                ReaderDetails r2 = new ReaderDetails(
                        "2",
                        "1995-06-02",
                        "929292929",
                        true,
                        false,
                        false,
                        null,
                        user.getUsername(),
                        List.of()); // No interests specified
                readerRepository.save(r2);
            }
        }

// Reader3 - Pedro
        if (userRepository.findById("pedro@gmail.com").isEmpty()) {
            User user = new User("pedro@gmail.com");
            userRepository.save(user);

            Optional<ReaderDetails> readerDetails3 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/3");
            Optional<Genre> g1 = genreRepository.findByString("Fantasia");

            List<String> interestList = new ArrayList<>();
            g1.ifPresent(genre -> interestList.add(genre.getGenre()));

            if (readerDetails3.isEmpty()) {
                ReaderDetails r3 = new ReaderDetails(
                        "3",
                        "2001-12-03",
                        "939393939",
                        true,
                        false,
                        true,
                        "readerPhotoTest.jpg",
                        user.getUsername(),
                        interestList);
                readerRepository.save(r3);
            }
        }

// Reader4 - Catarina
        if (userRepository.findById("catarina@gmail.com").isEmpty()) {
            User user = new User("catarina@gmail.com");
            userRepository.save(user);

            Optional<ReaderDetails> readerDetails4 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/4");
            if (readerDetails4.isEmpty()) {
                ReaderDetails r4 = new ReaderDetails(
                        "4",
                        "2002-03-20",
                        "912345678",
                        true,
                        false,
                        true,
                        null,
                        user.getUsername(),
                        List.of()); // No interests specified
                readerRepository.save(r4);
            }
        }

// Reader5 - Marcelo
        if (userRepository.findById("marcelo@gmail.com").isEmpty()) {
            User user = new User("marcelo@gmail.com");
            userRepository.save(user);

            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/5");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r5 = new ReaderDetails(
                        "5",
                        "2000-06-03",
                        "912355678",
                        true,
                        false,
                        true,
                        null,
                        user.getUsername(),
                        List.of()); // No interests specified
                readerRepository.save(r5);
            }
        }

// Reader6 - Luís
        if (userRepository.findById("luis@gmail.com").isEmpty()) {
            User user = new User("luis@gmail.com");
            userRepository.save(user);

            Optional<ReaderDetails> readerDetails6 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/6");
            if (readerDetails6.isEmpty()) {
                ReaderDetails r6 = new ReaderDetails(
                        "6",
                        "1999-03-03",
                        "912355678",
                        true,
                        false,
                        true,
                        null,
                        user.getUsername(),
                        List.of()); // No interests specified
                readerRepository.save(r6);
            }
        }

// Reader7 - António
        if (userRepository.findById("antonio@gmail.com").isEmpty()) {
            User user = new User("antonio@gmail.com");
            userRepository.save(user);

            Optional<ReaderDetails> readerDetails7 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/7");
            if (readerDetails7.isEmpty()) {
                ReaderDetails r7 = new ReaderDetails(
                        "7",
                        "2001-03-03",
                        "912355778",
                        true,
                        false,
                        true,
                        null,
                        user.getUsername(),
                        List.of()); // No interests specified
                readerRepository.save(r7);
            }
        }

// Reader8 - André
        if (userRepository.findById("andre@gmail.com").isEmpty()) {
            User user = new User("andre@gmail.com");
            userRepository.save(user);

            Optional<ReaderDetails> readerDetails8 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/8");
            if (readerDetails8.isEmpty()) {
                ReaderDetails r8 = new ReaderDetails(
                        "8",
                        "2001-03-03",
                        "912355888",
                        true,
                        false,
                        true,
                        null,
                        user.getUsername(),
                        List.of()); // No interests specified
                readerRepository.save(r8);
            }
        }

// Reader9 - Maria
        if (userRepository.findById("maria@gmail.com").isEmpty()) {
            User user = new User("maria@gmail.com");
            userRepository.save(user);

            Optional<ReaderDetails> readerDetails9 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/9");
            Optional<Genre> g1 = genreRepository.findByString("Romance");
            Optional<Genre> g2 = genreRepository.findByString("Thriller");

            List<String> interestList = new ArrayList<>();
            g1.ifPresent(genre -> interestList.add(genre.getGenre()));
            g2.ifPresent(genre -> interestList.add(genre.getGenre()));

            if (readerDetails9.isEmpty()) {
                ReaderDetails r9 = new ReaderDetails(
                        "9",
                        "1998-11-22",
                        "919393939",
                        true,
                        true,
                        true,
                        null,
                        user.getUsername(),
                        interestList);
                readerRepository.save(r9);
            }
        }

// Reader10 - Ana
        if (userRepository.findById("ana@gmail.com").isEmpty()) {
            User user = new User("ana@gmail.com");
            userRepository.save(user);

            Optional<ReaderDetails> readerDetails10 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/10");
            Optional<Genre> g1 = genreRepository.findByString("Thriller");
            Optional<Genre> g2 = genreRepository.findByString("Fantasia");

            List<String> interestList = new ArrayList<>();
            g1.ifPresent(genre -> interestList.add(genre.getGenre()));
            g2.ifPresent(genre -> interestList.add(genre.getGenre()));

            if (readerDetails10.isEmpty()) {
                ReaderDetails r10 = new ReaderDetails(
                        "10",
                        "2001-04-05",
                        "919494949",
                        true,
                        true,
                        true,
                        null,
                        user.getUsername(),
                        interestList);
                readerRepository.save(r10);
            }
        }

        // Reade11 - Francisco
        if (userRepository.findByUsername("francisco@gmail.com").isEmpty()) {
            User user = new User("francisco@gmail.com");
            userRepository.save(user);

            Optional<ReaderDetails> readerDetails11 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/11");
            Optional<Genre> g1 = genreRepository.findByString("Thriller");
            Optional<Genre> g2 = genreRepository.findByString("Romance");

            List<String> interestList = new ArrayList<>();
            g1.ifPresent(genre -> interestList.add(genre.getGenre()));
            g2.ifPresent(genre -> interestList.add(genre.getGenre()));

            if (readerDetails11.isEmpty()) {
                ReaderDetails r11 = new ReaderDetails(
                        "11",
                        "2001-04-05",
                        "919494949",
                        true,
                        true,
                        true,
                        null,
                        user.getUsername(),
                        interestList);
                readerRepository.save(r11);
            }
        }

        // Reader12 - Ricardo
        if (userRepository.findByUsername("ricardo@gmail.com").isEmpty()) {
            User user = new User("ricardo@gmail.com");
            userRepository.save(user);

            Optional<ReaderDetails> readerDetails12 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/12");
            Optional<Genre> g1 = genreRepository.findByString("Fantasia");
            Optional<Genre> g2 = genreRepository.findByString("Romance");

            List<String> interestList = new ArrayList<>();
            g1.ifPresent(genre -> interestList.add(genre.getGenre()));
            g2.ifPresent(genre -> interestList.add(genre.getGenre()));

            if (readerDetails12.isEmpty()) {
                ReaderDetails r12 = new ReaderDetails(
                        "12",
                        "2001-04-05",
                        "919494949",
                        true,
                        true,
                        true,
                        null,
                        user.getUsername(),
                        interestList);
                readerRepository.save(r12);
            }
        }
    }

    private void executeQueries() {
        for (String query : queriesToExecute) {
            jdbcTemplate.update(query);
        }
    }
}
