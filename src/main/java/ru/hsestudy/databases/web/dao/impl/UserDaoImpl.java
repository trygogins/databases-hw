package ru.hsestudy.databases.web.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.hsestudy.databases.web.dao.UserDao;
import ru.hsestudy.databases.web.model.Pair;
import ru.hsestudy.databases.web.model.User;

import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * @author georgii
 * @since 6/14/14
 */
@Service
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate template;

    @Override
    public List<User> getUsersByGroup(Long groupId) {
        return template.query("select * from rating r join user u on r.user_id = u.id where r.group_id = ? order by rating desc",
                new BeanPropertyRowMapper<>(User.class), groupId);
    }

    @Override
    public Pair getRandomPair(Long groupId) {
        List<User> users = getUsersByGroup(groupId);
        if (users instanceof RandomAccess) {
            Collections.shuffle(users);
        }
        if (users.size() < 2) {
            return null;
        }

        return new Pair(users.get(0), users.get(1));
    }

    @Override
    public void increaseRating(Long userId, Long groupId) {
        template.update("update rating set rating = rating + 1 where user_id = ? and group_id = ?", userId, groupId);
    }
}
