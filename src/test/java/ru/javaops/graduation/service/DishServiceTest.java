package ru.javaops.graduation.service;

import ru.javaops.graduation.model.Dish;
import ru.javaops.graduation.util.exception.NotFoundException;
import org.hsqldb.HsqlException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.graduation.DishTestData;
import ru.javaops.graduation.RestaurantTestData;
import ru.javaops.graduation.UserTestData;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DishServiceTest extends AbstractServiceTest {
    @Autowired
    protected DishService service;

    @Test
    void delete() throws Exception {
        service.delete(DishTestData.DISH_1_ID, RestaurantTestData.RESTAURANT_1_ID);
        assertThrows(NotFoundException.class, () -> service.get(DishTestData.DISH_1_ID, RestaurantTestData.RESTAURANT_1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(UserTestData.NOT_FOUND, RestaurantTestData.RESTAURANT_1_ID));
    }

    @Test
    void deleteNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(DishTestData.DISH_1_ID, RestaurantTestData.RESTAURANT_2_ID));
    }

    @Test
    void create() throws Exception {
        Dish created = service.create(DishTestData.getNewDish(), RestaurantTestData.RESTAURANT_1_ID);
        int newId = created.id();
        Dish newDish = DishTestData.getNewDish();
        newDish.setId(newId);
        DishTestData.DISH_MATCHER.assertMatch(created, newDish);
        DishTestData.DISH_MATCHER.assertMatch(service.get(newId, RestaurantTestData.RESTAURANT_1_ID), newDish);
    }

    @Test
    void get() throws Exception {
        Dish actual = service.get(DishTestData.DISH_1_ID, RestaurantTestData.RESTAURANT_1_ID);
        DishTestData.DISH_MATCHER.assertMatch(actual, DishTestData.DISH_1);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(UserTestData.NOT_FOUND, RestaurantTestData.RESTAURANT_1_ID));
    }

    @Test
    void getNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(DishTestData.DISH_1_ID, RestaurantTestData.RESTAURANT_2_ID));
    }

    @Test
    void update() throws Exception {
        Dish updated = DishTestData.getUpdatedDish();
        service.update(updated, RestaurantTestData.RESTAURANT_1_ID);
        DishTestData.DISH_MATCHER.assertMatch(service.get(DishTestData.DISH_1_ID, RestaurantTestData.RESTAURANT_1_ID), DishTestData.getUpdatedDish());
    }

    @Test
    void updateNotOwn() throws Exception {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.update(DishTestData.DISH_1, RestaurantTestData.RESTAURANT_3_ID));
        Assertions.assertEquals("Not found entity with id=" + DishTestData.DISH_1_ID, exception.getMessage());
    }

    @Test
    void getByRestaurantIdAndDate() throws Exception {
        DishTestData.DISH_MATCHER.assertMatch(service.getByDate(DishTestData.DISH_TEST_DATE, RestaurantTestData.RESTAURANT_1_ID), DishTestData.DISH_2, DishTestData.DISH_1, DishTestData.DISH_3);
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(() -> service.create(new Dish(null, DishTestData.DISH_1.getDate(), DishTestData.DISH_1.getName(), 1000L, DishTestData.DISH_1.getRestaurant()), RestaurantTestData.RESTAURANT_1_ID), HsqlException.class);
    }
}