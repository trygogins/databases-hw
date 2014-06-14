package ru.hsestudy.databases.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.hsestudy.databases.web.dao.GroupDao;
import ru.hsestudy.databases.web.model.Group;

@Controller
public class MainPageController {

    @Autowired
    private GroupDao groupDao;

	@RequestMapping(value = "/groups", method = RequestMethod.GET)
	public ModelAndView showGroups() {
        ModelAndView model = new ModelAndView("index");
        model.addObject("groups", groupDao.getGroups());

        return model;
    }

    @RequestMapping(value = "/groups/add", method = RequestMethod.POST)
    public ModelAndView addGroup(@ModelAttribute Group group) {
        ModelAndView model = new ModelAndView("redirect:/groups");

        groupDao.saveGroup(group);
        return model;
    }
}