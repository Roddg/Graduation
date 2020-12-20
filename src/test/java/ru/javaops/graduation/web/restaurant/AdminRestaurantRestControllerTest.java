package ru.javaops.graduation.web.restaurant;

import ru.javaops.graduation.model.Dish;
import ru.javaops.graduation.model.Restaurant;
import ru.javaops.graduation.service.DishService;
import ru.javaops.graduation.service.RestaurantService;
import ru.javaops.graduation.util.exception.NotFoundException;
import ru.javaops.graduation.web.AbstractControllerTest;
import ru.javaops.graduation.web.json.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.graduation.DishTestData;
import ru.javaops.graduation.RestaurantTestData;
import ru.javaops.graduation.TestUtil;
import ru.javaops.graduation.UserTestData;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRestaurantRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestaurantController.REST_URL + '/';

    private static final String DISHES_FULL_REST_URL = AdminRestaurantController.REST_URL + AdminRestaurantController.DISHES_REST_URL + '/';

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    DishService dishService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT_1_ID)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_MATCHER.contentJson(RestaurantTestData.RESTAURANT_1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT_1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT_1_ID)
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.NOT_FOUND)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RestaurantTestData.RESTAURANT_1_ID)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> restaurantService.get(RestaurantTestData.RESTAURANT_1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RestaurantTestData.NOT_FOUND)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void create() throws Exception {
        Restaurant newRestaurant = RestaurantTestData.getNewRestaurant();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andDo(print())
                .andExpect(status().isCreated());

        Restaurant created = TestUtil.readFromJson(action, Restaurant.class);
        int newId = created.getId();
        newRestaurant.setId(newId);
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(restaurantService.get(newId), newRestaurant);
    }

    @Test
    void update() throws Exception {
        Restaurant updated = RestaurantTestData.getUpdatedRestaurant();
        perform(MockMvcRequestBuilders.put(REST_URL + RestaurantTestData.RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(restaurantService.get(RestaurantTestData.RESTAURANT_1_ID), updated);
    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + RestaurantTestData.RESTAURANT_1_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());

        Assertions.assertFalse(restaurantService.get(RestaurantTestData.RESTAURANT_1_ID).isEnabled());
    }

    @Test
    void getDish() throws Exception {
        perform(MockMvcRequestBuilders.get(DISHES_FULL_REST_URL + DishTestData.DISH_1_ID, RestaurantTestData.RESTAURANT_1_ID)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DishTestData.DISH_MATCHER.contentJson(DishTestData.DISH_1));
    }

    @Test
    void createDishWithLocation() throws Exception {
        Dish newDish = DishTestData.getNewDish();
        ResultActions action = perform(MockMvcRequestBuilders.post(DISHES_FULL_REST_URL, RestaurantTestData.RESTAURANT_1_ID)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isCreated());

        Dish created = TestUtil.readFromJson(action, Dish.class);
        int newId = created.id();
        newDish.setId(newId);
        DishTestData.DISH_MATCHER.assertMatch(created, newDish);
        DishTestData.DISH_MATCHER.assertMatch(dishService.get(newId, RestaurantTestData.RESTAURANT_1_ID), newDish);
    }

    @Test
    void updateDish() throws Exception {
        Dish updated = DishTestData.getUpdatedDish();
        perform(MockMvcRequestBuilders.put(DISHES_FULL_REST_URL + DishTestData.DISH_1_ID, RestaurantTestData.RESTAURANT_1_ID)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        DishTestData.DISH_MATCHER.assertMatch(dishService.get(DishTestData.DISH_1_ID, RestaurantTestData.RESTAURANT_1_ID), updated);
    }

    @Test
    void deleteDish() throws Exception {
        perform(MockMvcRequestBuilders.delete(DISHES_FULL_REST_URL + DishTestData.DISH_1_ID, RestaurantTestData.RESTAURANT_1_ID)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> dishService.get(DishTestData.DISH_1_ID, RestaurantTestData.RESTAURANT_1_ID));
    }
}