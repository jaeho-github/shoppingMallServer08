package com.model2.mvc.web.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserService;


//==> 회원관리 RestController
@RestController
@RequestMapping("/app/user/*")
public class UserRestController {
	
	///Field
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	//setter Method 구현 않음
		
	public UserRestController(){
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	@RequestMapping( value="addUser", method=RequestMethod.POST )
	public String addUser( @RequestBody User user ) throws Exception {
		//여기 하는중

		System.out.println("/app/user/addUser : POST");
		
		userService.addUser(user);
		
		return "redirect:/user/loginView.jsp";
	}
	
	@RequestMapping( value="getUser/{userId}", method=RequestMethod.GET )
	public User getUser( @PathVariable String userId ) throws Exception{
		
		System.out.println("/app/user/getUser : GET");
		System.out.println(userService.getUser(userId));
		
		//Business Logic
		return userService.getUser(userId);
	}

	@RequestMapping( value="login", method=RequestMethod.POST )
	public User login(	@RequestBody User user,
									HttpSession session ) throws Exception{
	
		System.out.println("/app/user/login : POST");
		//Business Logic
		System.out.println("::"+user);
		User dbUser=userService.getUser(user.getUserId());
		
		// session 쓰이진 않지만 미래의 무언가를 위해 남겨놓음
		if( user.getPassword().equals(dbUser.getPassword())){
			session.setAttribute("user", dbUser);
		}
		
		return dbUser;
	}
	
	@RequestMapping( value="updateUser/{userId}", method=RequestMethod.GET )
	public User updateUser( @PathVariable String userId , Model model ) throws Exception{

		System.out.println("/app/user/updateUser : GET");
		
		return userService.getUser(userId);
	}
	
	@RequestMapping( value="checkDuplication", method=RequestMethod.POST )
	public Map checkDuplication( @RequestParam("userId") String userId , Model model ) throws Exception{
		
		System.out.println("/app/user/checkDuplication : POST");
		
		boolean result=userService.checkDuplication(userId);
		
		Map map = new HashMap();
		map.put("result", result);
		map.put("userId", userId);

		return map;
	}
	
	
	
	// UserService.java 확인, 뭐만들지 결정
}