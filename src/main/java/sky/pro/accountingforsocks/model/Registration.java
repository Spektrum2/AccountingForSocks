package sky.pro.accountingforsocks.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс регистрация, для хранения информации о регистрации прихода/отпуска носков со склада
 */
@Entity
public class Registration {
    /**
     * Поле id регистрации
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /**
     * Поле - время регистрации носков на складе
     */
    private LocalDateTime date;
    /**
     * Поле - количество прихода/отпуска носков
     */
    private int quantity;
    /**
     * Поле - выбор статуса регистрации(INCOME - приход, OUTCOME - отпуск)
     */
    @Enumerated(EnumType.STRING)
    private Status status;
    /**
     * Поле - Носки
     */
    @ManyToOne
    @JoinColumn(name = "socks_id")
    private Socks socks;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Socks getSocks() {
        return socks;
    }

    public void setSocks(Socks socks) {
        this.socks = socks;
    }
}
