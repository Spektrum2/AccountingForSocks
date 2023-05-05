package sky.pro.accountingforsocks.dto;

import lombok.Data;
import sky.pro.accountingforsocks.model.Socks;
import sky.pro.accountingforsocks.model.Status;

import java.time.LocalDateTime;

@Data
public class RegistrationRecord {
    private LocalDateTime date;
    private int quantity;
    private Status status;
    private Socks socks;
}
