package ru.javaops.graduation.service;

import ru.javaops.graduation.model.Vote;
import ru.javaops.graduation.util.exception.VoteDeadlineException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.util.List;

import static ru.javaops.graduation.UserTestData.USER_ID;
import static ru.javaops.graduation.VoteTestData.*;
import static ru.javaops.graduation.service.VoteService.VOTE_DEADLINE;
import static ru.javaops.graduation.util.DateTimeUtil.createClock;

class VoteServiceTest extends AbstractServiceTest {
    @Autowired
    protected VoteService voteService;

    @Test
    void voteSimple() throws Exception {
        Vote newVote = getNewVote();
        Vote created = voteService.vote(newVote.getUser().id(), newVote.getRestaurant().id());
        newVote.setId(created.getId());
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteService.getByUserAndDate(created.getUser(), created.getDate()), newVote);
    }

    @Test
    void voteAgainBeforeDeadline() throws Exception {
        Clock clock = createClock(VOTE_3.getDate(), VOTE_DEADLINE.minusMinutes(1));
        voteService.setClock(clock);
        Vote newVote = getNewVote();
        Vote created = voteService.vote(newVote.getUser().id(), newVote.getRestaurant().id());
        newVote.setId(created.getId());
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteService.getByUserAndDate(created.getUser(), created.getDate()), newVote);}

    @Test
    void voteAgainAfterDeadline() throws Exception {
        Clock clock = createClock(VOTE_3.getDate(), VOTE_DEADLINE);
        voteService.setClock(clock);
        Vote newVote = getNewVote();
        validateRootCause(() ->voteService.vote(newVote.getUser().id(), newVote.getRestaurant().id()), VoteDeadlineException.class);
    }

    @Test
    void getAll() throws Exception {
        List<Vote> voteList = voteService.getAll(USER_ID);
        VOTE_LAZY_MATCHER.assertMatch(voteList, VOTE_3, VOTE_2, VOTE_1);
    }

    @Test
    void getByUserIdAndDate() throws  Exception {
        Vote result = voteService.getByUserIdAndDate(USER_ID, VOTE_1.getDate());
        VOTE_LAZY_MATCHER.assertMatch(result, VOTE_1);
    }
}