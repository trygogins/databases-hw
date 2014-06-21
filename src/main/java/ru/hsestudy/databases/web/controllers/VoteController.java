package ru.hsestudy.databases.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import ru.hsestudy.databases.web.dao.GroupDao;
import ru.hsestudy.databases.web.dao.UserDao;
import ru.hsestudy.databases.web.model.Pair;

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

    @RequestMapping(value = "/vote", method = RequestMethod.GET)
    public ModelAndView getVotePage(@RequestParam(value = "group_id") Long groupId) {
        if (!groupDao.exists(groupId)) {
            return new ModelAndView("redirect:/groups");
        }
        return new ModelAndView("vote");
    }

    @ResponseBody
    @RequestMapping(value = "/vote.json", method = RequestMethod.GET)
    public Pair getCouple(@RequestParam(value = "group_id") Long groupId) {
        return userDao.getRandomPair(groupId);
    }

    @RequestMapping(value = "/vote/vote", method = RequestMethod.POST)
    public void increaseRating(WebRequest webRequest) {
        userDao.increaseRating(Long.parseLong(String.valueOf(webRequest.getParameter("userId"))),
                Long.parseLong(String.valueOf(webRequest.getParameter("groupId"))));
    }

}
