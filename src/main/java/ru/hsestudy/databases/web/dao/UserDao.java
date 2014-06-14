package ru.hsestudy.databases.web.dao;

import ru.hsestudy.databases.web.model.User;

import java.util.List;

/**
 * @author georgii
 * @since 6/14/14
 */
public interface UserDao {

    List<User> getUsersByGroup(Long groupId);

}
