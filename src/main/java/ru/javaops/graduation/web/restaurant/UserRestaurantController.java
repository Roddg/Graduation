package ru.javaops.graduation.web.restaurant;

import ru.javaops.graduation.service.RestaurantService;
import ru.javaops.graduation.to.RestaurantTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = UserRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestaurantController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String REST_URL = "/restaurants";

    private final RestaurantService restaurantService;

    public UserRestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("get all");
        return restaurantService.getAllWithRatingByDate(LocalDate.now());
    }

    @GetMapping("/by")
    public List<RestaurantTo> getByDate(@RequestParam LocalDate date) {
        log.info("get by date {}", date);
        return restaurantService.getAllWithRatingByDate(date);
    }
}