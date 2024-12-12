package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.FactoryAuthor;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.idGenerationStrategy.IdGenerationStrategy;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
import pt.psoft.g1.psoftg1.shared.services.ForbiddenNameService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Profile("bootstrap")
@PropertySource({"classpath:config/library.properties"})
@Order(2)
public class Bootstrapper implements CommandLineRunner {
    @Value("${lendingDurationInDays}")
    private int lendingDurationInDays;
    @Value("${fineValuePerDayInCents}")
    private int fineValuePerDayInCents;

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final LendingRepository lendingRepository;
    private final ReaderRepository readerRepository;
    private final PhotoRepository photoRepository;
    private final ForbiddenNameService forbiddenNameService;
    private final IdGenerationStrategy<String> idGenerationStrategy;

    private final FactoryGenre factoryGenre;
    private final FactoryAuthor factoryAuthor;

    @Override
    @Transactional
    public void run(final String... args) throws InstantiationException {
        createAuthors();
        createGenres();
        createBooks();
        loadForbiddenNames();
        createLendings();
        createPhotos();
    }

    private void createAuthors() {

        if (authorRepository.searchByNameName("Manuel Antonio Pina").isEmpty()) {
            final Author author = new Author(idGenerationStrategy.generateId(), "Manuel Antonio Pina",
                    "Manuel António Pina foi um jornalista e escritor português, premiado em 2011 com o Prémio Camões",
                    "authorPhotoTest.jpg");
            authorRepository.save(author);
        }
        if (authorRepository.searchByNameName("Antoine de Saint Exupéry").isEmpty()) {
            final Author author = new Author(idGenerationStrategy.generateId(), "Antoine de Saint Exupéry",
                    "Antoine de Saint-Exupéry nasceu a 29 de junho de 1900 em Lyon. Faz o seu batismo de voo aos 12 anos, aos 22 torna-se piloto militar e é como capitão que em 1939 se junta à Força Aérea francesa em luta contra a ocupação nazi. A aviação e a guerra viriam a revelar-se elementos centrais de toda a sua obra literária, onde se destacam títulos como Correio do Sul (1929), o seu primeiro romance, Voo Noturno (1931), que logo se tornou um êxito de vendas internacional, e Piloto de Guerra (1942), retrato da sua participação na Segunda Guerra Mundial. Em 1943 publicaria aquela que é reconhecida como a sua obra-prima, O Principezinho, um dos livros mais traduzidos em todo o mundo. A sua morte, aos 44 anos, num acidente de aviação durante uma missão de reconhecimento no sul de França, permanece ainda hoje um mistério.",
                    null);
            authorRepository.save(author);
        }
        if (authorRepository.searchByNameName("Alexandre Pereira").isEmpty()) {
            final Author author = new Author(idGenerationStrategy.generateId(), "Alexandre Pereira",
                    "Alexandre Pereira é licenciado e mestre em Engenharia Electrotécnica e de Computadores, pelo Instituto Superior Técnico. É, também, licenciado em Antropologia, pela Faculdade de Ciências Sociais e Humanas da Universidade Nova de Lisboa.\n" +
                            "É Professor Auxiliar Convidado na Universidade Lusófona de Humanidades e Tecnologias, desde Março de 1993, onde lecciona diversas disciplinas na Licenciatura de Informática e lecciona uma cadeira de introdução ao SPSS na Licenciatura de Psicologia.\n" +
                            "Tem também leccionado cursos de formação na área da aplicação da informática ao cálculo estatístico e processamento de dados utilizando o SPSS, em diversas instituições, nomeadamente no Instituto Nacional de Estatística.\n" +
                            "Para além disso, desenvolve aplicações informáticas na área da Psicologia Cognitiva, no âmbito de projectos de investigação do departamento de Psicologia Cognitiva da Faculdade de Psicologia da Universidade de Lisboa.\n" +
                            "Está ainda ligado a projectos de ensino à distância desenvolvidos na Faculdade de Motricidade Humana da Universidade Técnica de Lisboa.\n" +
                            "Paralelamente, tem desenvolvido aplicações de software comercial, área onde continua em actividade. ",
                    null);
            authorRepository.save(author);
        }
        if (authorRepository.searchByNameName("Filipe Portela").isEmpty()) {
            final Author author = new Author(idGenerationStrategy.generateId(), "Filipe Portela",
                    " «Docente convidado na Escola de Engenharia da Universidade do Minho. Investigador integrado do Centro Algoritmi. CEO e fundador da startup tecnológica IOTech - Innovation on Technology. Coautor do livro Introdução ao Desenvolvimento Moderno para a Web. ",
                    null);
            authorRepository.save(author);
        }
        if (authorRepository.searchByNameName("Ricardo Queirós").isEmpty()) {
            final Author author = new Author(idGenerationStrategy.generateId(), "Ricardo Queirós",
                    "Docente na Escola Superior de Media Artes e Design do Politécnico do Porto. Diretor da uniMAD (ESMAD) e membro efetivo do CRACS (INESC TEC). Autor de vários livros sobre tecnologias Web e programação móvel, publicados pela FCA. Coautor do livro Introdução ao Desenvolvimento Moderno para a Web.",
                    null);
            authorRepository.save(author);
        }
        if (authorRepository.searchByNameName("Freida Mcfadden").isEmpty()) {
            final Author author = new Author(idGenerationStrategy.generateId(), "Freida Mcfadden",
                    "Freida McFadden é médica e especialista em lesões cerebrais. Autora de diversos thrillers psicológicos, todos eles bestsellers, já traduzidos para mais de 30 idiomas. As suas obras foram selecionadas para «O Melhor Livro do Ano» na Amazon e também para «Melhor Thriller» dos Goodreads Choice Awards.\n" +
                            "Freida vive com a sua família e o gato preto numa casa de três andares com vista para o oceano, com escadas que rangem e gemem a cada passo, e ninguém conseguiria ouvi-la se gritasse. A menos que gritasse muito alto, talvez.",
                    null);
            authorRepository.save(author);
        }
        if (authorRepository.searchByNameName("J R R Tolkien").isEmpty()) {
            final Author author = new Author(idGenerationStrategy.generateId(), "J R R Tolkien",
                    "J.R.R. Tolkien nasceu a 3 de Janeiro de 1892, em Bloemfontein.\n" +
                            "Depois de ter combatido na Primeira Guerra Mundial, dedicou-se a uma ilustre carreira académica e foi reconhecido como um dos grandes filólogos do planeta.\n" +
                            "Foi a criação da Terra Média, porém, a trazer-lhe a celebridade. Autor de extraordinários clássicos da ficção, de que são exemplo O Hobbit, O Senhor dos Anéis e O Silmarillion, os seus livros foram traduzidos em mais de 60 línguas e venderam largos milhões de exemplares no mundo inteiro.\n" +
                            "Tolkien foi nomeado Comandante da Ordem do Império Britânico e, em 1972, foi-lhe atribuído o título de Doutor Honoris Causa, pela Universidade de Oxford.\n" +
                            "Morreu em 1973, com 81 anos.",
                    null);
            authorRepository.save(author);
        }
        if (authorRepository.searchByNameName("Gardner Dozois").isEmpty()) {
            final Author author = new Author(idGenerationStrategy.generateId(), "Gardner Dozois",
                    "Gardner Raymond Dozois (23 de julho de 1947 – 27 de maio de 2018) foi um autor de ficção científica norte-americano.\n" +
                            "Foi o fundador e editor do Melhores Do Ano de Ficção científica antologias (1984–2018) e foi editor da revista Asimov Ficção científica (1984-2004), ganhando vários prémios.",
                    "authorPhotoTest.jpg");
            authorRepository.save(author);
        }
        if (authorRepository.searchByNameName("Lisa Tuttle").isEmpty()) {
            final Author author = new Author(idGenerationStrategy.generateId(), "Lisa Tuttle",
                    "Lisa Gracia Tuttle (nascida a 16 de setembro de 1952) é uma autora americana de ficção científica, fantasia e terror. Publicou mais de uma dúzia de romances, sete coleções de contos e vários títulos de não-ficção, incluindo um livro de referência sobre feminismo, \"Enciclopédia do Feminismo\" (1986). Também editou várias antologias e fez críticas de livros para diversas publicações. Vive no Reino Unido desde 1981.\n" +
                            "Tuttle ganhou o Prémio John W. Campbell para Melhor Novo Escritor em 1974, recebeu o Prémio Nebula de Melhor Conto em 1982 por \"The Bone Flute\", que recusou, e o Prémio BSFA de Ficção Curta em 1989 por \"In Translation\".",
                    null);
            authorRepository.save(author);
        }
        if (authorRepository.searchByNameName("Markus Zusak").isEmpty()) {
            final Author author = new Author(idGenerationStrategy.generateId(), "Markus Zusak",
                    "Markus Zusak é um escritor australiano, conhecido por suas obras juvenis e adultas. Seu livro mais famoso, 'A Menina que Roubava Livros', foi traduzido para mais de 40 idiomas e adaptado para o cinema.",
                    null);
            authorRepository.save(author);
        }
        if (authorRepository.searchByNameName("J K Rowling").isEmpty()) {
            final Author author = new Author(idGenerationStrategy.generateId(), "J K Rowling",
                    "J K Rowling é uma autora britânica mundialmente famosa pela série 'Harry Potter'. A série de livros a consagrou como uma das escritoras mais vendidas da história, com mais de 500 milhões de cópias vendidas.",
                    null);
            authorRepository.save(author);
        }
        if (authorRepository.searchByNameName("Rick Riordan").isEmpty()) {
            final Author author = new Author(idGenerationStrategy.generateId(), "Rick Riordan",
                    "Rick Riordan é um autor americano, conhecido por suas séries de fantasia mitológica. Ele escreveu a famosa série 'Percy Jackson e os Olimpianos', que mistura mitologia grega e aventuras contemporâneas.",
                    "authorPhotoTest.jpg");
            authorRepository.save(author);
        }
    }

    private void createGenres() {
        if (genreRepository.findByString("Fantasia").isEmpty()) {
            final Genre g1 = new Genre("Fantasia");
            genreRepository.save(g1);
        }
        if (genreRepository.findByString("Informação").isEmpty()) {
            final Genre g2 = new Genre("Informação");
            genreRepository.save(g2);
        }
        if (genreRepository.findByString("Romance").isEmpty()) {
            final Genre g3 = new Genre("Romance");
            genreRepository.save(g3);
        }
        if (genreRepository.findByString("Infantil").isEmpty()) {
            final Genre g4 = new Genre("Infantil");
            genreRepository.save(g4);
        }
        if (genreRepository.findByString("Thriller").isEmpty()) {
            final Genre g5 = new Genre("Thriller");
            genreRepository.save(g5);
        }
        if (genreRepository.findByString("Juvenil").isEmpty()) {
            final Genre g6 = new Genre("Juvenil");
            genreRepository.save(g6);
        }
    }

    protected void createBooks() {
        Optional<Genre> genre = Optional.ofNullable(genreRepository.findByString("Infantil"))
                .orElseThrow(() -> new NotFoundException("Cannot find genre"));
        List<Author> author = authorRepository.searchByNameName("Manuel Antonio Pina");

        // 1 - O País das Pessoas de Pernas Para o Ar
        if (bookRepository.findByIsbn("9789720706386").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get(0));
                Book book = new Book("9789720706386",
                        "O País das Pessoas de Pernas Para o Ar ",
                        "Fazendo uso do humor e do nonsense, o livro reúne quatro histórias divertidas e com múltiplos significados: um país onde as pessoas vivem de pernas para o ar, que nos é apresentado por um passarinho chamado Fausto; a vida de um peixinho vermelho que escrevia um livro que a Sara não sabia ler; um Menino Jesus que não queria ser Deus, pois só queria brincar como as outras crianças; um bolo que queria ser comido, mas que não foi, por causa do pecado da gula. ",
                        "bookPhotoTest.jpg", factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }

        // 2 - Como se Desenha Uma Casa
        if (bookRepository.findByIsbn("9789723716160").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get(0));
                Book book = new Book("9789723716160",
                        "Como se Desenha Uma Casa",
                        "Como quem, vindo de países distantes fora de / si, chega finalmente aonde sempre esteve / e encontra tudo no seu lugar, / o passado no passado, o presente no presente, / assim chega o viajante à tardia idade / em que se confundem ele e o caminho. [...]",
                        null, factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }

        // 3 - C e Algoritmos
        if (bookRepository.findByIsbn("9789895612864").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            genre = Optional.ofNullable(genreRepository.findByString("Informação"))
                    .orElseThrow(() -> new NotFoundException("Cannot find genre"));
            author = authorRepository.searchByNameName("Alexandre Pereira");
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get(0));
                Book book = new Book("9789895612864",
                        "C e Algoritmos",
                        "O C é uma linguagem de programação incontornável no estudo e aprendizagem das linguagens de programação. É um precursor das linguagens de programação estruturadas e a sua sintaxe foi reutilizada em muitas linguagens posteriores, mesmo de paradigmas diferentes, entre as quais se contam o Java, o Javascript, o Actionscript, o PHP, o Perl, o C# e o C++.\n" +
                                "\n" +
                                "Este livro apresenta a sintaxe da linguagem C tal como especificada pelas normas C89, C99, C11 e C17, da responsabilidade do grupo de trabalho ISO/IEC JTC1/SC22/WG14.",
                        "bookPhotoTest.jpg", factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }


        // 4 - Introdução ao Desenvolvimento Moderno para a Web
        if (bookRepository.findByIsbn("9782722203402").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            genre = Optional.ofNullable(genreRepository.findByString("Informação"))
                    .orElseThrow(() -> new NotFoundException("Cannot find genre"));
            author = authorRepository.searchByNameName("Filipe Portela");
            List<Author> author2 = authorRepository.searchByNameName("Ricardo Queirós");
            if (genre.isPresent() && !author.isEmpty() && !author2.isEmpty()) {
                authors.add(author.get(0));
                authors.add(author2.get(0));
                Book book = new Book("9782722203402",
                        "Introdução ao Desenvolvimento Moderno para a Web",
                        "Este livro foca o desenvolvimento moderno de aplicações Web, sendo apresentados os princípios básicos associados à programação para a Web, divididos em duas partes: front-end e back-end. Na parte do front-end, são introduzidos os conceitos de estruturação, estilização e interação, através das suas principais linguagens HTML, CSS e JavaScript. Na parte do back-end, é feita uma introdução aos servidores Web e respetivas linguagem (Node.js) e framework (Express), às bases de dados (SQL) e aos serviços na Web (REST). De forma a consolidar todos os conceitos teóricos apresentados, é descrita a implementação de um projeto prático completo.\n" +
                                "\n" +
                                "Com capítulos que podem ser lidos sequencialmente ou de forma alternada, o livro é dirigido a todos aqueles que com conhecimentos básicos de programação pretendem (re)entrar no mundo da Web e a quem pretenda colocar-se rapidamente a par de todas as novidades introduzidas nos últimos anos.\n" +
                                "\n" +
                                "O ambiente de desenvolvimento onde todos os exemplos da obra foram escritos é o Visual Studio Code e o controlo de versões foi feito no GitHub. Para colocar o servidor a correr, foi utilizada a plataforma Heroku.\n" +
                                "\n" +
                                "Principais temas abordados:\n" +
                                "· Estruturação de conteúdos na Web com o HTML;\n" +
                                "· Estilização de conteúdos através de CSS e do Bootstrap;\n" +
                                "· Programação Web com o JavaScript;\n" +
                                "· Programação do lado do servidor com o Node.js;\n" +
                                "· Construção de API com o Express e o paradigma REST;\n" +
                                "· Armazenamento de dados com o MySQL;\n" +
                                "· Segurança e proteção dos dados na Web.\n" +
                                "\n" +
                                "O que pode encontrar neste livro:\n" +
                                "· 14 Tecnologias Web;\n" +
                                "· Capítulos organizados para uma leitura sequencial ou alternada;\n" +
                                "· Um projeto Web completo explicado passo a passo;\n" +
                                "· Secção de boas práticas no final de cada capítulo;\n" +
                                "· Resumo dos principais conceitos;\n" +
                                "· Linguagem simples e acessível. ",
                        null, factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }

        // 5 - O Principezinho
        if (bookRepository.findByIsbn("9789722328296").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            genre = Optional.ofNullable(genreRepository.findByString("Infantil"))
                    .orElseThrow(() -> new NotFoundException("Cannot find genre"));
            author = authorRepository.searchByNameName("Antoine de Saint Exupéry");
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get(0));
                Book book = new Book("9789722328296",
                        "O Principezinho", "Depois de deixar o seu asteroide e embarcar numa viagem pelo espaço, o principezinho chega, finalmente, à Terra. No deserto, o menino de cabelos da cor do ouro conhece um aviador, a quem conta todas as aventuras que viveu e tudo o que viu ao longo da sua jornada.",
                        null, factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }

        // 6 - A Criada Está a Ver
        if (bookRepository.findByIsbn("9789895702756").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            genre = Optional.ofNullable(genreRepository.findByString("Thriller"))
                    .orElseThrow(() -> new NotFoundException("Cannot find genre"));
            author = authorRepository.searchByNameName("Freida Mcfadden");
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get(0));
                Book book = new Book("9789895702756",
                        "A Criada Está a Ver", "A Sra. Lowell transborda simpatia ao acenar-me através da cerca que separa as nossas casas. “Devem ser os nossos novos vizinhos!” Agarro na mão da minha filha e sorrio de volta. No entanto, assim que vê o meu marido, uma expressão estranha atravessa-lhe o rosto. MILLIE, A MEMORÁVEL PROTAGONISTA DOS BESTSELLERS A CRIADA E O SEGREDO DA CRIADA, ESTÁ DE VOLTA!Eu costumava limpar a casa de outras pessoas. Nem posso acreditar que esta casa é realmente minha...",
                        "bookPhotoTest.jpg", factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }

        // 7 - O Hobbit
        if (bookRepository.findByIsbn("9789897776090").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            genre = Optional.ofNullable(genreRepository.findByString("Fantasia"))
                    .orElseThrow(() -> new NotFoundException("Cannot find genre"));
            author = authorRepository.searchByNameName("J R R Tolkien");
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get(0));
                Book book = new Book("9789897776090",
                        "O Hobbit", "\"Esta é a história de como um Baggins viveu uma aventura e deu por si a fazer e a dizer coisas totalmente inesperadas...\n" +
                        "Bilbo Baggins goza de uma vida confortável, calma e pouco ambiciosa. Raramente viaja mais longe do que a despensa ou a adega do seu buraco de hobbit, em Fundo-do-Saco.\n" +
                        "Mas a sua tranquilidade é perturbada quando, um dia, o feiticeiro Gandalf e uma companhia de treze anões aparecem à sua porta, para o levar numa perigosa aventura.\n" +
                        "Eles têm um plano: saquear o tesouro guardado por Smaug, O Magnífico, um dragão enorme e muito perigoso... Bilbo, embora relutante, junta-se a esta missão, desconhecendo que nesta viagem até à Montanha Solitária vai encontrar um anel mágico e uma estranha criatura conhecida como Gollum. Livro com nova tradução e edição.\n" +
                        "Inclui mapas e ilustrações originais do autor. Situado no mundo imaginário da Terra Média,\n" +
                        "O Hobbit, o prelúdio de O Senhor dos Anéis, vendeu milhões de exemplares em todo o mundo desde a sua publicação em 1937, impondo-se como um clássico intemporal e um dos livros mais adorados e influentes do século xx.\" ",
                        "bookPhotoTest.jpg", factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }

        // 8 - Histórias de Vigaristas e Canalhas
        if (bookRepository.findByIsbn("9789896379636").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            genre = Optional.ofNullable(genreRepository.findByString("Fantasia"))
                    .orElseThrow(() -> new NotFoundException("Cannot find genre"));
            author = authorRepository.searchByNameName("J R R Tolkien");
            List<Author> author2 = authorRepository.searchByNameName("Gardner Dozois");
            if (genre.isPresent() && !author.isEmpty() && !author2.isEmpty()) {
                authors.add(author.get(0));
                authors.add(author2.get(0));
                Book book = new Book("9789896379636",
                        "Histórias de Vigaristas e Canalhas",
                        "Recomendamos cautela ao ler estes contos: há muitos vigaristas e canalhas à solta.\n" +
                                "Se gostou de ler \"Histórias de Aventureiros e Patifes\", então não vai querer perder novas histórias com alguns dos maiores vigaristas e canalhas. São personagens infames que se recusam a agir preto no branco, e escolhem trilhar os seus próprios caminhos, à margem das leis dos homens. Personagens carismáticas, eloquentes, sem escrúpulos, que chegam até nós através de um formidável elenco de autores.\n" +
                                "Com organização de George R. R. Martin, um nome que já dispensa apresentações, e Gardner Dozois, tem nas mãos uma antologia de géneros multifacetados e que reúne algumas das mentes mais perversas da literatura fantástica.",
                        null, factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }

        // 9 - Histórias de Aventureiros e Patifes
        if (bookRepository.findByIsbn("9789896378905").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            genre = Optional.ofNullable(genreRepository.findByString("Fantasia"))
                    .orElseThrow(() -> new NotFoundException("Cannot find genre"));
            author = authorRepository.searchByNameName("J R R Tolkien");
            List<Author> author2 = authorRepository.searchByNameName("Gardner Dozois");
            if (genre.isPresent() && !author.isEmpty() && !author2.isEmpty()) {
                authors.add(author.get(0));
                authors.add(author2.get(0));
                Book book = new Book("9789896378905",
                        "Histórias de Aventureiros e Patifes",
                        "Recomendamos cautela a ler estes contos: Há muitos patifes à solta.\n" +
                                "\n" +
                                "Há personagens malandras e sem escrúpulos cujo carisma e presença de espírito nos faz estimá-las mais do que devíamos. São patifes, mercenários e vigaristas com códigos de honra duvidosos mas que fazem de qualquer aventura uma delícia de ler.\n" +
                                "George R. R. Martin é um grande admirador desse tipo de personagens – ou não fosse ele o autor de \"A Guerra dos Tronos\". Nesta monumental antologia, não só participa com um prefácio e um conto introduzindo uma das personagens mais canalhas da história de Westeros, como também a organiza com Gardner Dozois. Se é fã de literatura fantástica, vai deliciar-se!",
                        null, factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }
        // 10 - Windhaven
        if (bookRepository.findByIsbn("9789896375225").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            genre = Optional.ofNullable(genreRepository.findByString("Fantasia"))
                    .orElseThrow(() -> new NotFoundException("Cannot find genre"));
            author = authorRepository.searchByNameName("J R R Tolkien");
            List<Author> author2 = authorRepository.searchByNameName("Lisa Tuttle");
            if (genre.isPresent() && !author.isEmpty() && !author2.isEmpty()) {
                authors.add(author.get(0));
                authors.add(author2.get(0));
                Book book = new Book("9789896375225",
                        "Windhaven",
                        "Ao descobrirem neste novo planeta a habilidade de voar com asas de metal, os voadores de asas prateadas " +
                                "tornam-se a elite e levam a todo o lado notícias, canções e histórias. Atravessam oceanos, enfrentam as " +
                                "tempestades e são heróis lendários que enfrentam a morte a cada golpe traiçoeiro do vento. Maris de Amberly," +
                                " filha de um pescador, foi criada por um voador e nada mais deseja do que conquistar os céus de Windhaven. " +
                                "A sua ambição é tão forte que a jovem desafia a tradição para se juntar à elite. Mas cedo irá descobrir que" +
                                " nem todos os voadores estão dispostos a aceitá-la e terá de lutar e arriscar a vida pelo seu sonho. " +
                                "Conseguirá Maris vencer ou tornar-se-á uma testemunha do fim de Windhaven?",
                        null, factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }
        // 11 - A Menina que Roubava Livros
        if (bookRepository.findByIsbn("9781101934180").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            genre = Optional.ofNullable(genreRepository.findByString("Juvenil"))
                    .orElseThrow(() -> new NotFoundException("Cannot find genre"));
            author = authorRepository.searchByNameName("Markus Zusak");
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get(0));
                Book book = new Book("9781101934180",
                        "A Menina que Roubava Livros",
                        "Durante a Segunda Guerra Mundial, a jovem Liesel encontra conforto nos livros que rouba e compartilha com os outros. Em meio ao caos da guerra, sua relação com as palavras torna-se sua salvação.",
                        null, factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }

        // 12 - Harry Potter e a Pedra Filosofal
        if (bookRepository.findByIsbn("9788869183157").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            author = authorRepository.searchByNameName("J K Rowling");
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get(0));
                Book book = new Book("9788869183157",
                        "Harry Potter e a Pedra Filosofal",
                        "O primeiro livro da série Harry Potter, onde o jovem Harry descobre que é um bruxo e começa sua jornada na escola de magia de Hogwarts, enfrentando desafios e fazendo amizades inesquecíveis.",
                        "bookPhotoTest.jpg", factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }

        // 13 - Percy Jackson e o Ladrão de Raios
        if (bookRepository.findByIsbn("9788580575392").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            author = authorRepository.searchByNameName("Rick Riordan");
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get(0));
                Book book = new Book("9788580575392",
                        "Percy Jackson e o Ladrão de Raios",
                        "Percy Jackson descobre que é um semideus, filho de Poseidon, e parte em uma jornada épica para recuperar o raio de Zeus, que foi roubado. Ele enfrenta monstros mitológicos e descobre mais sobre seu verdadeiro destino.",
                        null, factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }

    }

    protected void loadForbiddenNames() {
        String fileName = "forbiddenNames.txt";
        forbiddenNameService.loadDataFromFile(fileName);
    }

    private void createLendings() {
        int i;
        int seq = 0;
        final var book1 = bookRepository.findByIsbn("9789720706386");
        final var book2 = bookRepository.findByIsbn("9789723716160");
        final var book3 = bookRepository.findByIsbn("9789895612864");
        final var book4 = bookRepository.findByIsbn("9782722203402");
        final var book5 = bookRepository.findByIsbn("9789722328296");
        final var book6 = bookRepository.findByIsbn("9789895702756");
        final var book7 = bookRepository.findByIsbn("9789897776090");
        final var book8 = bookRepository.findByIsbn("9789896379636");
        final var book9 = bookRepository.findByIsbn("9789896378905");
        final var book10 = bookRepository.findByIsbn("9789896375225");
        final var book11 = bookRepository.findByIsbn("9781101934180");
        final var book12 = bookRepository.findByIsbn("9788869183157");
        final var book13 = bookRepository.findByIsbn("9788580575392");
        List<Book> books = new ArrayList<>();
        if (book1.isPresent() && book2.isPresent()
                && book3.isPresent() && book4.isPresent()
                && book5.isPresent() && book6.isPresent()
                && book7.isPresent() && book8.isPresent()
                && book9.isPresent() && book10.isPresent()
                && book11.isPresent() && book12.isPresent()
                && book13.isPresent()) {
            books = List.of(new Book[]{book1.get(), book2.get(), book3.get(),
                    book4.get(), book5.get(), book6.get(), book7.get(),
                    book8.get(), book9.get(), book10.get(), book11.get(),
                    book12.get(), book13.get()});
        }

        final var readerDetails1 = readerRepository.findByReaderNumber("2024/1");
        final var readerDetails2 = readerRepository.findByReaderNumber("2024/2");
        final var readerDetails3 = readerRepository.findByReaderNumber("2024/3");
        final var readerDetails4 = readerRepository.findByReaderNumber("2024/4");
        final var readerDetails5 = readerRepository.findByReaderNumber("2024/5");
        final var readerDetails6 = readerRepository.findByReaderNumber("2024/6");
        final var readerDetails7 = readerRepository.findByReaderNumber("2024/7");
        final var readerDetails8 = readerRepository.findByReaderNumber("2024/8");
        final var readerDetails9 = readerRepository.findByReaderNumber("2024/9");

        List<ReaderDetails> readers = new ArrayList<>();
        if (readerDetails1.isPresent() && readerDetails2.isPresent() && readerDetails3.isPresent()
                && readerDetails4.isPresent() && readerDetails5.isPresent() && readerDetails6.isPresent()
                && readerDetails7.isPresent() && readerDetails8.isPresent() && readerDetails9.isPresent()) {
            readers = List.of(new ReaderDetails[]{readerDetails1.get(), readerDetails2.get(), readerDetails3.get(),
                    readerDetails4.get(), readerDetails5.get(), readerDetails6.get(),
                    readerDetails7.get(), readerDetails8.get(), readerDetails9.get()});
        }

        LocalDate startDate;
        LocalDate returnedDate;
        Lending lending;

        //Lendings 1 through 3 (late, returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 1, 31 - i);
                returnedDate = LocalDate.of(2024, 2, 15 + i);
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(i), readers.get(i * 2), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 4 through 6 (overdue, not returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 3, 25 + i);
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(1 + i), readers.get(1 + i * 2), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }
        //Lendings 7 through 9 (late, overdue, not returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 4, (1 + 2 * i));
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(3 / (i + 1)), readers.get(i * 2), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 10 through 12 (returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 1));
                returnedDate = LocalDate.of(2024, 5, (i + 2));
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(3 - i), readers.get(1 + i * 2), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 13 through 18 (returned)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 2));
                returnedDate = LocalDate.of(2024, 5, (i + 2 * 2));
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(i), readers.get(i), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 19 through 23 (returned)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 8));
                returnedDate = LocalDate.of(2024, 5, (2 * i + 8));
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(i), readers.get(1 + i % 4), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 24 through 29 (returned)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 18));
                returnedDate = LocalDate.of(2024, 5, (2 * i + 18));
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(i), readers.get(i % 2 + 2), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 30 through 35 (not returned, not overdue)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 6, (i / 3 + 1));
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(i), readers.get(i % 2 + 3), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 36 through 45 (not returned, not overdue)
        for (i = 0; i < 10; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 6, (2 + i / 4));
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(i), readers.get(4 - i % 4), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        // Create lendings for book11, book12, and book13
        List<Book> newBooks = new ArrayList<>();
        List<ReaderDetails> newReaders = new ArrayList<>();
        if (book11.isPresent() && book12.isPresent() && book13.isPresent()) {
            newBooks = List.of(book11.get(), book12.get(), book13.get());
        }
        if (readerDetails7.isPresent() && readerDetails8.isPresent() && readerDetails6.isPresent()) {
            newReaders = List.of(readerDetails7.get(), readerDetails8.get(), readerDetails6.get());
        }

        for (i = 0; i < 3; i++) {
            seq++;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 7, i + 1); // Example: start dates in July
                returnedDate = LocalDate.of(2024, 7, i + 10); // Example: returned 10 days later
                lending = Lending.newBootstrappingLending(
                        idGenerationStrategy.generateId(),
                        newBooks.get(i),
                        newReaders.get(i),
                        2024,
                        seq,
                        startDate,
                        returnedDate,
                        lendingDurationInDays,
                        fineValuePerDayInCents
                );
                lendingRepository.save(lending);
            }
        }
    }

    private void createPhotos() {
        /*Optional<Photo> photoJoao = photoRepository.findByPhotoFile("foto-joao.jpg");
        if(photoJoao.isEmpty()) {
            Photo photo = new Photo(Paths.get(""))
        }*/
    }

    private void defineGenreAndAddAuthors(Book book, Genre genre, List<Author> authors) {
        try {
            book.defineGenre(genre.getPk(), genre.getGenre());

            for (Author bAuthor : authors) {
                book.addAuthor(bAuthor.getAuthorNumber(), bAuthor.getName(), bAuthor.getBio(),
                        bAuthor.getPhoto() != null ? bAuthor.getPhoto().getPhotoFile() : null, bAuthor.getVersion());
            }
        } catch (InstantiationException e) {
            System.err.println("Failed to instantiate: " + e.getMessage());
        }
    }
}


