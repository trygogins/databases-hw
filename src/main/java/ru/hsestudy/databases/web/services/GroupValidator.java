package ru.hsestudy.databases.web.services;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author georgii
 * @since 6/14/14
 */
@Service
public class GroupValidator {

    private static final Logger logger = LoggerFactory.getLogger(GroupValidator.class);

    @Autowired PersonFetchService fetchService;

    /**
     * Осуществляет проверку корректности указанного URL - должна быть страница группы
     */
    public Boolean isGroup(String url) {
        JSONObject response = fetchService.getResponse(url);
        try {
            // если нет поля error - группа
            return response.get("error") == null;
        } catch (JSONException e) {
            logger.info("error parsing json from {}", url);
            return false;
        }
    }

    public String getScreenName(String url) {
        return url.substring(url.lastIndexOf("/"));
    }

}
