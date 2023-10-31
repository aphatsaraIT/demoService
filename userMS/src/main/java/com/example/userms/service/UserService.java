package com.example.userms.service;



import com.example.userms.pojo.Summary;
import com.example.userms.pojo.User;
import com.example.userms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> getUser(){
        return userRepository.findAll();
    }
    public boolean addUser(User user){
        try {
            userRepository.save(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean updateUser(User user){
        try {
            userRepository.save(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean deleteUser(User user){
        try {
            userRepository.delete(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
