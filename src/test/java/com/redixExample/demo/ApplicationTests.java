package com.redixExample.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.redixExample.demo.service.UserService;

import net.sf.ehcache.CacheManager;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTests {
	@Autowired
	private CacheManager cacheManager;
	
	private UserService userService ;

	@Before
	public void setUp() {
		ApplicationContext context = new ClassPathXmlApplicationContext("appCtx.xml");
		userService = (UserService) context.getBean("userServiceImpl");
	}

	@Test
	public void contextLoads() {

		System.out.println(userService.findById(5l));
		System.out.println(userService.findById(5l));
		System.out.println(userService.findById(5l));
		System.out.println(userService.findById(5l));
		System.out.println(userService.findById(5l));
	}

}
