package ru.javaops.graduation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

import static javax.persistence.FetchType.LAZY;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date"}, name = "votes_unique_user_date_idx")})
@Getter
@Setter
@NoArgsConstructor
public class Vote extends AbstractBaseEntity {
    public static final LocalTime VOTE_DEADLINE = LocalTime.of(11, 0);

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date = LocalDate.now();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = CASCADE)
    @NotNull
    @JsonBackReference(value = "userVotes")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = CASCADE)
    @NotNull
    @JsonBackReference(value = "restaurantVotes")
    private Restaurant restaurant;

    public Vote(LocalDate date, User user, Restaurant restaurant) {
        this(null, date, user, restaurant);
    }

    public Vote(Integer id, LocalDate date, User user, Restaurant restaurant) {
        super(id);
        this.date = date;
        this.user = user;
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", date=" + date +
                '}';
    }
}