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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import ru.javaops.graduation.util.RepositoryUtil;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javaops.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @Setter
    private Clock clock = Clock.systemDefaultZone();

    @CacheEvict(value = "restaurantTos", allEntries = true)
    public Vote vote(int userId, int restaurantId) {
        LocalDateTime votingLocalDateTime = LocalDateTime.now(clock);
        try {
            Vote vote = checkNotFoundWithId(voteRepository.getByUserIdAndDate(userId, votingLocalDateTime.toLocalDate()), userId);
            if (votingLocalDateTime.toLocalTime().isBefore(Vote.VOTE_DEADLINE)) {
                if (vote.getRestaurant().id() != restaurantId) {
                    vote.setRestaurant(RepositoryUtil.findById(restaurantRepository, restaurantId));
                    return voteRepository.save(vote);
                }
                return vote;
            } else {
                throw new VoteDeadlineException("Vote deadline has already passed");
            }
        } catch (NotFoundException e) {
            final Restaurant restaurant = RepositoryUtil.findById(restaurantRepository, restaurantId);
            final User user = RepositoryUtil.findById(userRepository, userId);
            return voteRepository.save(new Vote(null, votingLocalDateTime.toLocalDate(), user, restaurant));
        }
    }

    public Vote getByUserIdAndDate(int userId, LocalDate date) {
        return checkNotFoundWithId(voteRepository.getByUserIdAndDate(userId, date), userId);
    }

    public List<Vote> getAll(int userId) {
        return voteRepository.getAll(userId);
    }
}