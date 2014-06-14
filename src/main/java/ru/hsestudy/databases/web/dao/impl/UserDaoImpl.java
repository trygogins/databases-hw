package ru.hsestudy.databases.web.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.hsestudy.databases.web.dao.UserDao;
import ru.hsestudy.databases.web.model.User;

import java.util.List;

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
        return template.query("select * from rating r join user r on r.user_id = u.id where r.group_id = ?",
                new BeanPropertyRowMapper<>(User.class), groupId);
    }
}
