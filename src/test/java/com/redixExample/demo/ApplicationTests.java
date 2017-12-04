package com.redixExample.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.redixExample.demo.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTests {

	@Test
	public void contextLoads() {
	       ApplicationContext context = new ClassPathXmlApplicationContext("appCtx.xml");
	        UserService userService= (UserService) context.getBean("userServiceImpl");
	        System.out.println(userService.findById(5l));
	        System.out.println(userService.findById(5l));
	        System.out.println(userService.findById(5l));
	        System.out.println(userService.findById(5l));
	        System.out.println(userService.findById(5l));
	}

}
