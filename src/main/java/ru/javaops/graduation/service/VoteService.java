package ru.javaops.graduation.service;

import ru.javaops.graduation.model.Restaurant;
import ru.javaops.graduation.model.User;
import ru.javaops.graduation.model.Vote;
import ru.javaops.graduation.repository.RestaurantRepository;
import ru.javaops.graduation.repository.UserRepository;
import ru.javaops.graduation.repository.VoteRepository;
import ru.javaops.graduation.util.exception.NotFoundException;
import ru.javaops.graduation.util.exception.VoteDeadlineException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javaops.graduation.model.Vote.VOTE_DEADLINE;
import static ru.javaops.graduation.util.RepositoryUtil.findById;
import static ru.javaops.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public VoteService(VoteRepository repository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = repository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Vote vote(int userId, int restaurantId, LocalDateTime localDateTime) {
        Assert.notNull(restaurantId, "userId must not be null");
        Assert.notNull(userId, "restaurantId must not be null");
        try {
            Vote vote = checkNotFoundWithId(voteRepository.getByUserIdAndDate(userId, localDateTime.toLocalDate()), userId);
            if (localDateTime.toLocalTime().isBefore(VOTE_DEADLINE)) {
                if (vote.getRestaurant().id() != restaurantId) {
                    vote.setRestaurant(findById(restaurantRepository, restaurantId));
                    return voteRepository.save(vote);
                }
                return vote;
            } else {
                throw new VoteDeadlineException("Vote deadline has already passed");
            }
        } catch (NotFoundException e) {
            final Restaurant restaurant = findById(restaurantRepository, restaurantId);
            final User user = findById(userRepository, userId);
            return voteRepository.save(new Vote(null, localDateTime.toLocalDate(), user, restaurant));
        }
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(voteRepository.deleteByIdAndUserId(id, userId) != 0, id);
    }

    public Vote getByUserIdAAndDate(int userId, LocalDate date) {
        return checkNotFoundWithId(voteRepository.getByUserIdAndDate(userId, date), userId);
    }

    public List<Vote> getAll(int userId) {
        return voteRepository.getAll(userId);
    }
}