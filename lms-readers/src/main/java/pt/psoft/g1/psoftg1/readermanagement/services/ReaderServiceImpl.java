package pt.psoft.g1.psoftg1.readermanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.publishers.ReaderEventsPublisher;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.repositories.ForbiddenNameRepository;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Transactional
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepo;
    private final UserRepository userRepo;
    private final GenreRepository genreRepo;
    private final ForbiddenNameRepository forbiddenNameRepository;
    private final PhotoRepository photoRepository;

    private final ReaderEventsPublisher readerEventsPublisher;

    @Override
    public ReaderDetails create(CreateReaderRequest request, String photoURI) {

        String email = request.getUsername();
        String fullName = request.getFullName();
        String birthDate = request.getBirthDate();
        String phoneNumber = request.getPhoneNumber();
        boolean gdprConsent = request.getGdpr();
        boolean marketingConsent = request.getMarketing();
        boolean thirdPartySharingConsent = request.getThirdParty();
        List<String> interestList = request.getInterestList();

        Iterable<String> words = List.of(fullName.split("\\s+"));
        for (String word : words){
            if(!forbiddenNameRepository.findByForbiddenNameIsContained(word).isEmpty()) {
                throw new IllegalArgumentException("Name contains a forbidden word");
            }
        }

        /*if(stringInterestList != null && !stringInterestList.isEmpty()) {
            request.setInterestList(this.getGenreListFromStringList(stringInterestList));
        }*/

        /*
         * Since photos can be null (no photo uploaded) that means the URI can be null as well.
         * To avoid the client sending false data, photoURI has to be set to any value / null
         * according to the MultipartFile photo object
         *
         * That means:
         * - photo = null && photoURI = null -> photo is removed
         * - photo = null && photoURI = validString -> ignored
         * - photo = validFile && photoURI = null -> ignored
         * - photo = validFile && photoURI = validString -> photo is set
         * */

        MultipartFile photo = request.getPhoto();
        if(photo == null && photoURI != null || photo != null && photoURI == null) {
            request.setPhoto(null);
        }

        int count = readerRepo.getCountFromCurrentYear();
        int number = count + 1;

        ReaderDetails savedReader = create(String.valueOf(number), email, birthDate, phoneNumber,
                null, gdprConsent, marketingConsent, thirdPartySharingConsent, interestList);

        if( savedReader != null ) {
            readerEventsPublisher.sendReaderCreated(savedReader);
        }

        return savedReader;
    }

    @Override
    public ReaderDetails create(ReaderViewAMQP readerViewAMQP) {

        String readerNumber = readerViewAMQP.getReaderNumber();
        String email = readerViewAMQP.getUsername();
        String birthDate = readerViewAMQP.getBirthDate();
        String phoneNumber = readerViewAMQP.getPhoneNumber();
        String photo = readerViewAMQP.getPhoto();
        boolean gdprConsent = readerViewAMQP.isGdprConsent();
        boolean marketingConsent = readerViewAMQP.isMarketingConsent();
        boolean thirdPartySharingConsent = readerViewAMQP.isThirdPartySharingConsent();
        List<String> interestList = readerViewAMQP.getInterestList();

        return create(readerNumber, email, birthDate, phoneNumber,
                photo, gdprConsent, marketingConsent, thirdPartySharingConsent, interestList);
    }

    private ReaderDetails create(String readerNumber, String email, String birthDate, String phoneNumber, String photo,
                                 boolean gdprConsent, boolean marketingConsent, boolean thirdPartySharingConsent, List<String> interestList) {
        if (readerRepo.findByUsername(email).isPresent()) {
            throw new ConflictException("Username already exists!");
        }

        ReaderDetails rd = new ReaderDetails(readerNumber, birthDate, phoneNumber,
                gdprConsent, marketingConsent, thirdPartySharingConsent, photo, email, interestList);

        return readerRepo.save(rd);
    }
    @Override
    public ReaderDetails update(final String id, final UpdateReaderRequest request, final long desiredVersion, String photoURI){

        MultipartFile photo = request.getPhoto();
        if(photo == null && photoURI != null || photo != null && photoURI == null) {
            request.setPhoto(null);
        }

        ReaderDetails updatedReader = update(request.getNumber(), desiredVersion, request.getBirthDate(), request.getPhoneNumber(),
                request.getMarketing(), request.getThirdParty(), request.getPhoto().getContentType(), request.getInterestList());

        if( updatedReader != null ) {
            readerEventsPublisher.sendReaderUpdated(updatedReader, desiredVersion);
        }

        return updatedReader;
    }

    @Override
    public ReaderDetails update(ReaderViewAMQP readerViewAMQP){

        return update(readerViewAMQP.getReaderNumber(), readerViewAMQP.getVersion(), readerViewAMQP.getBirthDate(), readerViewAMQP.getPhoneNumber(),
                readerViewAMQP.isMarketingConsent(), readerViewAMQP.isThirdPartySharingConsent(), readerViewAMQP.getPhoto(), readerViewAMQP.getInterestList());
    }

    private ReaderDetails update(String readerNumber, Long currentVersion, String birthDate, String phoneNumber,
                                 boolean marketing, boolean thirdParty, String photoURI, List<String> interestList) {

        final ReaderDetails readerDetails = readerRepo.findByReaderNumber(readerNumber)
                .orElseThrow(() -> new NotFoundException("Cannot find reader"));

        if (interestList != null) {
            for (String genreId : interestList) {
                Optional<Genre> temp = genreRepo.findByString(genreId);
                if (temp.isEmpty()) {
                    throw new NotFoundException("Genre not found");
                }
            }
        }

        readerDetails.applyPatch(currentVersion, birthDate, phoneNumber, marketing, thirdParty, photoURI, interestList);

        return readerRepo.save(readerDetails);
    }

    @Override
    public void delete(ReaderViewAMQP readerViewAMQP) {
        final ReaderDetails readerDetails = readerRepo.findByReaderNumber(readerViewAMQP.getReaderNumber())
                .orElseThrow(() -> new NotFoundException("Cannot find reader"));

        readerRepo.delete(readerDetails);
    }


    @Override
    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {
        return this.readerRepo.findByReaderNumber(readerNumber);
    }

    @Override
    public List<ReaderDetails> findByPhoneNumber(String phoneNumber) {
        return this.readerRepo.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<ReaderDetails> findByUsername(final String username) {
        return this.readerRepo.findByUsername(username);
    }

    @Override
    public Iterable<ReaderDetails> findAll() {
        return this.readerRepo.findAll();
    }

    private List<Genre> getGenreListFromStringList(List<String> interestList) {
        if(interestList == null) {
            return null;
        }

        if(interestList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Genre> genreList = new ArrayList<>();
        for(String interest : interestList) {
            Optional<Genre> optGenre = genreRepo.findByString(interest);
            if(optGenre.isEmpty()) {
                throw new NotFoundException("Could not find genre with name " + interest);
            }

            genreList.add(optGenre.get());
        }

        return genreList;
    }

    @Override
    public Optional<ReaderDetails> removeReaderPhoto(String readerNumber, long desiredVersion) {
        ReaderDetails readerDetails = readerRepo.findByReaderNumber(readerNumber)
                .orElseThrow(() -> new NotFoundException("Cannot find reader"));

        String photoFile = readerDetails.getPhoto().getPhotoFile();
        readerDetails.removePhoto(desiredVersion);
        Optional<ReaderDetails> updatedReader = Optional.of(readerRepo.save(readerDetails));
        photoRepository.deleteByPhotoFile(photoFile);
        return updatedReader;
    }

    @Override
    public List<ReaderDetails> searchReaders(pt.psoft.g1.psoftg1.shared.services.Page page, SearchReadersQuery query) {
        if (page == null)
            page = new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);

        if (query == null)
            query = new SearchReadersQuery("", "","");

        final var list = readerRepo.searchReaderDetails(page, query);

        if(list.isEmpty())
            throw new NotFoundException("No results match the search query");

        return list;
    }
}
