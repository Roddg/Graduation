package ru.javaops.graduation.service;

import ru.javaops.graduation.VoteTestData;
import ru.javaops.graduation.model.Role;
import ru.javaops.graduation.model.User;
import ru.javaops.graduation.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.javaops.graduation.UserTestData;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest extends AbstractServiceTest {

    @Autowired
    protected UserService service;

    @Test
    void create() throws Exception {
        User created = service.create(UserTestData.getNew());
        int newId = created.id();
        User newUser = UserTestData.getNew();
        newUser.setId(newId);
        UserTestData.USER_MATCHER.assertMatch(created, newUser);
        UserTestData.USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    void duplicateMailCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    void delete() throws Exception {
        service.delete(UserTestData.USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(UserTestData.USER_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(UserTestData.NOT_FOUND));
    }

    @Test
    void get() throws Exception {
        User user = service.get(UserTestData.USER_ID);
        UserTestData.USER_MATCHER.assertMatch(user, UserTestData.USER);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(UserTestData.NOT_FOUND));
    }

    @Test
    void getByEmail() throws Exception {
        User user = service.getByEmail("admin@gmail.com");
        UserTestData.USER_MATCHER.assertMatch(user, UserTestData.ADMIN);
    }

    @Test
    void update() throws Exception {
        User updated = UserTestData.getUpdatedUser();
        service.update(updated);
        UserTestData.USER_MATCHER.assertMatch(service.get(UserTestData.USER_ID), UserTestData.getUpdatedUser());
    }

    @Test
    void getAll() throws Exception {
        List<User> all = service.getAll();
        UserTestData.USER_MATCHER.assertMatch(all, UserTestData.ADMIN, UserTestData.USER);
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(() -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "  ", "password", Role.USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.USER)), ConstraintViolationException.class);
    }

    @Test
    void getWithVotes() throws Exception {
        User user = service.getWithVotes(UserTestData.USER_ID);
        UserTestData.USER_MATCHER.assertMatch(user, UserTestData.USER);
        VoteTestData.VOTE_LAZY_MATCHER.assertMatch(user.getVotes(), VoteTestData.VOTES);
    }

    @Test
    void getWithVotesNotFound() throws Exception {
        assertThrows(NotFoundException.class,
                () -> service.getWithVotes(UserTestData.NOT_FOUND));
    }
}