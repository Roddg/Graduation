package ru.javaops.graduation.web.user;

import ru.javaops.graduation.UserTestData;
import ru.javaops.graduation.model.User;
import ru.javaops.graduation.service.UserService;
import ru.javaops.graduation.util.exception.NotFoundException;
import ru.javaops.graduation.web.AbstractControllerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.graduation.TestUtil;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminRestController.REST_URL + '/';

    @Autowired
    private UserService userService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + UserTestData.ADMIN_ID)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(UserTestData.USER_MATCHER.contentJson(UserTestData.ADMIN));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + 1)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by?email=" + UserTestData.ADMIN.getEmail())
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(UserTestData.USER_MATCHER.contentJson(UserTestData.ADMIN));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + UserTestData.USER_ID)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> userService.get(UserTestData.USER_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + 1)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        User updated = UserTestData.getUpdatedUser();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL + UserTestData.USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN))
                .content(UserTestData.jsonWithPassword(updated, "newPass")))
                .andExpect(status().isNoContent());

        UserTestData.USER_MATCHER.assertMatch(userService.get(UserTestData.USER_ID), UserTestData.getUpdatedUser());
    }

    @Test
    void createWithLocation() throws Exception {
        User newUser = UserTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN))
                .content(UserTestData.jsonWithPassword(newUser, "newPass")))
                .andExpect(status().isCreated());

        User created = TestUtil.readFromJson(action, User.class);
        int newId = created.id();
        newUser.setId(newId);
        UserTestData.USER_MATCHER.assertMatch(created, newUser);
        UserTestData.USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(UserTestData.USER_MATCHER.contentJson(UserTestData.ADMIN, UserTestData.USER));
    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + UserTestData.USER_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());

        Assertions.assertFalse(userService.get(UserTestData.USER_ID).isEnabled());
    }

    @Test
    void getWithVotes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + UserTestData.ADMIN_ID + "/with-votes")
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(UserTestData.USER_MATCHER.contentJson(UserTestData.ADMIN));
    }
}