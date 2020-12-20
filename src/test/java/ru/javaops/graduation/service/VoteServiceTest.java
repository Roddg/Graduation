package ru.javaops.graduation.service;

import ru.javaops.graduation.model.Vote;
import ru.javaops.graduation.util.exception.VoteDeadlineException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.graduation.UserTestData;
import ru.javaops.graduation.VoteTestData;
import ru.javaops.graduation.util.DateTimeUtil;

import java.time.Clock;
import java.util.List;

class VoteServiceTest extends AbstractServiceTest {
    @Autowired
    protected VoteService voteService;

    @Test
    void voteSimple() throws Exception {
        Vote newVote = VoteTestData.getNewVote();
        Vote created = voteService.vote(newVote.getUser().id(), newVote.getRestaurant().id());
        newVote.setId(created.getId());
        VoteTestData.VOTE_MATCHER.assertMatch(created, newVote);
        VoteTestData.VOTE_MATCHER.assertMatch(voteService.getByUserIdAndDate(created.getUser().getId(), created.getDate()), newVote);
    }

    @Test
    void voteAgainBeforeDeadline() throws Exception {
        Clock clock = DateTimeUtil.createClock(VoteTestData.VOTE_3.getDate(), Vote.VOTE_DEADLINE.minusMinutes(1));
        voteService.setClock(clock);
        Vote newVote = VoteTestData.getNewVote();
        Vote created = voteService.vote(newVote.getUser().id(), newVote.getRestaurant().id());
        newVote.setId(created.getId());
        VoteTestData.VOTE_MATCHER.assertMatch(created, newVote);
        VoteTestData.VOTE_MATCHER.assertMatch(voteService.getByUserIdAndDate(created.getUser().getId(), created.getDate()), newVote);}

    @Test
    void voteAgainAfterDeadline() throws Exception {
        Clock clock = DateTimeUtil.createClock(VoteTestData.VOTE_3.getDate(), Vote.VOTE_DEADLINE);
        voteService.setClock(clock);
        Vote newVote = VoteTestData.getNewVote();
        validateRootCause(() ->voteService.vote(newVote.getUser().id(), newVote.getRestaurant().id()), VoteDeadlineException.class);
    }

    @Test
    void getAll() throws Exception {
        List<Vote> voteList = voteService.getAll(UserTestData.USER_ID);
        VoteTestData.VOTE_LAZY_MATCHER.assertMatch(voteList, VoteTestData.VOTE_3, VoteTestData.VOTE_2, VoteTestData.VOTE_1);
    }
}