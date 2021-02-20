package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private MealService service;
@Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id) {
    // получаем ID авторизовннного пользователя
        int userId = SecurityUtil.authUserId();
        log.info("get meal {} for user {}", id, userId);
        // делегируем действие на сервис
        return service.get(id,userId);
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        checkNew(meal);
        log.info("create {} for user {}", meal, userId);
        return service.create(meal, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for user {}", id,userId);
        service.delete(id, userId);
    }

    public void update(Meal user, int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(user, id);
        log.info("update {} for user {}", user, userId);
        service.update(user, userId);
    }

    public List<MealTo> getBetweenHalfOpen(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                           @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate,endDate,startTime,endTime,userId);

        List<Meal> mealsDateFilter = service.getBetweenHalfOpen(startDate, endDate, userId);
        // преобразование списка еды в MealТo
        return MealsUtil.getFilteredTos(mealsDateFilter, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}