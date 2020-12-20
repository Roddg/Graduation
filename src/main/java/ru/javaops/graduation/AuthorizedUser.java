package ru.javaops.graduation;

import ru.javaops.graduation.model.User;
import ru.javaops.graduation.to.UserTo;
import ru.javaops.graduation.util.UserUtil;
import lombok.Getter;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;

    @Getter
    private UserTo userTo;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, user.getRoles());
        this.userTo = UserUtil.asTo(user);
    }

    public int getId() {
        return userTo.id();
    }

    public void update(UserTo newTo) {
        userTo = newTo;
    }

    @Override
    public String toString() {
        return userTo.toString();
    }
}