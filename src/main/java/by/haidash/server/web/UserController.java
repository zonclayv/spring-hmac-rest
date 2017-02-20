package by.haidash.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import by.haidash.server.model.User;
import by.haidash.server.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController (final UserService userService){
		this.userService=userService;
	}

	@ResponseBody
	@RequestMapping(value = "/users",  method = RequestMethod.GET)
	public List<User> getUsers() {
		return userService.getUsers();
	}
}
