package ru.javaops.graduation.web.user;

import ru.javaops.graduation.model.User;
import ru.javaops.graduation.service.UserService;
import ru.javaops.graduation.to.UserTo;
import ru.javaops.graduation.util.UserUtil;
import ru.javaops.graduation.web.AbstractControllerTest;
import ru.javaops.graduation.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.graduation.TestUtil;
import ru.javaops.graduation.UserTestData;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestControllerTest extends AbstractControllerTest {
    @Autowired
    private UserService userService;

    @Test
    void test() throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL)
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isOk());

        User user = TestUtil.readFromJson(resultActions, User.class);
        System.out.println(user.toString());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL)
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(UserTestData.USER_MATCHER.contentJson(UserTestData.USER));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(ProfileRestController.REST_URL)
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isNoContent());
        UserTestData.USER_MATCHER.assertMatch(userService.getAll(), UserTestData.ADMIN);
    }

    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword");
        User newUser = UserUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(ProfileRestController.REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = TestUtil.readFromJson(action, User.class);
        int newId = created.getId();
        newUser.setId(newId);
        UserTestData.USER_MATCHER.assertMatch(created, newUser);
        UserTestData.USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "user@yandex.ru", "newPassword");
        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(TestUtil.userHttpBasic(UserTestData.USER))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        UserTestData.USER_MATCHER.assertMatch(userService.get(UserTestData.USER_ID), UserUtil.updateFromTo(new User(UserTestData.USER), updatedTo));
    }

    @Test
    void getWithVotes() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL + "/with-votes")
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(UserTestData.USER_MATCHER.contentJson(UserTestData.USER));
    }

    @Test
    void updateInvalid() throws Exception {
        UserTo updatedTo = new UserTo(null, null, "password", null);
        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(TestUtil.userHttpBasic(UserTestData.USER))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "admin@gmail.com", "newPassword");
        perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(TestUtil.userHttpBasic(UserTestData.USER))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}