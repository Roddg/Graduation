package ru.javaops.graduation.web.user;

import ru.javaops.graduation.AuthorizedUser;
import ru.javaops.graduation.model.User;
import ru.javaops.graduation.model.Vote;
import ru.javaops.graduation.to.UserTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = ProfileRestController.REST_URL)
public class ProfileRestController extends AbstractUserController {
    public static final String REST_URL = "/profile";

    public static final String VOTE_URL = "/votes";

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        User created = super.create(userTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@AuthenticationPrincipal AuthorizedUser authUser) {
        return super.get(authUser.getId());
    }

    @GetMapping("/with-votes")
    public User getWithVotes(@AuthenticationPrincipal AuthorizedUser authUser) {
        return super.getWithVotes(authUser.getId());
    }

    @GetMapping(VOTE_URL + "/today")
    public Vote getTodayVote(@AuthenticationPrincipal AuthorizedUser authUser) {
        return super.getTodayVote(authUser.getId());
    }

    @GetMapping(VOTE_URL)
    public List<Vote> getAllVotes(@AuthenticationPrincipal AuthorizedUser authUser) {
        return super.getAllVotes(authUser.getId());
    }

    @GetMapping(VOTE_URL + "/by")
    public Vote getByDate(@AuthenticationPrincipal AuthorizedUser authUser, @RequestParam LocalDate date) {
        log.info("get vote for {} by date {}", authUser.getId(), date);
        return super.getVoteByDate(authUser.getId(), date);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authUser) {
        super.delete(authUser.getId());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody UserTo userTo, @AuthenticationPrincipal AuthorizedUser authUser) throws BindException {
        checkAndValidateForUpdate(userTo, authUser.getId());
        userService.update(userTo);
    }
}