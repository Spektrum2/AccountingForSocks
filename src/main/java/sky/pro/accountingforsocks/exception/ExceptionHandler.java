package sky.pro.accountingforsocks.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(QuantityLessZeroException.class)
    public ResponseEntity<String> handlesQuantityLessZeroException(QuantityLessZeroException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(String.format("Невозможно отпустить носков в количестве %d, так как на складе имеется всего %d", e.getQuantityNeed(), e.getQuantity()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(SocksNotFoundException.class)
    public ResponseEntity<String> handlesSocksNotFoundException(SocksNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("Носков с цветом %s и c процентным содержанием хлопка %d на складе нет", e.getColor(), e.getCottonPart()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(SocksNotFoundException2.class)
    public ResponseEntity<String> handlesSocksNotFoundException2(SocksNotFoundException2 e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Носков с данными параметрами на складе нет");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CottonPartMoreLessException.class)
    public ResponseEntity<String> handlesCottonPartMoreLessException(CottonPartMoreLessException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Процентное содержание хлопка должно быть от 0 до 100");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(OperationException.class)
    public ResponseEntity<String> handlesOperationException(OperationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Нужно ввести один из параматров - moreThan, lessThan, equal");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handlesMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        e.getBindingResult().getFieldErrors().stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .collect(Collectors.joining(", "))
                );
    }
}
