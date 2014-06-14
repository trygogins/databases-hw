package ru.hsestudy.databases.web.dao;

import ru.hsestudy.databases.web.model.Group;

import java.util.List;

/**
 * @author georgii
 * @since 6/14/14
 */
public interface GroupDao {

    List<Group> getGroups();

}
