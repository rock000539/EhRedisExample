package com.redixExample.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redixExample.demo.dao.DBConnector;

@RestController
@RequestMapping("/task")
public class MvcController {

    @Autowired 
    private DBConnector connector ;

    @RequestMapping(value = {"/",""})
    public String hellTask(){

        connector.configure();    
        return "hello task !!";
    }
    
    @RequestMapping(value = {"/find"})
    public String findItem(@RequestParam String id){
    	
        return "hello id is =="+id;
    }
    
}