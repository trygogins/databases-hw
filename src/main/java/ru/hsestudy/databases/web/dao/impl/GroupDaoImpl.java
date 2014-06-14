package ru.hsestudy.databases.web.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.hsestudy.databases.web.dao.GroupDao;
import ru.hsestudy.databases.web.model.Group;

import java.util.List;

/**
 * @author georgii
 * @since 6/14/14
 */
@Service
public class GroupDaoImpl implements GroupDao {

    @Autowired
    private JdbcTemplate template;

    @Override
    public List<Group> getGroups() {
        // TODO:
        return template.query("select * from groups limit 20", new BeanPropertyRowMapper<>(Group.class));
    }

    @Override
    public void saveGroup(Group group) {
        template.update("replace into groups(screen_name, name) values(?, ?)", group.getScreenName(), group.getName());
    }
}
