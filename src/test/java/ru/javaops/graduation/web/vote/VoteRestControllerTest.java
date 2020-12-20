package ru.javaops.graduation.web.vote;

import ru.javaops.graduation.service.VoteService;
import ru.javaops.graduation.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.graduation.RestaurantTestData;
import ru.javaops.graduation.TestUtil;
import ru.javaops.graduation.UserTestData;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteRestControllerTest  extends AbstractControllerTest {

    @Autowired
    private VoteService voteService;

    @Test
    void voteSimple() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(VoteController.REST_URL)
                .param("restaurantId", String.valueOf(RestaurantTestData.RESTAURANT_1_ID))
                .with(TestUtil.userHttpBasic(UserTestData.USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isCreated());
    }
}