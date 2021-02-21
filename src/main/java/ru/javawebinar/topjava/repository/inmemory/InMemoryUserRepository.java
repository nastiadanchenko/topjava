package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {

    private final Map<Integer, User> usersMap = new ConcurrentHashMap<>();
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            usersMap.put(user.getId(), user);
            return user;
        }
        return usersMap.computeIfPresent(user.getId(), (id, oldId) -> user);
    }

    @Override
    public boolean delete(int id) {
        return usersMap.remove(id) != null;
    }

    @Override
    public User get(int id) {
        return usersMap.get(id);
    }

    @Override
    public List<User> getAll() {
        // сама
        /*List<User> usersAllList = new ArrayList<>(Collections.emptyList());
        usersAllList.addAll(repository.values());
        // сортирую список с помощью компаратора, сравнивая имена обьектов User, если они одинаковы сравниваем Email'ы
        usersAllList.sort(Comparator.comparing(User::getName).thenComparing(User::getEmail));
        return usersAllList;*/

        // подсмотрела
        // звмена usersMap.values() на getCollection() из клвсса InMemoryBaseRepository
        return getCollection()
                .stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());

    }

    @Override
    public User getByEmail(String email) {
        // сама
        /*for (Map.Entry<Integer,User> pair:repository.entrySet()
             ) {
            User user = pair.getValue();
            if (email.equals(user.getEmail())) return user;
        }
        return null;*/
        // подсмотрела
        // // звмена usersMap.values() на getCollection()
        return getCollection()
                .stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst()
                .orElse(null);
    }
}
