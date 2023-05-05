package sky.pro.accountingforsocks.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class SocksRecord {
    @NotBlank(message = "Цвет носок должен быть заполнен!")
    private String color;

    @Min(value = 0, message = "Минимальный процент содержания хлопка 0")
    @Max(value = 100, message = "Максимальный процент содержания хлопка 100")
    private int cottonPart;

    @Min(value = 1, message = "Минимальное количество пар носков 1")
    private int quantity;
}
