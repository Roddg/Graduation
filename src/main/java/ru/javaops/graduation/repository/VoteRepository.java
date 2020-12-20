package ru.javaops.graduation.repository;

import ru.javaops.graduation.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId ORDER BY v.date DESC")
    List<Vote> getAll(@Param("userId") int userId);

    @EntityGraph(attributePaths = {"restaurant", "user"}, type = EntityGraph.EntityGraphType.LOAD)
    Vote getByUserIdAndDate(int userId, LocalDate date);
}