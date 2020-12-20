package ru.javaops.graduation.to;

import ru.javaops.graduation.HasId;
import ru.javaops.graduation.model.Dish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTo extends BaseTo implements HasId {
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Positive
    private long rating;

    private Set<Dish> dailyMenu;

    public RestaurantTo(int id, String name, long rating, Set<Dish> dailyMenu) {
        super(id);
        this.name = name;
        this.rating = rating;
        this.dailyMenu = dailyMenu;
    }
}