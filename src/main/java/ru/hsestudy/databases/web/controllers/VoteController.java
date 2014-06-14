package ru.hsestudy.databases.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.hsestudy.databases.web.dao.UserDao;
import ru.hsestudy.databases.web.model.Pair;

import java.util.Map;

/**
 * @author georgii
 * @since 6/14/14
 */
@Controller
public class VoteController {

    @Autowired
    private UserDao userDao;

    @ResponseBody
    @RequestMapping(value = "/vote.json", method = RequestMethod.GET)
    public Pair getCouple(@RequestParam(value = "group_id") Long groupId) {
        return userDao.getRandomPair(groupId);
    }

    @RequestMapping(value = "/vote/vote", method = RequestMethod.POST)
    public void getCouple(@RequestBody Map<String, Long> idMap) {
        userDao.increaseRating(idMap.get("userId"), idMap.get("groupId"));
    }

}
