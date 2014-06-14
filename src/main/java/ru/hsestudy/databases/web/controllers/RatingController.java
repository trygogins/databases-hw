package ru.hsestudy.databases.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
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

    @RequestMapping(value = "/rating/{group_id}", method = RequestMethod.GET)
    public ModelAndView getRating(@PathVariable("group_id") Long groupId) {
        ModelAndView model = new ModelAndView("rating");
        model.addObject("groupId", groupId);
        model.addObject("users", userDao.getUsersByGroup(groupId));

        return model;
    }

}
