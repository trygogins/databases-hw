package ru.hsestudy.databases.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import ru.hsestudy.databases.web.dao.GroupDao;
import ru.hsestudy.databases.web.services.GroupValidator;
import ru.hsestudy.databases.web.services.PersonFetchService;

@Controller
public class MainPageController {

    public static final String PASSWORD = "thecreators";

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private PersonFetchService fetchService;

    @Autowired
    private GroupValidator groupValidator;

	@RequestMapping(value = {"/", "/groups"}, method = RequestMethod.GET)
	public ModelAndView showGroups() {
        ModelAndView model = new ModelAndView("index");
        model.addObject("groups", groupDao.getGroups());

        return model;
    }

    @ResponseBody
    @RequestMapping(value = "/groups/add", method = RequestMethod.POST)
    public String addGroup(WebRequest webRequest) {
        String url = String.valueOf(webRequest.getParameter("url"));

        if (!groupValidator.isGroup(url)) {
            return "error";
        }
        // здесь происходит сохранение группы и её участников в базу
        Long groupId = fetchService.fetchPeople(groupValidator.getScreenName(url));
        return groupId < 0 ? "error" : groupId == 0 ? "warning" : groupId.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/groups/delete", method = RequestMethod.POST)
    public ModelAndView deleteGroup(WebRequest webRequest) {
        String password = String.valueOf(webRequest.getParameter("password"));
        Long groupId = Long.parseLong(String.valueOf(webRequest.getParameter("groupId")));

        if (PASSWORD.equals(password)) {
            groupDao.deleteGroup(groupId);
        }
        return new ModelAndView("redirect:/");
    }
}