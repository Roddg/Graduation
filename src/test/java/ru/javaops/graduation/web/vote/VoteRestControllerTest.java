package ru.javaops.graduation.web.vote;

import ru.javaops.graduation.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.graduation.RestaurantTestData;
import ru.javaops.graduation.TestUtil;
import ru.javaops.graduation.UserTestData;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.graduation.web.vote.VoteController.REST_URL;

class VoteRestControllerTest  extends AbstractControllerTest {

    @Test
    void voteSimple() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RestaurantTestData.RESTAURANT_1_ID))
                .with(TestUtil.userHttpBasic(UserTestData.USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isCreated());
    }
}