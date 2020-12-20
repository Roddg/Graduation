package ru.javaops.graduation.service;

import ru.javaops.graduation.model.Restaurant;
import ru.javaops.graduation.util.exception.NotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;

import static ru.javaops.graduation.RestaurantTestData.*;
import static org.junit.Assert.assertThrows;

public class RestaurantServiceTest extends AbstractServiceTest {
    @Autowired
    protected RestaurantService service;

    @Test
    public void create() throws Exception {
        Restaurant created = service.create(getNewRestaurant());
        int newId = created.id();
        Restaurant newRestaurant = getNewRestaurant();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(service.get(newId), newRestaurant);
    }

    @Test
    public void duplicateNameCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                service.create(new Restaurant(null, "Aragawa")));
    }

    @Test
    public void delete() throws Exception {
        service.delete(RESTAURANT_1_ID);
        assertThrows(NotFoundException.class, () -> service.get(RESTAURANT_1_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    public void get() throws Exception {
        Restaurant restaurant = service.get(RESTAURANT_2_ID);
        RESTAURANT_MATCHER.assertMatch(restaurant, RESTAURANT_2);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void update() throws Exception {
        Restaurant updated = getUpdatedRestaurant();
        service.update(updated);
        RESTAURANT_MATCHER.assertMatch(service.get(RESTAURANT_1_ID), getUpdatedRestaurant());
    }

    @Test
    public void getAll() throws Exception {
        List<Restaurant> all = service.getAll();
        RESTAURANT_MATCHER.assertMatch(all, RESTAURANT_1, RESTAURANT_2, RESTAURANT_3, RESTAURANT_4);
    }

    @Test
    public void getWithVotesNotFound() throws Exception {
        assertThrows(NotFoundException.class,
                () -> service.getWithVotes(NOT_FOUND));
    }
}