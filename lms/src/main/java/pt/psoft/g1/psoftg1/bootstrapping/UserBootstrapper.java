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
import pt.psoft.g1.psoftg1.usermanagement.model.Librarian;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    public void run(final String... args) {
        createReaders();
        createLibrarian();
        executeQueries();
    }

    private void createReaders() {
        //Reader1 - Manuel
        if (userRepository.findByUsername("manuel@gmail.com").isEmpty()) {
            final Reader manuel = Reader.newReader("manuel@gmail.com", "Manuelino123!", "Manuel Sarapinto das Coives");
            userRepository.save(manuel);

            //String dateFormat = LocalDateTime.of(LocalDate.of(2024, 1, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String dateFormat = LocalDateTime.of(2024, 1, 20, 0, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, manuel.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails1 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/1");
            Optional<Genre> g1 = genreRepository.findByString("Fantasia");
            Optional<Genre> g2 = genreRepository.findByString("Infantil");
            List<Genre> interestList = new ArrayList<>();
            if (g1.isPresent()) {
                interestList.add(g1.get());
            }

            if (g2.isPresent()) {
                interestList.add(g2.get());
            }

            if (readerDetails1.isEmpty()) {
                ReaderDetails r1 = new ReaderDetails(
                        1,
                        manuel,
                        "2000-01-01",
                        "919191919",
                        true,
                        true,
                        true,
                        "readerPhotoTest.jpg",
                        interestList);
                readerRepository.save(r1);
            }
        }

        //Reader2 - João
        if (userRepository.findByUsername("joao@gmail.com").isEmpty()) {
            final Reader joao = Reader.newReader("joao@gmail.com", "Joaoratao!123", "João Ratao");
            userRepository.save(joao);
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 3, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, joao.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            //jdbcTemplate.update("UPDATE PUBLIC.T_USER SET CREATED_AT = ? WHERE USERNAME = ?", LocalDateTime.of(LocalDate.of(2024, 1, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), joao.getUsername());jdbcTemplate.update("UPDATE PUBLIC.T_USER SET CREATED_AT = ? WHERE USERNAME = ?", LocalDateTime.of(LocalDate.of(2024, 1, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), joao.getUsername());


            Optional<ReaderDetails> readerDetails2 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/2");
            if (readerDetails2.isEmpty()) {
                ReaderDetails r2 = new ReaderDetails(
                        2,
                        joao,
                        "1995-06-02",
                        "929292929",
                        true,
                        false,
                        false,
                        null,
                        null);
                readerRepository.save(r2);
            }
        }

        //Reader3 - Pedro
        if (userRepository.findByUsername("pedro@gmail.com").isEmpty()) {
            final Reader pedro = Reader.newReader("pedro@gmail.com", "Pedrodascenas!123", "Pedro Das Cenas");
            userRepository.save(pedro);
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 1, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, pedro.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails3 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/3");
            if (readerDetails3.isEmpty()) {
                ReaderDetails r3 = new ReaderDetails(
                        3,
                        pedro,
                        "2001-12-03",
                        "939393939",
                        true,
                        false,
                        true,
                        null,
                        null);
                readerRepository.save(r3);
            }
        }

        //Reader4 - Catarina
        if (userRepository.findByUsername("catarina@gmail.com").isEmpty()) {
            final Reader catarina = Reader.newReader("catarina@gmail.com", "Catarinamartins!123", "Catarina Martins");
            userRepository.save(catarina);
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 3, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, catarina.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails4 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/4");
            if (readerDetails4.isEmpty()) {
                ReaderDetails r4 = new ReaderDetails(
                        4,
                        catarina,
                        "2002-03-20",
                        "912345678",
                        true,
                        false,
                        true,
                        null,
                        null);
                readerRepository.save(r4);
            }
        }

        //Reader5 - Marcelo
        if (userRepository.findByUsername("marcelo@gmail.com").isEmpty()) {
            final Reader marcelo = Reader.newReader("marcelo@gmail.com", "Marcelosousa!123", "Marcelo Rebelo de Sousa");
            userRepository.save(marcelo);
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 1, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, marcelo.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/5");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r5 = new ReaderDetails(
                        5,
                        marcelo,
                        "2000-06-03",
                        "912355678",
                        true,
                        false,
                        true,
                        null,
                        null);
                readerRepository.save(r5);
            }
        }

        //Reader6 - Luís
        if (userRepository.findByUsername("luis@gmail.com").isEmpty()) {
            final Reader luis = Reader.newReader("luis@gmail.com", "Luismontenegro!123", "Luís Montenegro");
            userRepository.save(luis);
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 3, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, luis.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/6");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r6 = new ReaderDetails(
                        6,
                        luis,
                        "1999-03-03",
                        "912355678",
                        true,
                        false,
                        true,
                        null,
                        null);
                readerRepository.save(r6);
            }
        }

        //Reader7 - António
        if (userRepository.findByUsername("antonio@gmail.com").isEmpty()) {
            final Reader antonio = Reader.newReader("antonio@gmail.com", "Antoniocosta!123", "António Costa");
            userRepository.save(antonio);
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 6, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, antonio.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/7");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r7 = new ReaderDetails(
                        7,
                        antonio,
                        "2001-03-03",
                        "912355778",
                        true,
                        false,
                        true,
                        null,
                        null);
                readerRepository.save(r7);
            }
        }

        //Reader8 - André
        if (userRepository.findByUsername("andre@gmail.com").isEmpty()) {
            final Reader andre = Reader.newReader("andre@gmail.com", "Andreventura!123", "André Ventura");
            userRepository.save(andre);
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 5, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, andre.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/8");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r5 = new ReaderDetails(
                        8,
                        andre,
                        "2001-03-03",
                        "912355888",
                        true,
                        false,
                        true,
                        null,
                        null);
                readerRepository.save(r5);
            }
        }

        // Reader9 - Maria
        if (userRepository.findByUsername("maria@gmail.com").isEmpty()) {
            final Reader maria = Reader.newReader("mariapg@gmail.com", "Mariazinhawww123!", "Maria Pereira Gonçalves");
            userRepository.save(maria);

            String dateFormat = LocalDateTime.of(2024, 1, 23, 0, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, maria.getUsername());
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails3 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/9");
            Optional<Genre> g1 = genreRepository.findByString("Romance");
            Optional<Genre> g2 = genreRepository.findByString("Thriller");
            List<Genre> interestList = new ArrayList<>();
            if (g1.isPresent()) {
                interestList.add(g1.get());
            }

            if (g2.isPresent()) {
                interestList.add(g2.get());
            }

            if (readerDetails3.isEmpty()) {
                ReaderDetails r3 = new ReaderDetails(
                        9,
                        maria,
                        "1998-11-22",
                        "919393939",
                        true,
                        true,
                        true,
                        null,
                        interestList);
                readerRepository.save(r3);
            }
        }

        // Reader10 - Ana
        if (userRepository.findByUsername("ana@gmail.com").isEmpty()) {
            final Reader ana = Reader.newReader("ana@gmail.com", "Ana1233445643!", "Ana Costa");
            userRepository.save(ana);

            String dateFormat = LocalDateTime.of(2024, 1, 24, 0, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, ana.getUsername());
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails4 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/10");
            Optional<Genre> g1 = genreRepository.findByString("Thriller");
            Optional<Genre> g2 = genreRepository.findByString("Fantasia");
            List<Genre> interestList = new ArrayList<>();
            if (g1.isPresent()) {
                interestList.add(g1.get());
            }

            if (g2.isPresent()) {
                interestList.add(g2.get());
            }

            if (readerDetails4.isEmpty()) {
                ReaderDetails r4 = new ReaderDetails(
                        10,
                        ana,
                        "2001-04-05",
                        "919494949",
                        true,
                        true,
                        true,
                        null,
                        interestList);
                readerRepository.save(r4);
            }
        }

        // Reade11 - Francisco
        if (userRepository.findByUsername("francisco@gmail.com").isEmpty()) {
            final Reader francisco = Reader.newReader("francisco@gmail.com", "Franciswwco123!", "Francisco Almeida dos Santos");
            userRepository.save(francisco);

            String dateFormat = LocalDateTime.of(2024, 1, 25, 0, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, francisco.getUsername());
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/11");
            Optional<Genre> g1 = genreRepository.findByString("Informação");
            Optional<Genre> g2 = genreRepository.findByString("Juvenil");
            List<Genre> interestList = new ArrayList<>();
            if (g1.isPresent()) {
                interestList.add(g1.get());
            }

            if (g2.isPresent()) {
                interestList.add(g2.get());
            }

            if (readerDetails5.isEmpty()) {
                ReaderDetails r5 = new ReaderDetails(
                        11,
                        francisco,
                        "2015-08-10",
                        "919595959",
                        true,
                        true,
                        true,
                        null,
                        interestList);
                readerRepository.save(r5);
            }
        }

        // Reader12 - Ricardo
        if (userRepository.findByUsername("ricardo@gmail.com").isEmpty()) {
            final Reader ricardo = Reader.newReader("ricardo@gmail.com", "Ricardodsadsa8123!", "Ricardo Reis");
            userRepository.save(ricardo);

            String dateFormat = LocalDateTime.of(2024, 1, 26, 0, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, ricardo.getUsername());
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails6 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/12");
            Optional<Genre> g1 = genreRepository.findByString("Informação");
            Optional<Genre> g2 = genreRepository.findByString("Juvenil");
            List<Genre> interestList = new ArrayList<>();
            if (g1.isPresent()) {
                interestList.add(g1.get());
            }

            if (g2.isPresent()) {
                interestList.add(g2.get());
            }

            if (readerDetails6.isEmpty()) {
                ReaderDetails r6 = new ReaderDetails(
                        12,
                        ricardo,
                        "2013-03-12",
                        "919696969",
                        true,
                        true,
                        true,
                        null,
                        interestList);
                readerRepository.save(r6);
            }
        }


    }

    private void createLibrarian() {
        // Maria
        if (userRepository.findByUsername("maria@gmail.com").isEmpty()) {
            final User maria = Librarian.newLibrarian("maria@gmail.com", "Mariaroberta!123", "Maria Roberta");
            userRepository.save(maria);
        }
    }

    private void executeQueries() {
        for (String query : queriesToExecute) {
            jdbcTemplate.update(query);
        }
    }
}
