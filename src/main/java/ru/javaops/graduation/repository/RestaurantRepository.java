package ru.javaops.graduation.repository;

import ru.javaops.graduation.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.graduation.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT DISTINCT new ru.javaops.graduation.to.RestaurantTo(r.id, r.name, count(vote))  " +
            "FROM Restaurant r LEFT OUTER JOIN Vote vote ON r.id = vote.restaurant.id AND vote.date=:date " +
            "GROUP BY r.id ORDER BY count(vote) DESC, r.name ASC")
    List<RestaurantTo> getAllByDate(@Param("date") LocalDate date);
}