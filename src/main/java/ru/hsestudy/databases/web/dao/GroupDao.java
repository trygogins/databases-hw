package ru.hsestudy.databases.web.dao;

import ru.hsestudy.databases.web.model.Group;

import java.util.List;

/**
 * @author georgii
 * @since 6/14/14
 */
public interface GroupDao {

    Group getGroupInfo(Long groupId);
    List<Group> getGroups();
    void deleteGroup(Long groupId);
    boolean exists(Long groupId);

}
