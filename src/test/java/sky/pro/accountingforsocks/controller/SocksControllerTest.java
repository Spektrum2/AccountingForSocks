package sky.pro.accountingforsocks.controller;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sky.pro.accountingforsocks.dto.SocksRecord;

import sky.pro.accountingforsocks.model.Socks;
import sky.pro.accountingforsocks.repository.RegistrationRepository;
import sky.pro.accountingforsocks.repository.SocksRepository;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SocksControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private SocksRepository socksRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    private final Faker faker = new Faker();

    @AfterEach
    public void afterEach() {
        registrationRepository.deleteAll();
        socksRepository.deleteAll();
    }

    @Test
    public void addIncomeTest() {
        addSocks(generateSocks());
    }

    @Test
    public void addIncomeSocksNotNullTest() {
        SocksRecord socksRecord = addSocks(generateSocks());

        ResponseEntity<String> socksRecordResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/api/socks/income", socksRecord, String.class);
        assertThat(socksRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Socks socks = socksRepository.findByColorAndCottonPart(socksRecord.getColor(), socksRecord.getCottonPart());
        assertThat(socks).isNotNull();
        assertThat(socks.getQuantity()).isEqualTo(socksRecord.getQuantity() * 2);
    }

    @Test
    public void addOutcomeTest() {
        SocksRecord socksRecord = addSocks(generateSocks());

        ResponseEntity<String> socksRecordResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/api/socks/outcome", socksRecord, String.class);
        assertThat(socksRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(socksRecordResponseEntity.getBody()).isEqualTo("Удалось добавить отпуск");
        Socks socks = socksRepository.findByColorAndCottonPart(socksRecord.getColor(), socksRecord.getCottonPart());
        assertThat(socks).isNotNull();
        assertThat(socks.getQuantity()).isEqualTo(0);
    }

    @Test
    public void addOutcomeNegativeTest() {
        SocksRecord socksRecord = addSocks(generateSocks());
        socksRecord.setQuantity(socksRecord.getQuantity() + 1);

        ResponseEntity<String> socksRecordResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/api/socks/outcome", socksRecord, String.class);
        assertThat(socksRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addOutcomeNegative2Test() {
        SocksRecord socksRecord = addSocks(generateSocks());
        socksRecord.setColor("123");

        ResponseEntity<String> socksRecordResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/api/socks/outcome", socksRecord, String.class);
        assertThat(socksRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForGetQuantity")
    public void getQuantityTest(String color, String operation, int cotton, int count) {
        SocksRecord socksRecord1 = new SocksRecord();
        socksRecord1.setColor("red");
        socksRecord1.setCottonPart(45);
        socksRecord1.setQuantity(10);
        addSocks(socksRecord1);
        SocksRecord socksRecord2 = new SocksRecord();
        socksRecord2.setColor("red");
        socksRecord2.setCottonPart(30);
        socksRecord2.setQuantity(5);
        addSocks(socksRecord2);
        SocksRecord socksRecord3 = new SocksRecord();
        socksRecord3.setColor("black");
        socksRecord3.setCottonPart(60);
        socksRecord3.setQuantity(20);
        addSocks(socksRecord3);


        ResponseEntity<String> countSocksResponseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/api/socks?color=" + color + "&operation=" + operation + "&cottonPart=" + cotton, String.class);
        assertThat(countSocksResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(countSocksResponseEntity.getBody()).isEqualTo("Количество носков на складе - " + count);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForgetQuantityNegative")
    public void getQuantityNegativeTest(int cottonPart) {
        SocksRecord socksRecord = addSocks(generateSocks());
        socksRecord.setCottonPart(cottonPart);

        ResponseEntity<String> countSocksResponseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/api/socks?color=" + socksRecord.getColor() + "&operation=equal&cottonPart=" + socksRecord.getCottonPart(), String.class);
        assertThat(countSocksResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getQuantityNegative2Test() {
        SocksRecord socksRecord = addSocks(generateSocks());

        ResponseEntity<String> countSocksResponseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/api/socks?color=" + socksRecord.getColor() + "&operation=error&cottonPart=" + socksRecord.getCottonPart(), String.class);
        assertThat(countSocksResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getQuantityNegative3Test() {
        SocksRecord socksRecord2 = new SocksRecord();
        socksRecord2.setColor("red");
        socksRecord2.setCottonPart(30);
        socksRecord2.setQuantity(5);
        addSocks(socksRecord2);

        ResponseEntity<String> countSocksResponseEntity = testRestTemplate.getForEntity("http://localhost:" + port + "/api/socks?color=black&operation=equal&cottonPart=50", String.class);
        assertThat(countSocksResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private SocksRecord generateSocks() {
        SocksRecord socksRecord = new SocksRecord();
        socksRecord.setColor(faker.color().name());
        socksRecord.setCottonPart(faker.random().nextInt(0, 100));
        socksRecord.setQuantity(faker.random().nextInt(1, 50));
        return socksRecord;
    }

    private SocksRecord addSocks(SocksRecord socksRecord) {
        ResponseEntity<String> socksRecordResponseEntity = testRestTemplate.postForEntity("http://localhost:" + port + "/api/socks/income", socksRecord, String.class);
        assertThat(socksRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(socksRecordResponseEntity.getBody()).isNotNull();
        assertThat(socksRecordResponseEntity.getBody()).isEqualTo("Удалось добавить приход");
        return socksRecord;
    }

    public static Stream<Arguments> provideParamsForGetQuantity() {
        return Stream.of(
                Arguments.of("black", "moreThan", 50, 20),
                Arguments.of("red", "lessThan", 46, 15),
                Arguments.of("red", "equal", 45, 10)
        );
    }

    public static Stream<Arguments> provideParamsForgetQuantityNegative() {
        return Stream.of(
                Arguments.of(-45),
                Arguments.of(200),
                Arguments.of(-100)
        );
    }
}