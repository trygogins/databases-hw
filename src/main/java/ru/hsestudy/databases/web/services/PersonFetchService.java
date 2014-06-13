package ru.hsestudy.databases.web.services;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author georgii
 * @since 6/13/14
 */
@Service
public class PersonFetchService {

    private static final Logger logger = LoggerFactory.getLogger(PersonFetchService.class);

    public static final String VK_USER_API = "http://api.vk.com/method/users.get?uids=${userId}&fields=photo_big,sex";
    public static final String VK_GROUP_API = "http://api.vk.com/method/groups.getMembers?gid=${groupId}";

    @Autowired
    private JdbcTemplate template;

    /**
     * Метод сохраняет участников указанной группы в базу
     * @param groupId - айдишник группы Вконтакте
     */
    public void fetchPeople(Long groupId) {
        try {
            JSONObject response = getResponse(VK_GROUP_API.replace("${groupId}", String.valueOf(groupId)));
            JSONArray userIds = response.getJSONObject("response").getJSONArray("users");

            // формируем список idшников для запроса к API пользователей
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < userIds.length(); i++) {
                sb.append(String.valueOf(userIds.get(i))).append(",");
            }
            sb.setLength(sb.length() - 1);
            saveUser(sb.toString(), ImmutableMap.<String, Object>of("sex", 2), groupId);

        } catch (JSONException e) {
            logger.error("unable to obtain users array - incorrect JSON structure", e);
        }
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
        JSONArray userInfos = response.getJSONArray("response");

        StringBuilder sb = new StringBuilder("insert into user values ");

        for (int i = 0; i < userInfos.length(); i++) {
            JSONObject userInfoObject = userInfos.getJSONObject(i);

            if (isValid(userInfoObject, conditions)) {
                sb.append("(").append(userInfoObject.get("uid")).append(",'").append(userInfoObject.get("first_name"))
                        .append("','").append(userInfoObject.get("last_name")).append("','")
                        .append(userInfoObject.get("photo_big")).append("',").append(groupId).append("),");
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
    private JSONObject getResponse(String url) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = client.execute(httpGet);
            return new JSONObject(IOUtils.toString(response.getEntity().getContent()));

        } catch (IOException e) {
            logger.error("IO Exception", e);
        } catch (JSONException e) {
            logger.error("unable to convert response into JSON", e);
        }
        return new JSONObject();
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        PersonFetchService fetchService = context.getBean(PersonFetchService.class);
        fetchService.fetchPeople(29040404l);
    }

}
