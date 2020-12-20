package ru.javaops.graduation.util;

import org.springframework.data.jpa.repository.JpaRepository;

import static ru.javaops.graduation.util.ValidationUtil.checkNotFoundWithId;

public class RepositoryUtil {
    private RepositoryUtil() {
    }

    public static <T, K extends Integer> T findById(JpaRepository<T, K> repository, K id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }
}