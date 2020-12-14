package ru.javaops.graduation;

import ru.javaops.graduation.model.Dish;

import java.time.LocalDate;

import static ru.javaops.graduation.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingFieldsComparator("date", "restaurant");

    public static final int DISH_1_ID = START_SEQ + 13;
    public static final int DISH_2_ID = START_SEQ + 14;
    public static final int DISH_3_ID = START_SEQ + 15;

    public static final Dish DISH_1 = new Dish(DISH_1_ID, LocalDate.now(), "Salmon tartare with daikon", 660);
    public static final Dish DISH_2 = new Dish(DISH_2_ID, LocalDate.now(), "Tuna tataki", 590);
    public static final Dish DISH_3 = new Dish(DISH_3_ID, LocalDate.now(), "Salmon salad with tangerines", 780);

    public static Dish getNewDish() {
        return new Dish(null, LocalDate.now(), "New", 1000);
    }

    public static Dish getUpdatedDish() {
        Dish updated = new Dish(DISH_1);
        updated.setName("UpdatedName");
        return updated;
    }
}
