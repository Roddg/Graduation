package ru.javaops.graduation.util;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaops.graduation.util.exception.NotFoundException;

public class RepositoryUtil {
    private RepositoryUtil() {
    }

    public static <T, K extends Integer> T findById(JpaRepository<T, K> repository, K id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id " + id));
    }
}