package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
    private final JdbcTemplate jdbcTemplate;
    private List<String> queriesToExecute = new ArrayList<>();

    private final FactoryUser _factoryUser;

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
        }

        //Reader2 - João
        if (userRepository.findByUsername("joao@gmail.com").isEmpty()) {
            Reader joao = Reader.newReader("joao@gmail.com", "Joaoratao!123", "João Ratao");
            User m = userRepository.save(joao);
            joao.setPk(m.getPk());
            joao.setVersion(m.getVersion());
        }

        //Reader3 - Pedro
        if (userRepository.findByUsername("pedro@gmail.com").isEmpty()) {
            Reader pedro = Reader.newReader("pedro@gmail.com", "Pedrodascenas!123", "Pedro Das Cenas");
            User m = userRepository.save(pedro);
            pedro.setPk(m.getPk());
            pedro.setVersion(m.getVersion());
        }

        //Reader4 - Catarina
        if (userRepository.findByUsername("catarina@gmail.com").isEmpty()) {
            Reader catarina = Reader.newReader("catarina@gmail.com", "Catarinamartins!123", "Catarina Martins");
            User m = userRepository.save(catarina);
            catarina.setPk(m.getPk());
            catarina.setVersion(m.getVersion());
        }

        //Reader5 - Marcelo
        if (userRepository.findByUsername("marcelo@gmail.com").isEmpty()) {
            Reader marcelo = Reader.newReader("marcelo@gmail.com", "Marcelosousa!123", "Marcelo Rebelo de Sousa");
            User m = userRepository.save(marcelo);
            marcelo.setPk(m.getPk());
            marcelo.setVersion(m.getVersion());
        }

        //Reader6 - Luís
        if (userRepository.findByUsername("luis@gmail.com").isEmpty()) {
            Reader luis = Reader.newReader("luis@gmail.com", "Luismontenegro!123", "Luís Montenegro");
            User m = userRepository.save(luis);
            luis.setPk(m.getPk());
            luis.setVersion(m.getVersion());
        }

        //Reader7 - António
        if (userRepository.findByUsername("antonio@gmail.com").isEmpty()) {
            Reader antonio = Reader.newReader("antonio@gmail.com", "Antoniocosta!123", "António Costa");
            User m = userRepository.save(antonio);
            antonio.setPk(m.getPk());
            antonio.setVersion(m.getVersion());
        }

        //Reader8 - André
        if (userRepository.findByUsername("andre@gmail.com").isEmpty()) {
            Reader andre = Reader.newReader("andre@gmail.com", "Andreventura!123", "André Ventura");
            User m = userRepository.save(andre);
            andre.setPk(m.getPk());
            andre.setVersion(m.getVersion());
        }

        // Reader9 - Maria
        if (userRepository.findByUsername("maria@gmail.com").isEmpty()) {
            Reader maria = Reader.newReader("mariapg@gmail.com", "Mariazinhawww123!", "Maria Pereira Gonçalves");
            User m = userRepository.save(maria);
            maria.setPk(m.getPk());
            maria.setVersion(m.getVersion());
        }

        // Reader10 - Ana
        if (userRepository.findByUsername("ana@gmail.com").isEmpty()) {
            Reader ana = Reader.newReader("ana@gmail.com", "Ana1233445643!", "Ana Costa");
            User m = userRepository.save(ana);
            ana.setPk(m.getPk());
            ana.setVersion(m.getVersion());
        }

        // Reade11 - Francisco
        if (userRepository.findByUsername("francisco@gmail.com").isEmpty()) {
            Reader francisco = Reader.newReader("francisco@gmail.com", "Franciswwco123!", "Francisco Almeida dos Santos");
            User m = userRepository.save(francisco);
            francisco.setPk(m.getPk());
            francisco.setVersion(m.getVersion());
        }

        // Reader12 - Ricardo
        if (userRepository.findByUsername("ricardo@gmail.com").isEmpty()) {
            Reader ricardo = Reader.newReader("ricardo@gmail.com", "Ricardodsadsa8123!", "Ricardo Reis");
            User m = userRepository.save(ricardo);
            ricardo.setPk(m.getPk());
            ricardo.setVersion(m.getVersion());
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
