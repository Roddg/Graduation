package ru.javaops.graduation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ru.javaops.graduation.HasId;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class Dish extends AbstractNamedEntity implements HasId {
    @Column(name = "price", nullable = false)
    @Positive
    private Long price;

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date = LocalDate.now();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = CASCADE)
    @NotNull
    @JsonBackReference(value = "restaurantDishes")
    private Restaurant restaurant;

    public Dish(LocalDate date, String name, Long price, Restaurant restaurant) {
        this(null, date, name, price, restaurant);
    }

    public Dish(Integer id, LocalDate date, String name, Long price, Restaurant restaurant) {
        super(id, name);
        this.date = date;
        this.price = price;
        this.restaurant = restaurant;
    }

    public Dish(Dish d) {
        this(d.getId(), d.getDate(), d.getName(), d.getPrice(), d.getRestaurant());
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