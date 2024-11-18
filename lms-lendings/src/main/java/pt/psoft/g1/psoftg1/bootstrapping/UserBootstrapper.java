package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.usermanagement.model.FactoryUser;
import pt.psoft.g1.psoftg1.usermanagement.model.Librarian;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
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
    private final JdbcTemplate jdbcTemplate;

    private final FactoryUser _factoryUser;

    @Override
    @Transactional
    public void run(final String... args) throws InstantiationException {
        createReaders();
        createLibrarian();
    }

    private void createReaders() throws InstantiationException {
        //Reader1 - Manuel
        if (userRepository.findByUsername("manuel@gmail.com").isEmpty()) {
            Reader manuel = Reader.newReader("manuel@gmail.com");
            User m = userRepository.save(manuel);
            manuel.setPk(m.getPk());
            manuel.setVersion(m.getVersion());
            Optional<ReaderDetails> readerDetails1 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/1");
            if (readerDetails1.isEmpty()) {
                ReaderDetails r1 = new ReaderDetails("1", _factoryUser);
                r1.setReader(manuel);
                readerRepository.save(r1);
            }
        }

        //Reader2 - João
        if (userRepository.findByUsername("joao@gmail.com").isEmpty()) {
            Reader joao = Reader.newReader("joao@gmail.com");
            User m = userRepository.save(joao);
            joao.setPk(m.getPk());
            joao.setVersion(m.getVersion());
            Optional<ReaderDetails> readerDetails2 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/2");
            if (readerDetails2.isEmpty()) {
                ReaderDetails r2 = new ReaderDetails("2", _factoryUser);
                r2.setReader(joao);
                readerRepository.save(r2);
            }
        }

        //Reader3 - Pedro
        if (userRepository.findByUsername("pedro@gmail.com").isEmpty()) {
            Reader pedro = Reader.newReader("pedro@gmail.com");
            User m = userRepository.save(pedro);
            pedro.setPk(m.getPk());
            pedro.setVersion(m.getVersion());
            Optional<ReaderDetails> readerDetails3 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/3");
            if (readerDetails3.isEmpty()) {
                ReaderDetails r3 = new ReaderDetails("3", _factoryUser);
                r3.setReader(pedro);
                readerRepository.save(r3);
            }
        }

        //Reader4 - Catarina
        if (userRepository.findByUsername("catarina@gmail.com").isEmpty()) {
            Reader catarina = Reader.newReader("catarina@gmail.com");
            User m = userRepository.save(catarina);
            catarina.setPk(m.getPk());
            catarina.setVersion(m.getVersion());
            Optional<ReaderDetails> readerDetails4 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/4");
            if (readerDetails4.isEmpty()) {
                ReaderDetails r4 = new ReaderDetails("4", _factoryUser);
                r4.setReader(catarina);
                readerRepository.save(r4);
            }
        }

        //Reader5 - Marcelo
        if (userRepository.findByUsername("marcelo@gmail.com").isEmpty()) {
            Reader marcelo = Reader.newReader("marcelo@gmail.com");
            User m = userRepository.save(marcelo);
            marcelo.setPk(m.getPk());
            marcelo.setVersion(m.getVersion());
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/5");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r5 = new ReaderDetails("5", _factoryUser);
                r5.setReader(marcelo);
                readerRepository.save(r5);
            }
        }

        //Reader6 - Luís
        if (userRepository.findByUsername("luis@gmail.com").isEmpty()) {
            Reader luis = Reader.newReader("luis@gmail.com");
            User m = userRepository.save(luis);
            luis.setPk(m.getPk());
            luis.setVersion(m.getVersion());
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/6");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r6 = new ReaderDetails("6", _factoryUser);
                r6.setReader(luis);
                readerRepository.save(r6);
            }
        }

        //Reader7 - António
        if (userRepository.findByUsername("antonio@gmail.com").isEmpty()) {
            Reader antonio = Reader.newReader("antonio@gmail.com");
            User m = userRepository.save(antonio);
            antonio.setPk(m.getPk());
            antonio.setVersion(m.getVersion());
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/7");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r7 = new ReaderDetails("7", _factoryUser);
                r7.setReader(antonio);
                readerRepository.save(r7);
            }
        }

        //Reader8 - André
        if (userRepository.findByUsername("andre@gmail.com").isEmpty()) {
            Reader andre = Reader.newReader("andre@gmail.com");
            User m = userRepository.save(andre);
            andre.setPk(m.getPk());
            andre.setVersion(m.getVersion());
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/8");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r8 = new ReaderDetails("8", _factoryUser);
                r8.setReader(andre);
                readerRepository.save(r8);
            }
        }

        // Reader9 - Maria
        if (userRepository.findByUsername("maria@gmail.com").isEmpty()) {
            Reader maria = Reader.newReader("mariapg@gmail.com");
            User m = userRepository.save(maria);
            maria.setPk(m.getPk());
            maria.setVersion(m.getVersion());
            Optional<ReaderDetails> readerDetails3 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/9");
            if (readerDetails3.isEmpty()) {
                ReaderDetails r9 = new ReaderDetails("9", _factoryUser);
                r9.setReader(maria);
                readerRepository.save(r9);
            }
        }

        // Reader10 - Ana
        if (userRepository.findByUsername("ana@gmail.com").isEmpty()) {
            Reader ana = Reader.newReader("ana@gmail.com");
            User m = userRepository.save(ana);
            ana.setPk(m.getPk());
            ana.setVersion(m.getVersion());
            Optional<ReaderDetails> readerDetails4 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/10");
            if (readerDetails4.isEmpty()) {
                ReaderDetails r10 = new ReaderDetails("10", _factoryUser);
                r10.setReader(ana);
                readerRepository.save(r10);
            }
        }

        // Reade11 - Francisco
        if (userRepository.findByUsername("francisco@gmail.com").isEmpty()) {
            Reader francisco = Reader.newReader("francisco@gmail.com");
            User m = userRepository.save(francisco);
            francisco.setPk(m.getPk());
            francisco.setVersion(m.getVersion());
            Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/11");
            if (readerDetails5.isEmpty()) {
                ReaderDetails r11 = new ReaderDetails("11", _factoryUser);
                r11.setReader(francisco);
                readerRepository.save(r11);
            }
        }

        // Reader12 - Ricardo
        if (userRepository.findByUsername("ricardo@gmail.com").isEmpty()) {
            Reader ricardo = Reader.newReader("ricardo@gmail.com");
            User m = userRepository.save(ricardo);
            ricardo.setPk(m.getPk());
            ricardo.setVersion(m.getVersion());
            Optional<ReaderDetails> readerDetails6 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/12");
            if (readerDetails6.isEmpty()) {
                ReaderDetails r12 = new ReaderDetails("12", _factoryUser);
                r12.setReader(ricardo);
                readerRepository.save(r12);
            }
        }
    }

    private void createLibrarian() {
        // Maria
        if (userRepository.findByUsername("maria@gmail.com").isEmpty()) {
            final User maria = Librarian.newLibrarian("maria@gmail.com");
            userRepository.save(maria);
        }
    }
}
