package ru.hsestudy.databases.web.dao;

import ru.hsestudy.databases.web.model.Pair;
import ru.hsestudy.databases.web.model.User;

import java.util.List;

/**
 * @author georgii
 * @since 6/14/14
 */
public interface UserDao {

    List<User> getUsersByGroup(Long groupId);
    Pair getRandomPair(Long groupId);
    void increaseRating(Long userId, Long groupId);

}
