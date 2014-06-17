package ru.hsestudy.databases.web.services;

import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * @author georgii
 * @since 6/13/14
 */
@Service
public class PersonFetchService {

    private static final Logger logger = LoggerFactory.getLogger(PersonFetchService.class);

    public static final String VK_USER_API = "http://api.vk.com/method/users.get?uids=${userId}&fields=photo_big,photo_400_orig,sex";
    public static final String VK_GROUP_API = "http://api.vk.com/method/groups.getMembers?gid=${groupId}";
    public static final String VK_GROUP_INFO_API = "http://api.vk.com/method/groups.getById?gid=${groupId}";

    @Autowired
    private JdbcTemplate template;

    /**
     * Метод сохраняет участников указанной группы в базу
     * @param groupId - айдишник группы Вконтакте
     */
    public Long fetchPeople(final String groupId) {
        try {
            JSONObject response = getResponse(VK_GROUP_API.replace("${groupId}", groupId));
            if (response.has("error")) {
                response = getResponse(VK_GROUP_API.replace("${groupId}", groupId.replace("club", "")));
            }
            JSONArray userIds = response.getJSONObject("response").getJSONArray("users");

            // вставка группы в базу
            Long groupLongId = getGroupId(groupId);
            if (groupLongId < 0) {
                return -1l;
            }

            // формируем список idшников для запроса к API пользователей
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < userIds.length(); i++) {
                sb.append(String.valueOf(userIds.get(i))).append(",");
            }
            sb.setLength(sb.length() - 1);
            saveUser(sb.toString(), ImmutableMap.<String, Object>of("sex", 2), groupLongId);

            return groupLongId;
        } catch (JSONException e) {
            logger.error("unable to obtain users array - incorrect JSON structure", e);
            return -1l;
        }
    }

    private String getGroupName(String groupId) {
        try {
            JSONObject response = getResponse(VK_GROUP_INFO_API.replace("${groupId}", groupId));

            return String.valueOf(response.getJSONArray("response").getJSONObject(0).get("name")).replaceAll("[^A-Za-z0-9а-яА-Я ]+", "");
        } catch (JSONException e) {
            logger.error("unable to parse group info", e);
            return "empty";
        }
    }

    private Long getGroupId(final String groupId) {
        Long groupLongId;
        try {
            groupLongId = template.queryForObject("select id from groups where screen_name = ?", Long.class, groupId);
            logger.info("group already exists with id {}", groupLongId);

            return -1l;
        } catch (EmptyResultDataAccessException ignored) {}

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "insert into groups(name, screen_name) values('"
                        + getGroupName(groupId) + "','" + groupId + "')";

                return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            }
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    /**
     * Сохранение пользователей с указанными id, удовлетворяющих условию
     *
     * @param userIds - айдишник пользователя для сохранения
     * @param groupId - айдишник группы Вконтакте
     * @param conditions - мапа, в которой ключ - поле json-ответа,
     *                  при равенстве которого указанному значению пользователь будет сохранён в базу
     */
    private void saveUser(String userIds, Map<String, Object> conditions, Long groupId) throws JSONException {
        JSONObject response = getResponse(VK_USER_API.replace("${userId}", userIds));
        JSONArray users = response.getJSONArray("response");

        StringBuilder sb = new StringBuilder("replace into user values ");

        for (int i = 0; i < users.length(); i++) {
            JSONObject userInfo = users.getJSONObject(i);

            if (isValid(userInfo, conditions)) {
                Integer uid = (Integer) userInfo.get("uid");

                template.update("insert into rating values (?,?,0)", uid, groupId);

                Object photo;
                try {
                    photo = userInfo.get("photo_400_orig");
                } catch (JSONException e) {
                    photo = userInfo.get("photo_big");
                }
                sb.append("(").append(uid).append(",'").append(userInfo.get("first_name"))
                        .append("','").append(userInfo.get("last_name")).append("','")
                        .append(photo).append("'),");
            }
        }
        sb.setLength(sb.length() - 1);

        if (sb.toString().endsWith("values")) {
            logger.warn("no users to insert!");
        } else {
            int inserted = template.update(sb.toString());
            logger.info("{} users inserted", inserted);
        }
    }

    private boolean isValid(JSONObject userInfo, Map<String, Object> conditions) throws JSONException {
        for (String key : conditions.keySet()) {
            if (!userInfo.get(key).equals(conditions.get(key))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Метод получения ответа от VK.API
     * @param url - url для получения ответа
     * @return HTTP-ответ в форме JSONObject
     */
    public JSONObject getResponse(String url) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String resp = EntityUtils.toString(entity, "UTF-8");
            return new JSONObject(resp);

        } catch (IOException e) {
            logger.error("IO Exception", e);
        } catch (JSONException e) {
            logger.error("unable to convert response into JSON", e);
        }
        return new JSONObject();
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        PersonFetchService fetchService = context.getBean(PersonFetchService.class);
        fetchService.fetchPeople("pihse");
    }

}
