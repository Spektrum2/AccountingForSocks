package sky.pro.accountingforsocks.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;
import sky.pro.accountingforsocks.dto.RegistrationRecord;
import sky.pro.accountingforsocks.dto.SocksRecord;
import sky.pro.accountingforsocks.service.SocksService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/socks")
public class SocksController {

    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @Operation(
            summary = "Регистрация прихода носков на склад",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Регистрация прихода носков на склад"
                    )
            }
    )
    @PostMapping("/income")
    private String addIncome(@RequestBody @Valid SocksRecord socksRecord) {
        return socksService.addIncome(socksRecord);
    }

    @Operation(
            summary = "Регистрация отпуска носков со склада",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Регистрация отпуска носков со склада"
                    )
            }
    )
    @PostMapping("/outcome")
    private String addOutcome(@RequestBody @Valid SocksRecord socksRecord) {
        return socksService.addOutcome(socksRecord);
    }

    @Operation(
            summary = "Возвращение общего количества носков на складе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Возвращение общего количества носков на складе"
                    )
            }
    )
    @GetMapping
    private String getQuantity(@Parameter(description = "Введите цвет носков", example = "black")
                               @RequestParam String color,
                               @Parameter(description = "Введите количества хлопка в составе носков - moreThan, lessThan, equal", example = "moreThan")
                               @RequestParam String operation,
                               @Parameter(description = "Введите значение процента хлопка от 0 до 100", example = "30")
                               @RequestParam Integer cottonPart) {
        return socksService.getQuantity(color, operation, cottonPart);
    }

    @Operation(
            summary = "Возвращение списока всех пар носков",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Возвращение списока всех пар носков"
                    )
            }
    )
    @GetMapping("/allSocks")
    private List<SocksRecord> getAllSocks(){
        return socksService.getAllSocks();
    }

    @Operation(
            summary = "Возвращение всех регистраций прихода/отпуска носков со склада",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Возвращение всех регистраций прихода/отпуска носков со склада"
                    )
            }
    )
    @GetMapping("/allRegistration")
    private List<RegistrationRecord> getAllRegistration(){
        return socksService.getAllRegistration();
    }

}
