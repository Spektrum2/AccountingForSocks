package sky.pro.accountingforsocks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sky.pro.accountingforsocks.component.DtoMapper;
import sky.pro.accountingforsocks.dto.RegistrationRecord;
import sky.pro.accountingforsocks.dto.SocksRecord;
import sky.pro.accountingforsocks.exception.*;
import sky.pro.accountingforsocks.model.Registration;
import sky.pro.accountingforsocks.model.Socks;
import sky.pro.accountingforsocks.model.Status;
import sky.pro.accountingforsocks.repository.RegistrationRepository;
import sky.pro.accountingforsocks.repository.SocksRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocksService {
    private final Logger logger = LoggerFactory.getLogger(SocksService.class);
    private final SocksRepository socksRepository;
    private final RegistrationRepository registrationRepository;
    private final DtoMapper dtoMapper;

    public SocksService(SocksRepository socksRepository,
                        RegistrationRepository registrationRepository,
                        DtoMapper dtoMapper) {
        this.socksRepository = socksRepository;
        this.registrationRepository = registrationRepository;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Метод для регистрации прихода носков на склад(Создается запись в таблице для регистрации и в таблице носки. Если запись в таблице носки есть, то к ней прибовляется количество носков)
     *
     * @param socksRecord тело для регистрации приход носков на склад
     * @return возвращает строку
     */
    public String addIncome(SocksRecord socksRecord) {
        logger.info("Was invoked method addIncome");
        Socks socks = socksRepository.findByColorAndCottonPart(socksRecord.getColor().toLowerCase(), socksRecord.getCottonPart());
        Registration registration = createRegistration(socksRecord);
        registration.setStatus(Status.INCOME);

        if (socks != null) {
            socks.setQuantity(socks.getQuantity() + socksRecord.getQuantity());
            registration.setSocks(socksRepository.save(socks));
            registrationRepository.save(registration);
        } else {
            registration.setSocks(socksRepository.save(dtoMapper.toSocksEntity(socksRecord)));
            registrationRepository.save(registration);
        }
        return "Удалось добавить приход";
    }

    /**
     * Метод для регистрации отпуска носков со склада(Создается запись в таблице для регистрации и вычитается количество носков из записи в таблице носки)
     *
     * @param socksRecord тело для регистрации отпуска носков со склада
     * @return возвращает строку
     */
    public String addOutcome(SocksRecord socksRecord) {
        logger.info("Was invoked method addOutcome");
        Socks socks = socksRepository.findByColorAndCottonPart(socksRecord.getColor().toLowerCase(), socksRecord.getCottonPart());
        Registration registration = createRegistration(socksRecord);
        registration.setStatus(Status.OUTCOME);

        if (socks != null) {
            int quantity = socks.getQuantity() - socksRecord.getQuantity();
            if (quantity < 0) {
                logger.error("The number of socks is more than there is in stock");
                throw new QuantityLessZeroException(socksRecord.getQuantity(), socks.getQuantity());
            }
            socks.setQuantity(quantity);
            registration.setSocks(socksRepository.save(socks));
            registrationRepository.save(registration);
        } else {
            logger.error("There is not socks with color = {} and cotton = {}%", socksRecord.getColor(), socksRecord.getCottonPart());
            throw new SocksNotFoundException(socksRecord.getColor(), socksRecord.getCottonPart());
        }
        return "Удалось добавить отпуск";
    }

    /**
     * Метод для возвращения общего количества носков на складе, соответствующих переданным в параметрах критериям запроса
     *
     * @param color цвет носков
     * @param operation оператор сравнения значения количества хлопка в составе носков
     * @param cottonPart значение процента хлопка в составе носков из сравнения
     * @return возвращает строку
     */
    public String getQuantity(String color, String operation, Integer cottonPart) {
        logger.info("Was invoked method getQuantity");
        if (cottonPart < 0 || cottonPart > 100) {
            throw new CottonPartMoreLessException();
        }
        switch (operation) {
            case "moreThan": {
                Integer count = socksRepository.getCountSocksMore(color.toLowerCase(), cottonPart);
                return checkCountSocks(count);
            }
            case "lessThan": {
                Integer count = socksRepository.getCountSocksLess(color.toLowerCase(), cottonPart);
                return checkCountSocks(count);
            }
            case "equal": {
                Integer count = socksRepository.getCountSocksEqual(color.toLowerCase(), cottonPart);
                return checkCountSocks(count);
            }
            default:
                logger.error("The parameter \"operation\" was sent incorrectly");
                throw new OperationException();
        }
    }

    /**
     * Метод для проверки, есть ли на складе носки, соответствующие переданным в параметрах критериям запроса
     *
     * @param count общего количества носков на складе
     * @return возвращает строку
     */
    private String checkCountSocks(Integer count) {
        if (count != null) {
            return "Количество носков на складе - " + count;
        } else {
            logger.error("No socks");
            throw new SocksNotFoundException2();
        }
    }

    /**
     * Метод для создания регистрации
     *
     * @param socksRecord тело для регистрации прихода/отпуска носков со склада
     * @return возвращает регистрацию
     */
    private Registration createRegistration(SocksRecord socksRecord) {
        Registration registration = new Registration();
        registration.setDate(LocalDateTime.now());
        registration.setQuantity(socksRecord.getQuantity());
        return registration;
    }

    /**
     * Метод для получения всех пар носков
     *
     * @return Возвращает список всех пар носков
     */
    public List<SocksRecord> getAllSocks() {
        logger.info("Was invoked method for get all socks");
        return socksRepository.findAll().stream()
                .map(dtoMapper::toSocksDto)
                .collect(Collectors.toList());
    }

    /**
     * Метод для получения всех регистраций прихода/отпуска носков со склада
     *
     * @return Возвращает список всех регистраций прихода/отпуска носков со склада
     */
    public List<RegistrationRecord> getAllRegistration() {
        logger.info("Was invoked method for get all registration");
        return registrationRepository.findAll().stream()
                .map(dtoMapper::toRegistrationDto)
                .collect(Collectors.toList());
    }
}
