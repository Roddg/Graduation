package ru.javaops.graduation;

import ru.javaops.graduation.model.Role;
import ru.javaops.graduation.model.User;
import ru.javaops.graduation.web.json.JsonUtil;
import ru.javaops.graduation.model.AbstractBaseEntity;

import java.util.Collections;
import java.util.Date;

public class UserTestData {
    public static TestMatcher<User> USER_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(User.class, "registered", "votes", "password");

    public static final int NOT_FOUND = 10;
    public static final int USER_ID = AbstractBaseEntity.START_SEQ;
    public static final int ADMIN_ID = AbstractBaseEntity.START_SEQ + 1;

    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "password", Role.USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN);

    static {
        USER.setVotes(VoteTestData.VOTES);
    }

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdatedUser() {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        return updated;
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}