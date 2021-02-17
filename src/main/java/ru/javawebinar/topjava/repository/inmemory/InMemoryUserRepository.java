package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private final Map<Integer, User> repository = new ConcurrentHashMap<>();
    private static final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);

        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);

        if (user.getId() == null) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        return repository.computeIfPresent(user.getId(), (id, oldId) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);

        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        // сама
        /*List<User> usersAllList = new ArrayList<>(Collections.emptyList());
        usersAllList.addAll(repository.values());
        // сортирую список с помощью компаратора, сравнивая имена обьектов User, если они одинаковы сравниваем Email'ы
        usersAllList.sort(Comparator.comparing(User::getName).thenComparing(User::getEmail));
        return usersAllList;*/

        // подсмотрела
        return repository
                .values()
                .stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());

    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        // сама
        /*for (Map.Entry<Integer,User> pair:repository.entrySet()
             ) {
            User user = pair.getValue();
            if (email.equals(user.getEmail())) return user;
        }
        return null;*/
        // подсмотрела
        return repository
                .values()
                .stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst()
                .orElse(null);
    }
}
