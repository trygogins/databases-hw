package ru.hsestudy.databases.web.dao.impl;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.hsestudy.databases.web.dao.GroupDao;
import ru.hsestudy.databases.web.model.Group;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author georgii
 * @since 6/14/14
 */
@Service
public class GroupDaoImpl implements GroupDao {

    @Autowired
    private JdbcTemplate template;

    @Override
    public Group getGroupInfo(Long groupId) {
        return template.queryForObject("select * from groups where id = ?", new BeanPropertyRowMapper<>(Group.class), groupId);
    }

    @Override
    public List<Group> getGroups() {
        return template.query("select * from groups", new BeanPropertyRowMapper<>(Group.class));
    }

    @Override
    public void deleteGroup(Long groupId) {
        // пользователи, состоящие только в одной группе
        List<Map<String,Object>> singleUsers = template.queryForList("select user_id, count(*) cnt from rating group by 1 having cnt = 1");
        Collection<Long> userIds = Collections2.transform(singleUsers, new Function<Map<String, Object>, Long>() {
            @Override
            public Long apply(Map<String, Object> from) {
                return Long.parseLong(String.valueOf(from.get("user_id")));
            }
        });
        Joiner j = Joiner.on(",");

        template.batchUpdate(new String[]{"delete u from user u join rating r on r.user_id = u.id where group_id = " + groupId +
                " and user_id in (" + j.join(userIds) + ")",
                "delete from rating where group_id = " + groupId,
                "delete from groups where id = " + groupId});
    }

    @Override
    public boolean exists(Long groupId) {
        return template.queryForObject("select count(*) from groups where id = ?", Integer.class, groupId) > 0;
    }
}
