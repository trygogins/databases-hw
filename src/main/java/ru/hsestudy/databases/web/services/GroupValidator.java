package ru.hsestudy.databases.web.services;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(new HttpGet(url));
            String responseBody = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

            return responseBody.contains("fm=group");
        } catch (IOException e) {
            logger.error("error fetching group", e);
            return false;
        }
    }

    public String getScreenName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

}
