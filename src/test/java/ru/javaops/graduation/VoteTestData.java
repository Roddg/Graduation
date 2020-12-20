package ru.javaops.graduation;

import ru.javaops.graduation.model.Vote;
import ru.javaops.graduation.model.AbstractBaseEntity;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.graduation.UserTestData.USER;

public class VoteTestData {
    public static TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingEqualsAssertions(Vote.class);
    public static TestMatcher<Vote> VOTE_LAZY_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(Vote.class, "user", "restaurant");

    public static final int VOTE_1_ID = AbstractBaseEntity.START_SEQ + 6;
    public static final int VOTE_2_ID = AbstractBaseEntity.START_SEQ + 7;
    public static final int VOTE_3_ID = AbstractBaseEntity.START_SEQ + 8;

    public static final Vote VOTE_1 = new Vote(VOTE_1_ID, LocalDate.of(2020, 12, 10), USER, RestaurantTestData.RESTAURANT_1);
    public static final Vote VOTE_2 = new Vote(VOTE_2_ID, LocalDate.of(2020, 12, 11), USER, RestaurantTestData.RESTAURANT_1);
    public static final Vote VOTE_3 = new Vote(VOTE_3_ID, LocalDate.of(2020, 12, 12), USER, RestaurantTestData.RESTAURANT_3);

    public static final List<Vote> VOTES = List.of(VOTE_3, VOTE_2, VOTE_1);

    public static Vote getNewVote() {
        return new Vote(null, LocalDate.now(), USER, RestaurantTestData.RESTAURANT_1);
    }
}