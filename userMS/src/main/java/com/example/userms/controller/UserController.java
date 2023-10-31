package com.example.userms.controller;


import com.example.userms.pojo.Account;
import com.example.userms.pojo.Summary;
import com.example.userms.pojo.User;
import com.example.userms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;



    public UserController() {
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @RequestMapping(value ="/user/addUser", method = RequestMethod.POST)
    public boolean addParcel(@RequestBody User user){
        try {
            userService.addUser(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @RequestMapping(value ="/user/updateUser", method = RequestMethod.PUT)
    public boolean updateUser(@RequestBody User user){
        try {
            userService.updateUser(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @GetMapping("/user")
    public List<User> getUser(){
        return userService.getUser();
    }

    @DeleteMapping("/user/deleteUser")
    public boolean deleteUser(@RequestBody User user){
        try {
            userService.deleteUser(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    @GetMapping("/user/deposit/summary1")
    public Summary getSummary1(){
        List<User> userList = userService.getUser();
        List<Account> accounts = WebClient.create()
                .get()
                .uri("http://localhost:8080/account")
                        .retrieve()
                        .bodyToMono(List.class)
                        .block();
        return new Summary(userList, accounts);
    }



}
