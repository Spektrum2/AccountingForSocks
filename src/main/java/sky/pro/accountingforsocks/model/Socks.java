package sky.pro.accountingforsocks.model;

import javax.persistence.*;
import java.util.List;

/**
 * Класс носки, для хранения информации о носках на складе
 */
@Entity
public class Socks {
    /**
     * Поле - id носков
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /**
     * Поле - цвет носков
     */
    private String color;
    /**
     * Поле - процент хлопка
     */
    private int cottonPart;
    /**
     * Поле - количество носков на складе
     */
    private int quantity;
    /**
     * Поле - регистрации
     */
    @OneToMany(mappedBy = "socks")
    private List<Registration> registrations;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCottonPart() {
        return cottonPart;
    }

    public void setCottonPart(int cottonPart) {
        this.cottonPart = cottonPart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }
}
