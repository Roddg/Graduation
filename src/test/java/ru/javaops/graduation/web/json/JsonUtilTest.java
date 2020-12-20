package ru.javaops.graduation.web.json;

import ru.javaops.graduation.UserTestData;
import ru.javaops.graduation.model.Dish;
import ru.javaops.graduation.model.Restaurant;
import ru.javaops.graduation.model.User;
import ru.javaops.graduation.model.Vote;
import org.junit.jupiter.api.Test;
import ru.javaops.graduation.DishTestData;
import ru.javaops.graduation.RestaurantTestData;
import ru.javaops.graduation.VoteTestData;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonUtilTest {
    @Test
    void readWriteValue() throws Exception {
        String voteJson = JsonUtil.writeValue(VoteTestData.VOTE_1);
        System.out.println(voteJson);
        Vote vote = JsonUtil.readValue(voteJson, Vote.class);
        VoteTestData.VOTE_LAZY_MATCHER.assertMatch(vote, VoteTestData.VOTE_1);

        String dishJson = JsonUtil.writeValue(DishTestData.DISH_1);
        System.out.println(dishJson);
        Dish dish = JsonUtil.readValue(dishJson, Dish.class);
        DishTestData.DISH_MATCHER.assertMatch(dish, DishTestData.DISH_1);

        String restaurantJson = JsonUtil.writeValue(RestaurantTestData.RESTAURANT_1);
        System.out.println(restaurantJson);
        Restaurant restaurant = JsonUtil.readValue(restaurantJson, Restaurant.class);
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(restaurant, RestaurantTestData.RESTAURANT_1);
    }

    @Test
    void readWriteValues() throws Exception {
        String json = JsonUtil.writeValue(VoteTestData.VOTES);
        System.out.println(json);
        List<Vote> votes = JsonUtil.readValues(json, Vote.class);
        VoteTestData.VOTE_LAZY_MATCHER.assertMatch(votes, VoteTestData.VOTES);
    }

    @Test
    void writeOnlyAccess() throws Exception {
        String json = JsonUtil.writeValue(UserTestData.USER);
        System.out.println(json);
        assertThat(json, not(containsString("password")));
        String jsonWithPass = UserTestData.jsonWithPassword(UserTestData.USER, "newPass");
        System.out.println(jsonWithPass);
        User user = JsonUtil.readValue(jsonWithPass, User.class);
        assertEquals(user.getPassword(), "newPass");
    }
}