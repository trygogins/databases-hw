package ru.hsestudy.databases.web.controllers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.hsestudy.databases.web.dao.GroupDao;
import ru.hsestudy.databases.web.dao.UserDao;
import ru.hsestudy.databases.web.model.Pair;
import ru.hsestudy.databases.web.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author georgii
 * @since 6/14/14
 */
@Controller
public class VoteController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    private Multimap<String, User> userTokens = HashMultimap.create();

    @RequestMapping(value = "/vote", method = RequestMethod.GET)
    public ModelAndView getVotePage(@RequestParam(value = "group_id") Long groupId) {
        if (!groupDao.exists(groupId)) {
            return new ModelAndView("redirect:/groups");
        }
        return new ModelAndView("vote");
    }

    @ResponseBody
    @RequestMapping(value = "/vote.json", method = RequestMethod.GET)
    public Map<String, ?> getCouple(@RequestParam(value = "group_id") Long groupId) {
        Pair randomPair = userDao.getRandomPair(groupId);
        String token = RandomStringUtils.randomAlphanumeric(32);
        if (userTokens.size() >= 100000) {
            userTokens.clear();
        }

        userTokens.put(token, randomPair.getLeft());
        userTokens.put(token, randomPair.getRight());

        return ImmutableMap.of("token", token, "pair", randomPair);
    }

    @ResponseBody
    @RequestMapping(value = "/vote/vote", method = RequestMethod.POST)
    public String increaseRating(@RequestParam("token") String token, @RequestBody User user) throws IOException {
        List<User> pairs = new ArrayList<>(userTokens.removeAll(token));

        if (pairs.indexOf(user) < 0) {
            return "no";
        }
        userDao.increaseRating(user.getId(), user.getGroupId());
        return "ok";
    }

}
