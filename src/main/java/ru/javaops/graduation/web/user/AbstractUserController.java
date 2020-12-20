package ru.javaops.graduation.web.user;

import ru.javaops.graduation.HasId;
import ru.javaops.graduation.model.User;
import ru.javaops.graduation.service.UserService;
import ru.javaops.graduation.to.UserTo;
import ru.javaops.graduation.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.javaops.graduation.util.ValidationUtil;

import java.util.List;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private WebDataBinder binder;

    @Autowired
    protected UserService service;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if (binder.getTarget() != null && emailValidator.supports(binder.getTarget().getClass())) {
            binder.addValidators(emailValidator);
            this.binder = binder;
        }
    }

    public List<User> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public User get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public User create(User user) {
        log.info("create {}", user);
        ValidationUtil.checkNew(user);
        return service.create(user);
    }

    public User create(UserTo userTo) {
        log.info("create from to {}", userTo);
        return create(UserUtil.createNewFromTo(userTo));
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    protected void checkAndValidateForUpdate(HasId user, int id) throws BindException {
        log.info("update {} with id={}", user, id);
        ValidationUtil.assureIdConsistent(user, id);
        binder.validate();
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }

    public void update(User user, int id) throws BindException {
        log.info("update {} with id={}", user, id);
        ValidationUtil.assureIdConsistent(user, id);
        service.update(user);
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return service.getByEmail(email);
    }

    public void enable(int id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        service.enable(id, enabled);
    }

    public User getWithVotes(int id) {
        log.info("getWithVotes {}", id);
        return service.getWithVotes(id);
    }
}