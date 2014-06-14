package ru.hsestudy.databases.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.hsestudy.databases.web.dao.GroupDao;
import ru.hsestudy.databases.web.services.GroupValidator;
import ru.hsestudy.databases.web.services.PersonFetchService;

@Controller
public class MainPageController {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private PersonFetchService fetchService;

    @Autowired
    private GroupValidator groupValidator;



	@RequestMapping(value = "/groups", method = RequestMethod.GET)
	public ModelAndView showGroups() {
        ModelAndView model = new ModelAndView("index");
        model.addObject("groups", groupDao.getGroups());

        return model;
    }

    @RequestMapping(value = "/groups/add", method = RequestMethod.POST)
    public ModelAndView addGroup(String url) {
        ModelAndView model = new ModelAndView("redirect:/groups");

        if (groupValidator.isGroup(url)) {
            model.addObject("error", "Неверно указан URL группы!");
        }
        // здесь происходит сохранение группы и её участников в базу
        fetchService.fetchPeople(groupValidator.getScreenName(url));
        return model;
    }
}