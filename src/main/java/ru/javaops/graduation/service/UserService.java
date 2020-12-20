package ru.javaops.graduation.service;

import ru.javaops.graduation.model.User;
import ru.javaops.graduation.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import static ru.javaops.graduation.util.RepositoryUtil.findById;
import static ru.javaops.graduation.util.ValidationUtil.checkNotFound;
import static ru.javaops.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
public class UserService {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.save(user);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }


    public User get(int id) {
        return findById(repository, id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        return repository.findAll(SORT_NAME_EMAIL);
    }

    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        checkNotFoundWithId(repository.save(user), user.id());
    }

    public User getWithVotes(int id) {
        return checkNotFoundWithId(repository.getWithVotes(id), id);
    }
}