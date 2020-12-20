package ru.javaops.graduation.service;

import ru.javaops.graduation.model.Dish;
import ru.javaops.graduation.util.exception.NotFoundException;
import org.hsqldb.HsqlException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ru.javaops.graduation.DishTestData.*;
import static ru.javaops.graduation.RestaurantTestData.*;
import static ru.javaops.graduation.UserTestData.NOT_FOUND;
import static org.junit.Assert.assertThrows;

public class DishServiceTest extends AbstractServiceTest {
    @Autowired
    protected DishService service;

    @Test
    public void delete() throws Exception {
        service.delete(DISH_1_ID, RESTAURANT_1_ID);
        assertThrows(NotFoundException.class, () -> service.get(DISH_1_ID, RESTAURANT_1_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, RESTAURANT_1_ID));
    }

    @Test
    public void deleteNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(DISH_1_ID, RESTAURANT_2_ID));
    }

    @Test
    public void create() throws Exception {
        Dish created = service.create(getNewDish(), RESTAURANT_1_ID);
        int newId = created.id();
        Dish newDish = getNewDish();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(service.get(newId, RESTAURANT_1_ID), newDish);
    }

    @Test
    public void get() throws Exception {
        Dish actual = service.get(DISH_1_ID, RESTAURANT_1_ID);
        DISH_MATCHER.assertMatch(actual, DISH_1);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, RESTAURANT_1_ID));
    }

    @Test
    public void getNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(DISH_1_ID, RESTAURANT_2_ID));
    }

    @Test
    public void update() throws Exception {
        Dish updated = getUpdatedDish();
        service.update(updated, RESTAURANT_1_ID);
        DISH_MATCHER.assertMatch(service.get(DISH_1_ID, RESTAURANT_1_ID), getUpdatedDish());
    }

    @Test
    public void updateNotOwn() throws Exception {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.update(DISH_1, RESTAURANT_3_ID));
        Assert.assertEquals("Not found entity with id=" + DISH_1_ID, exception.getMessage());
    }

    @Test
    public void getByRestaurantIdAndDate() throws Exception {
        DISH_MATCHER.assertMatch(service.getByDate(DISH_TEST_DATE, RESTAURANT_1_ID), DISH_2, DISH_1, DISH_3);
    }

    @Test
    public void createWithException() throws Exception {
        validateRootCause(() -> service.create(new Dish(null, DISH_1.getDate(), DISH_1.getName(), 1000L, DISH_1.getRestaurant()), RESTAURANT_1_ID), HsqlException.class);
    }
}