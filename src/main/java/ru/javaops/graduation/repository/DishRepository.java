package ru.javaops.graduation.repository;

import ru.javaops.graduation.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {
    @Transactional
    @Modifying
    int deleteAllByRestaurantId(int restaurantId);

    List<Dish> getAllByRestaurantIdAndDate(int restaurantId, LocalDate date);

    int deleteById(@Param("id") int id);
}
