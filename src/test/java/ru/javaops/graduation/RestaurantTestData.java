package ru.javaops.graduation;

import ru.javaops.graduation.model.Restaurant;

import static ru.javaops.graduation.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingFieldsComparator("registered", "dishes", "votes");

    public static final int NOT_FOUND = 10;
    public static final int RESTAURANT_1_ID = START_SEQ + 3;
    public static final int RESTAURANT_2_ID = START_SEQ + 4;
    public static final int RESTAURANT_3_ID = START_SEQ + 5;
    public static final int RESTAURANT_4_ID = START_SEQ + 6;

    public static final Restaurant RESTAURANT_1 = new Restaurant(RESTAURANT_1_ID, "Aragawa");
    public static final Restaurant RESTAURANT_2 = new Restaurant(RESTAURANT_2_ID, "Erarta");
    public static final Restaurant RESTAURANT_3 = new Restaurant(RESTAURANT_3_ID, "KFC");
    public static final Restaurant RESTAURANT_4 = new Restaurant(RESTAURANT_4_ID, "Masa");

    public static Restaurant getNew() {
        return new Restaurant(null, "New");
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(RESTAURANT_1);
        updated.setName("UpdatedName");
        return updated;
    }
}