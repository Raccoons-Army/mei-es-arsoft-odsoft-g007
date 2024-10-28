package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.usermanagement.model.FactoryUser;
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

    private final FactoryUser _factoryUser;
    private final FactoryGenre _factoryGenre;

    @Override
    @Transactional
    public void run(final String... args) throws InstantiationException {
        createReaders();
        createLibrarian();
        executeQueries();
    }

    private void createReaders() throws InstantiationException {
        //Reader1 - Manuel
        if (userRepository.findByUsername("manuel@gmail.com").isEmpty()) {
            Reader manuel = Reader.newReader("manuel@gmail.com", "Manuelino123!", "Manuel Sarapinto das Coives");
            User m = userRepository.save(manuel);
            manuel.setPk(m.getPk());
            manuel.setVersion(m.getVersion());

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
                        "1",
                        "2000-01-01",
                        "919191919",
                        true,
                        true,
                        true,
                        "readerPhotoTest.jpg",
                        _factoryUser, _factoryGenre);

                r1.setReader(manuel);

                r1.setInterestList(interestList);

                readerRepository.save(r1);
            }
        }

        //Reader2 - João
        if (userRepository.findByUsername("joao@gmail.com").isEmpty()) {
            Reader joao = Reader.newReader("joao@gmail.com", "Joaoratao!123", "João Ratao");
            User m = userRepository.save(joao);
            joao.setPk(m.getPk());
            joao.setVersion(m.getVersion());

            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 3, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, joao.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            //jdbcTemplate.update("UPDATE PUBLIC.T_USER SET CREATED_AT = ? WHERE USERNAME = ?", LocalDateTime.of(LocalDate.of(2024, 1, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), joao.getUsername());jdbcTemplate.update("UPDATE PUBLIC.T_USER SET CREATED_AT = ? WHERE USERNAME = ?", LocalDateTime.of(LocalDate.of(2024, 1, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), joao.getUsername());


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
                        _factoryUser, _factoryGenre);

                r2.setReader(joao);
                readerRepository.save(r2);
            }
        }

        //Reader3 - Pedro
        if (userRepository.findByUsername("pedro@gmail.com").isEmpty()) {
            Reader pedro = Reader.newReader("pedro@gmail.com", "Pedrodascenas!123", "Pedro Das Cenas");
            User m = userRepository.save(pedro);
            pedro.setPk(m.getPk());
            pedro.setVersion(m.getVersion());
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 1, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, pedro.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails3 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/3");
            if (readerDetails3.isEmpty()) {
                ReaderDetails r3 = new ReaderDetails(
                        "3",
                        "2001-12-03",
                        "939393939",
                        true,
                        false,
                        true,
                        null,
                        _factoryUser, _factoryGenre);

                r3.defineReader(pedro.getPk(), pedro.getUsername(), pedro.getPassword(), pedro.getName().getName(), pedro.getVersion());
                readerRepository.save(r3);
            }
        }

        //Reader4 - Catarina
        if (userRepository.findByUsername("catarina@gmail.com").isEmpty()) {
            Reader catarina = Reader.newReader("catarina@gmail.com", "Catarinamartins!123", "Catarina Martins");
            User m = userRepository.save(catarina);
            catarina.setPk(m.getPk());
            catarina.setVersion(m.getVersion());
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 3, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, catarina.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
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
                        _factoryUser, _factoryGenre);
                r4.defineReader(catarina.getPk(), catarina.getUsername(), catarina.getPassword(), catarina.getName().getName(), catarina.getVersion());
                readerRepository.save(r4);
            }
        }

        //Reader5 - Marcelo
        if (userRepository.findByUsername("marcelo@gmail.com").isEmpty()) {
            Reader marcelo = Reader.newReader("marcelo@gmail.com", "Marcelosousa!123", "Marcelo Rebelo de Sousa");
            User m = userRepository.save(marcelo);
            marcelo.setPk(m.getPk());
            marcelo.setVersion(m.getVersion());
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 1, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, marcelo.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
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
                        _factoryUser, _factoryGenre);
                r5.defineReader(marcelo.getPk(), marcelo.getUsername(), marcelo.getPassword(), marcelo.getName().getName(), marcelo.getVersion());
                readerRepository.save(r5);
            }
        }

        //Reader6 - Luís
        if (userRepository.findByUsername("luis@gmail.com").isEmpty()) {
            Reader luis = Reader.newReader("luis@gmail.com", "Luismontenegro!123", "Luís Montenegro");
            User m = userRepository.save(luis);
            luis.setPk(m.getPk());
            luis.setVersion(m.getVersion());
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 3, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, luis.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/6");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r6 = new ReaderDetails(
                        "6",
                        "1999-03-03",
                        "912355678",
                        true,
                        false,
                        true,
                        null,
                        _factoryUser, _factoryGenre);
                r6.defineReader(luis.getPk(), luis.getUsername(), luis.getPassword(), luis.getName().getName(), luis.getVersion());
                readerRepository.save(r6);
            }
        }

        //Reader7 - António
        if (userRepository.findByUsername("antonio@gmail.com").isEmpty()) {
            Reader antonio = Reader.newReader("antonio@gmail.com", "Antoniocosta!123", "António Costa");
            User m = userRepository.save(antonio);
            antonio.setPk(m.getPk());
            antonio.setVersion(m.getVersion());
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 6, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, antonio.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/7");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r7 = new ReaderDetails(
                        "7",
                        "2001-03-03",
                        "912355778",
                        true,
                        false,
                        true,
                        null,
                        _factoryUser, _factoryGenre);
                r7.defineReader(antonio.getPk(), antonio.getUsername(), antonio.getPassword(), antonio.getName().getName(), antonio.getVersion());
                readerRepository.save(r7);
            }
        }

        //Reader8 - André
        if (userRepository.findByUsername("andre@gmail.com").isEmpty()) {
            Reader andre = Reader.newReader("andre@gmail.com", "Andreventura!123", "André Ventura");
            User m = userRepository.save(andre);
            andre.setPk(m.getPk());
            andre.setVersion(m.getVersion());
            String dateFormat = LocalDateTime.of(LocalDate.of(2024, 5, 20), LocalTime.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, andre.getUsername());
            //jdbcTemplate.update(query);
            queriesToExecute.add(query);
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/8");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r8 = new ReaderDetails(
                        "8",
                        "2001-03-03",
                        "912355888",
                        true,
                        false,
                        true,
                        null,
                        _factoryUser, _factoryGenre);
                r8.defineReader(andre.getPk(), andre.getUsername(), andre.getPassword(), andre.getName().getName(), andre.getVersion());
                readerRepository.save(r8);
            }
        }

        // Reader9 - Maria
        if (userRepository.findByUsername("maria@gmail.com").isEmpty()) {
            Reader maria = Reader.newReader("mariapg@gmail.com", "Mariazinhawww123!", "Maria Pereira Gonçalves");
            User m = userRepository.save(maria);
            maria.setPk(m.getPk());
            maria.setVersion(m.getVersion());

            String dateFormat = LocalDateTime.of(2024, 1, 23, 0, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, maria.getUsername());
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails3 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/9");
            Optional<Genre> g1 = genreRepository.findByString("Romance");
            Optional<Genre> g2 = genreRepository.findByString("Thriller");

            if (readerDetails3.isEmpty()) {
                ReaderDetails r9 = new ReaderDetails(
                        "9",
                        "1998-11-22",
                        "919393939",
                        true,
                        true,
                        true,
                        null,
                        _factoryUser, _factoryGenre);

                if (g1.isPresent()) {
                    r9.addGenre(g1.get().getGenre());
                }

                if (g2.isPresent()) {
                    r9.addGenre(g2.get().getGenre());
                }

                r9.defineReader(maria.getPk(), maria.getUsername(), maria.getPassword(), maria.getName().getName(), maria.getVersion());

                readerRepository.save(r9);
            }
        }

        // Reader10 - Ana
        if (userRepository.findByUsername("ana@gmail.com").isEmpty()) {
            Reader ana = Reader.newReader("ana@gmail.com", "Ana1233445643!", "Ana Costa");
            User m = userRepository.save(ana);
            ana.setPk(m.getPk());
            ana.setVersion(m.getVersion());

            String dateFormat = LocalDateTime.of(2024, 1, 24, 0, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, ana.getUsername());
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails4 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/10");
            Optional<Genre> g1 = genreRepository.findByString("Thriller");
            Optional<Genre> g2 = genreRepository.findByString("Fantasia");

            if (readerDetails4.isEmpty()) {
                ReaderDetails r10 = new ReaderDetails(
                        "10",
                        "2001-04-05",
                        "919494949",
                        true,
                        true,
                        true,
                        null,
                        _factoryUser, _factoryGenre);

                if (g1.isPresent()) {
                    r10.addGenre(g1.get().getGenre());
                }

                if (g2.isPresent()) {
                    r10.addGenre(g2.get().getGenre());
                }

                r10.defineReader(ana.getPk(), ana.getUsername(), ana.getPassword(), ana.getName().getName(), ana.getVersion());

                readerRepository.save(r10);
            }
        }

        // Reade11 - Francisco
        if (userRepository.findByUsername("francisco@gmail.com").isEmpty()) {
            Reader francisco = Reader.newReader("francisco@gmail.com", "Franciswwco123!", "Francisco Almeida dos Santos");
            User m = userRepository.save(francisco);
            francisco.setPk(m.getPk());
            francisco.setVersion(m.getVersion());

            String dateFormat = LocalDateTime.of(2024, 1, 25, 0, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, francisco.getUsername());
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/11");
            Optional<Genre> g1 = genreRepository.findByString("Informação");
            Optional<Genre> g2 = genreRepository.findByString("Juvenil");

            if (readerDetails5.isEmpty()) {
                ReaderDetails r11 = new ReaderDetails(
                        "11",
                        "2015-08-10",
                        "919595959",
                        true,
                        true,
                        true,
                        null,
                        _factoryUser, _factoryGenre);

                if (g1.isPresent()) {
                    r11.addGenre(g1.get().getGenre());
                }

                if (g2.isPresent()) {
                    r11.addGenre(g2.get().getGenre());
                }

                r11.defineReader(francisco.getPk(), francisco.getUsername(), francisco.getPassword(), francisco.getName().getName(), francisco.getVersion());

                readerRepository.save(r11);
            }
        }

        // Reader12 - Ricardo
        if (userRepository.findByUsername("ricardo@gmail.com").isEmpty()) {
            Reader ricardo = Reader.newReader("ricardo@gmail.com", "Ricardodsadsa8123!", "Ricardo Reis");
            User m = userRepository.save(ricardo);
            ricardo.setPk(m.getPk());
            ricardo.setVersion(m.getVersion());

            String dateFormat = LocalDateTime.of(2024, 1, 26, 0, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            String query = String.format("UPDATE PUBLIC.T_USER SET CREATED_AT = '%s' WHERE USERNAME = '%s'", dateFormat, ricardo.getUsername());
            queriesToExecute.add(query);

            Optional<ReaderDetails> readerDetails6 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/12");
            Optional<Genre> g1 = genreRepository.findByString("Informação");
            Optional<Genre> g2 = genreRepository.findByString("Juvenil");

            if (readerDetails6.isEmpty()) {
                ReaderDetails r12 = new ReaderDetails(
                        "12",
                        "2013-03-12",
                        "919696969",
                        true,
                        true,
                        true,
                        null,
                        _factoryUser, _factoryGenre);

                if (g1.isPresent()) {
                    r12.addGenre(g1.get().getGenre());
                }

                if (g2.isPresent()) {
                    r12.addGenre(g2.get().getGenre());
                }

                r12.defineReader(ricardo.getPk(), ricardo.getUsername(), ricardo.getPassword(), ricardo.getName().getName(), ricardo.getVersion());

                readerRepository.save(r12);
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
