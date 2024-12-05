package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Profile("bootstrap")
@Order(1)
public class UserBootstrapper implements CommandLineRunner {

    private final ReaderRepository readerRepository;

    @Override
    @Transactional
    public void run(final String... args) throws InstantiationException {
        createReaders();
    }

    private void createReaders() throws InstantiationException {
        //Reader1 - Manuel
        Optional<ReaderDetails> readerDetails1 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/1");
        if (readerDetails1.isEmpty()) {
            ReaderDetails r1 = new ReaderDetails("1");
            readerRepository.save(r1);
        }

        //Reader2 - João
        Optional<ReaderDetails> readerDetails2 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/2");
        if (readerDetails2.isEmpty()) {
            ReaderDetails r2 = new ReaderDetails("2");
            readerRepository.save(r2);
        }

        //Reader3 - Pedro
        Optional<ReaderDetails> readerDetails3 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/3");
        if (readerDetails3.isEmpty()) {
            ReaderDetails r3 = new ReaderDetails("3");
            readerRepository.save(r3);
        }

        //Reader4 - Catarina
        Optional<ReaderDetails> readerDetails4 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/4");
        if (readerDetails4.isEmpty()) {
            ReaderDetails r4 = new ReaderDetails("4");
            readerRepository.save(r4);
        }

        //Reader5 - Marcelo
        Optional<ReaderDetails> readerDetails5 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/5");
        if (readerDetails5.isEmpty()) {
            ReaderDetails r5 = new ReaderDetails("5");
            readerRepository.save(r5);
        }

        //Reader6 - Luís
        Optional<ReaderDetails> readerDetails6 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/6");
        if (readerDetails6.isEmpty()) {
            ReaderDetails r6 = new ReaderDetails("6");
            readerRepository.save(r6);
        }

        //Reader7 - António
        Optional<ReaderDetails> readerDetails7 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/7");
        if (readerDetails7.isEmpty()) {
            ReaderDetails r7 = new ReaderDetails("7");
            readerRepository.save(r7);
        }

        //Reader8 - André
        Optional<ReaderDetails> readerDetails8 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/8");
        if (readerDetails8.isEmpty()) {
            ReaderDetails r8 = new ReaderDetails("8");
            readerRepository.save(r8);
        }

        // Reader9 - Maria
        Optional<ReaderDetails> readerDetails9 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/9");
        if (readerDetails9.isEmpty()) {
            ReaderDetails r9 = new ReaderDetails("9");
            readerRepository.save(r9);
        }

        // Reader10 - Ana
        Optional<ReaderDetails> readerDetails10 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/10");
        if (readerDetails10.isEmpty()) {
            ReaderDetails r10 = new ReaderDetails("10");
            readerRepository.save(r10);
        }

        // Reade11 - Francisco
        Optional<ReaderDetails> readerDetails11 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/11");
        if (readerDetails11.isEmpty()) {
            ReaderDetails r11 = new ReaderDetails("11");
            readerRepository.save(r11);
        }

        // Reader12 - Ricardo
        Optional<ReaderDetails> readerDetails12 = readerRepository.findByReaderNumber(LocalDate.now().getYear() + "/12");
        if (readerDetails12.isEmpty()) {
            ReaderDetails r12 = new ReaderDetails("12");
            readerRepository.save(r12);
        }
    }
}
