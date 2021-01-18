package ru.javaops.graduation.service;

import ru.javaops.graduation.model.Restaurant;
import ru.javaops.graduation.model.User;
import ru.javaops.graduation.model.Vote;
import ru.javaops.graduation.repository.RestaurantRepository;
import ru.javaops.graduation.repository.UserRepository;
import ru.javaops.graduation.repository.VoteRepository;
import ru.javaops.graduation.util.exception.NotFoundException;
import ru.javaops.graduation.util.exception.VoteDeadlineException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javaops.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
@RequiredArgsConstructor
public class VoteService {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final LocalTime VOTE_DEADLINE = LocalTime.of(11, 0);

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @Setter
    private Clock clock = Clock.systemDefaultZone();

    @CacheEvict(value = "restaurantTos", allEntries = true)
    @Transactional
    public Vote vote(int userId, int restaurantId) {
        LocalDateTime votingLocalDateTime = LocalDateTime.now(clock);
        final Restaurant restaurant = checkNotFoundWithId(restaurantRepository.getOne(restaurantId), restaurantId);
        final User user = checkNotFoundWithId(userRepository.getOne(userId), userId);
        Vote vote;
        try {
            vote = getByUserAndDate(user, votingLocalDateTime.toLocalDate());
        } catch (NotFoundException e) {
            log.debug("new vote from user {} for {}", userId, restaurantId);
            return voteRepository.save(new Vote(null, votingLocalDateTime.toLocalDate(), user, restaurant));
        }

        if (votingLocalDateTime.toLocalTime().isBefore(VOTE_DEADLINE)) {
            if (vote.getRestaurant().id() != restaurantId) {
                log.debug("vote from user {} for restaurant {} was changed", userId, restaurantId);
                vote.setRestaurant(restaurant);
                return vote;
            }
            log.debug("vote from user {} for restaurant {} not changed", userId, restaurantId);
            return vote;
        } else {
            throw new VoteDeadlineException("Vote deadline has already passed");
        }
    }

    public Vote getByUserAndDate(User user, LocalDate date) {
        return checkNotFoundWithId(voteRepository.getByUserAndDate(user, date), user.id());
    }

    public Vote getByUserIdAndDate(int userId, LocalDate date) {
        return checkNotFoundWithId(voteRepository.getByUserIdAndDate(userId, date), userId);
    }

    public List<Vote> getAll(int userId) {
        return voteRepository.getAll(userId);
    }
}