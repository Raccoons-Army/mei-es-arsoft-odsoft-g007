package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Profile("bootstrap")
@PropertySource({"classpath:config/library.properties"})
@Order(2)
public class Bootstrapper implements CommandLineRunner {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private final FactoryGenre factoryGenre;
    private final FactoryAuthor factoryAuthor;

    @Override
    @Transactional
    public void run(final String... args) throws InstantiationException {
        createAuthors();
        createGenres();
        createBooks();
    }

    private void createAuthors() {

        if (authorRepository.findByAuthorNumber("MAP").isEmpty()) {
            final Author author = new Author("MAP", "Manuel Antonio Pina");
            authorRepository.save(author);
        }
        if (authorRepository.findByAuthorNumber("ASE").isEmpty()) {
            final Author author = new Author("ASE", "Antoine de Saint Exupéry");
            authorRepository.save(author);
        }
        if (authorRepository.findByAuthorNumber("AP").isEmpty()) {
            final Author author = new Author("AP", "Alexandre Pereira");
            authorRepository.save(author);
        }
        if (authorRepository.findByAuthorNumber("FP").isEmpty()) {
            final Author author = new Author("FP", "Filipe Portela");
            authorRepository.save(author);
        }
        if (authorRepository.findByAuthorNumber("RQ").isEmpty()) {
            final Author author = new Author("RQ", "Ricardo Queirós");
            authorRepository.save(author);
        }
        if (authorRepository.findByAuthorNumber("FM").isEmpty()) {
            final Author author = new Author("FM", "Freida Mcfadden");
            authorRepository.save(author);
        }
        if (authorRepository.findByAuthorNumber("JRRT").isEmpty()) {
            final Author author = new Author("JRRT", "J R R Tolkien");
            authorRepository.save(author);
        }
        if (authorRepository.findByAuthorNumber("GD").isEmpty()) {
            final Author author = new Author("GD", "Gardner Dozois");
            authorRepository.save(author);
        }
        if (authorRepository.findByAuthorNumber("LT").isEmpty()) {
            final Author author = new Author("LT", "Lisa Tuttle");
            authorRepository.save(author);
        }
        if (authorRepository.findByAuthorNumber("MZ").isEmpty()) {
            final Author author = new Author("MZ", "Markus Zusak");
            authorRepository.save(author);
        }
        if (authorRepository.findByAuthorNumber("JKR").isEmpty()) {
            final Author author = new Author("JKR", "J K Rowling");
            authorRepository.save(author);
        }
        if (authorRepository.findByAuthorNumber("RR").isEmpty()) {
            final Author author = new Author("RR", "Rick Riordan");
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
        Optional<Author> author = authorRepository.findByAuthorNumber("MAP");

        // 1 - O País das Pessoas de Pernas Para o Ar
        if (bookRepository.findByIsbn("9789720706386").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get());
                Book book = new Book("9789720706386",
                        "O País das Pessoas de Pernas Para o Ar ",
                        "Fazendo uso do humor e do nonsense, o livro reúne quatro histórias divertidas e com múltiplos significados: um país onde as pessoas vivem de pernas para o ar, que nos é apresentado por um passarinho chamado Fausto; a vida de um peixinho vermelho que escrevia um livro que a Sara não sabia ler; um Menino Jesus que não queria ser Deus, pois só queria brincar como as outras crianças; um bolo que queria ser comido, mas que não foi, por causa do pecado da gula. "
                        , factoryGenre, factoryAuthor);

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
                authors.add(author.get());
                Book book = new Book("9789723716160",
                        "Como se Desenha Uma Casa",
                        "Como quem, vindo de países distantes fora de / si, chega finalmente aonde sempre esteve / e encontra tudo no seu lugar, / o passado no passado, o presente no presente, / assim chega o viajante à tardia idade / em que se confundem ele e o caminho. [...]",
                        factoryGenre, factoryAuthor);

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
            author = authorRepository.findByAuthorNumber("AP");
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get());
                Book book = new Book("9789895612864",
                        "C e Algoritmos",
                        "O C é uma linguagem de programação incontornável no estudo e aprendizagem das linguagens de programação. É um precursor das linguagens de programação estruturadas e a sua sintaxe foi reutilizada em muitas linguagens posteriores, mesmo de paradigmas diferentes, entre as quais se contam o Java, o Javascript, o Actionscript, o PHP, o Perl, o C# e o C++.\n" +
                                "\n" +
                                "Este livro apresenta a sintaxe da linguagem C tal como especificada pelas normas C89, C99, C11 e C17, da responsabilidade do grupo de trabalho ISO/IEC JTC1/SC22/WG14.",
                        factoryGenre, factoryAuthor);

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
            author = authorRepository.findByAuthorNumber("FP");
            Optional<Author> author2 = authorRepository.findByAuthorNumber("RQ");
            if (genre.isPresent() && !author.isEmpty() && !author2.isEmpty()) {
                authors.add(author.get());
                authors.add(author2.get());
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
                        factoryGenre, factoryAuthor);

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
            author = authorRepository.findByAuthorNumber("ASE");
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get());
                Book book = new Book("9789722328296",
                        "O Principezinho", "Depois de deixar o seu asteroide e embarcar numa viagem pelo espaço, o principezinho chega, finalmente, à Terra. No deserto, o menino de cabelos da cor do ouro conhece um aviador, a quem conta todas as aventuras que viveu e tudo o que viu ao longo da sua jornada.",
                        factoryGenre, factoryAuthor);

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
            author = authorRepository.findByAuthorNumber("FM");
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get());
                Book book = new Book("9789895702756",
                        "A Criada Está a Ver", "A Sra. Lowell transborda simpatia ao acenar-me através da cerca que separa as nossas casas. “Devem ser os nossos novos vizinhos!” Agarro na mão da minha filha e sorrio de volta. No entanto, assim que vê o meu marido, uma expressão estranha atravessa-lhe o rosto. MILLIE, A MEMORÁVEL PROTAGONISTA DOS BESTSELLERS A CRIADA E O SEGREDO DA CRIADA, ESTÁ DE VOLTA!Eu costumava limpar a casa de outras pessoas. Nem posso acreditar que esta casa é realmente minha...",
                        factoryGenre, factoryAuthor);

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
            author = authorRepository.findByAuthorNumber("JRRT");
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get());
                Book book = new Book("9789897776090",
                        "O Hobbit", "\"Esta é a história de como um Baggins viveu uma aventura e deu por si a fazer e a dizer coisas totalmente inesperadas...\n" +
                        "Bilbo Baggins goza de uma vida confortável, calma e pouco ambiciosa. Raramente viaja mais longe do que a despensa ou a adega do seu buraco de hobbit, em Fundo-do-Saco.\n" +
                        "Mas a sua tranquilidade é perturbada quando, um dia, o feiticeiro Gandalf e uma companhia de treze anões aparecem à sua porta, para o levar numa perigosa aventura.\n" +
                        "Eles têm um plano: saquear o tesouro guardado por Smaug, O Magnífico, um dragão enorme e muito perigoso... Bilbo, embora relutante, junta-se a esta missão, desconhecendo que nesta viagem até à Montanha Solitária vai encontrar um anel mágico e uma estranha criatura conhecida como Gollum. Livro com nova tradução e edição.\n" +
                        "Inclui mapas e ilustrações originais do autor. Situado no mundo imaginário da Terra Média,\n" +
                        "O Hobbit, o prelúdio de O Senhor dos Anéis, vendeu milhões de exemplares em todo o mundo desde a sua publicação em 1937, impondo-se como um clássico intemporal e um dos livros mais adorados e influentes do século xx.\" ",
                        factoryGenre, factoryAuthor);

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
            author = authorRepository.findByAuthorNumber("JRRT");
            Optional<Author> author2 = authorRepository.findByAuthorNumber("GD");
            if (genre.isPresent() && !author.isEmpty() && !author2.isEmpty()) {
                authors.add(author.get());
                authors.add(author2.get());
                Book book = new Book("9789896379636",
                        "Histórias de Vigaristas e Canalhas",
                        "Recomendamos cautela ao ler estes contos: há muitos vigaristas e canalhas à solta.\n" +
                                "Se gostou de ler \"Histórias de Aventureiros e Patifes\", então não vai querer perder novas histórias com alguns dos maiores vigaristas e canalhas. São personagens infames que se recusam a agir preto no branco, e escolhem trilhar os seus próprios caminhos, à margem das leis dos homens. Personagens carismáticas, eloquentes, sem escrúpulos, que chegam até nós através de um formidável elenco de autores.\n" +
                                "Com organização de George R. R. Martin, um nome que já dispensa apresentações, e Gardner Dozois, tem nas mãos uma antologia de géneros multifacetados e que reúne algumas das mentes mais perversas da literatura fantástica.",
                        factoryGenre, factoryAuthor);

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
            author = authorRepository.findByAuthorNumber("JRRT");
            Optional<Author> author2 = authorRepository.findByAuthorNumber("GD");
            if (genre.isPresent() && !author.isEmpty() && !author2.isEmpty()) {
                authors.add(author.get());
                authors.add(author2.get());
                Book book = new Book("9789896378905",
                        "Histórias de Aventureiros e Patifes",
                        "Recomendamos cautela a ler estes contos: Há muitos patifes à solta.\n" +
                                "\n" +
                                "Há personagens malandras e sem escrúpulos cujo carisma e presença de espírito nos faz estimá-las mais do que devíamos. São patifes, mercenários e vigaristas com códigos de honra duvidosos mas que fazem de qualquer aventura uma delícia de ler.\n" +
                                "George R. R. Martin é um grande admirador desse tipo de personagens – ou não fosse ele o autor de \"A Guerra dos Tronos\". Nesta monumental antologia, não só participa com um prefácio e um conto introduzindo uma das personagens mais canalhas da história de Westeros, como também a organiza com Gardner Dozois. Se é fã de literatura fantástica, vai deliciar-se!",
                        factoryGenre, factoryAuthor);

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
            author = authorRepository.findByAuthorNumber("J R R Tolkien");
            Optional<Author> author2 = authorRepository.findByAuthorNumber("LT");
            if (genre.isPresent() && !author.isEmpty() && !author2.isEmpty()) {
                authors.add(author.get());
                authors.add(author2.get());
                Book book = new Book("9789896375225",
                        "Windhaven",
                        "Ao descobrirem neste novo planeta a habilidade de voar com asas de metal, os voadores de asas prateadas " +
                                "tornam-se a elite e levam a todo o lado notícias, canções e histórias. Atravessam oceanos, enfrentam as " +
                                "tempestades e são heróis lendários que enfrentam a morte a cada golpe traiçoeiro do vento. Maris de Amberly," +
                                " filha de um pescador, foi criada por um voador e nada mais deseja do que conquistar os céus de Windhaven. " +
                                "A sua ambição é tão forte que a jovem desafia a tradição para se juntar à elite. Mas cedo irá descobrir que" +
                                " nem todos os voadores estão dispostos a aceitá-la e terá de lutar e arriscar a vida pelo seu sonho. " +
                                "Conseguirá Maris vencer ou tornar-se-á uma testemunha do fim de Windhaven?",
                        factoryGenre, factoryAuthor);

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
            author = authorRepository.findByAuthorNumber("MZ");
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get());
                Book book = new Book("9781101934180",
                        "A Menina que Roubava Livros",
                        "Durante a Segunda Guerra Mundial, a jovem Liesel encontra conforto nos livros que rouba e compartilha com os outros. Em meio ao caos da guerra, sua relação com as palavras torna-se sua salvação.",
                        factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }

        // 12 - Harry Potter e a Pedra Filosofal
        if (bookRepository.findByIsbn("9788869183157").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            author = authorRepository.findByAuthorNumber("JKR");
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get());
                Book book = new Book("9788869183157",
                        "Harry Potter e a Pedra Filosofal",
                        "O primeiro livro da série Harry Potter, onde o jovem Harry descobre que é um bruxo e começa sua jornada na escola de magia de Hogwarts, enfrentando desafios e fazendo amizades inesquecíveis.",
                        factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }

        // 13 - Percy Jackson e o Ladrão de Raios
        if (bookRepository.findByIsbn("9788580575392").isEmpty()) {
            List<Author> authors = new ArrayList<>();
            author = authorRepository.findByAuthorNumber("RR");
            if (genre.isPresent() && !author.isEmpty()) {
                authors.add(author.get());
                Book book = new Book("9788580575392",
                        "Percy Jackson e o Ladrão de Raios",
                        "Percy Jackson descobre que é um semideus, filho de Poseidon, e parte em uma jornada épica para recuperar o raio de Zeus, que foi roubado. Ele enfrenta monstros mitológicos e descobre mais sobre seu verdadeiro destino.",
                        factoryGenre, factoryAuthor);

//                defineGenreAndAddAuthors(book, genre.get(), authors);
                book.setGenre(genre.get());
                book.setAuthors(authors);
                bookRepository.save(book);
            }
        }

    }
}


