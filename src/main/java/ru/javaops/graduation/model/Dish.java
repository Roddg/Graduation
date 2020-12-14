package ru.javaops.graduation.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "name", "date"}, name = "dishes_unique_restaurant_name_date_idx")})
@Getter
@Setter
public class Dish extends AbstractNamedEntity {
    @Column(name = "price", nullable = false)
    @Positive
    private int price;

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date = LocalDate.now();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = CASCADE)
    @NotNull
    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(LocalDate date, String name, Integer price) {
        this(null, date, name, price);
    }

    public Dish(Integer id, LocalDate date, String name, Integer price) {
        super(id, name);
        this.date = date;
        this.price = price;
    }

    public Dish(Dish d) {
        this(d.getId(), d.getDate(), d.getName(), d.getPrice());
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
