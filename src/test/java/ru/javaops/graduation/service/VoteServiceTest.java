package ru.javaops.graduation.service;

import ru.javaops.graduation.model.Vote;
import ru.javaops.graduation.util.exception.NotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ru.javaops.graduation.DishTestData.DISH_1_ID;
import static ru.javaops.graduation.UserTestData.*;
import static ru.javaops.graduation.VoteTestData.*;
import static org.junit.Assert.assertThrows;

public class VoteServiceTest extends AbstractServiceTest {
    @Autowired
    protected VoteService voteService;

    @Test
    public void vote() throws Exception {
        Vote newVote = getNewVote();
        Vote created = voteService.vote(newVote.getUser().id(), newVote.getRestaurant().id(), newVote.getDate().atTime(10, 0));
        newVote.setId(created.getId());
        VOTE_MATCHER.assertMatch(created,newVote);
        VOTE_MATCHER.assertMatch(voteService.getByUserIdAAndDate(created.getUser().getId(), created.getDate()), newVote);
    }

    @Test
    public void getAll() throws Exception {
        List<Vote> voteList = voteService.getAll(USER_ID);
        VOTE_LAZY_MATCHER.assertMatch(voteList, VOTE_3, VOTE_2, VOTE_1);
    }

    @Test
    public void delete() throws Exception {
        voteService.delete(VOTE_1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> voteService.getByUserIdAAndDate(USER_ID, VOTE_1.getDate()));
    }

    @Test
    public void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> voteService.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> voteService.delete(DISH_1_ID, ADMIN_ID));
    }
}