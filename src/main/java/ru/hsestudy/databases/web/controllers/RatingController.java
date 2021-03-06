package ru.hsestudy.databases.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.hsestudy.databases.web.dao.GroupDao;
import ru.hsestudy.databases.web.dao.UserDao;

/**
 * @author georgii
 * @since 6/14/14
 */
@Controller
@RequestMapping("/")
public class RatingController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    @RequestMapping(value = "/rating/{group_id}", method = RequestMethod.GET)
    public ModelAndView getRating(@PathVariable("group_id") Long groupId) {
        if (!groupDao.exists(groupId)) {
            return new ModelAndView("redirect:/groups");
        }
        ModelAndView model = new ModelAndView("rating");
        model.addObject("group", groupDao.getGroupInfo(groupId));
        model.addObject("users", userDao.getUsersByGroup(groupId));

        return model;
    }

}
