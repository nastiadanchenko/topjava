package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }


    public Meal get(int id, int userId) {
        return repository.get(id, userId);
    }

    public void delete(int id, int userId) {
        repository.delete(id, userId);
    }

    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void update(Meal meal, int userId) {
        repository.save(meal, userId);
    }

    public List<Meal> getBetweenHalfOpen(LocalDate startDate, LocalDate endDate, int userId) {
        return repository.getBetweenHalfOpen(DateTimeUtil.getStartInclusive(startDate), DateTimeUtil.getEndExclusive(endDate), userId);
    }
}