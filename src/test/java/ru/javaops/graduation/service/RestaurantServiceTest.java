package ru.javaops.graduation.service;

import ru.javaops.graduation.model.Restaurant;
import ru.javaops.graduation.to.RestaurantTo;
import ru.javaops.graduation.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.javaops.graduation.DishTestData;
import ru.javaops.graduation.RestaurantTestData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RestaurantServiceTest extends AbstractServiceTest {
    @Autowired
    protected RestaurantService service;

    @Test
    void create() throws Exception {
        Restaurant created = service.create(RestaurantTestData.getNewRestaurant());
        int newId = created.id();
        Restaurant newRestaurant = RestaurantTestData.getNewRestaurant();
        newRestaurant.setId(newId);
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(service.get(newId), newRestaurant);
    }

    @Test
    void duplicateNameCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                service.create(new Restaurant(null, "Aragawa")));
    }

    @Test
    void delete() throws Exception {
        service.delete(RestaurantTestData.RESTAURANT_1_ID);
        assertThrows(NotFoundException.class, () -> service.get(RestaurantTestData.RESTAURANT_1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(RestaurantTestData.NOT_FOUND));
    }

    @Test
    void get() throws Exception {
        Restaurant restaurant = service.get(RestaurantTestData.RESTAURANT_2_ID);
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(restaurant, RestaurantTestData.RESTAURANT_2);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(RestaurantTestData.NOT_FOUND));
    }

    @Test
    void update() throws Exception {
        Restaurant updated = RestaurantTestData.getUpdatedRestaurant();
        service.update(updated);
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(service.get(RestaurantTestData.RESTAURANT_1_ID), RestaurantTestData.getUpdatedRestaurant());
    }

    @Test
    void getAllByDishesDate() throws Exception {
        List<RestaurantTo> allByDate = service.getAllByDishesDate(DishTestData.DISH_1.getDate());

        allByDate.forEach(System.out::println);
    }
}