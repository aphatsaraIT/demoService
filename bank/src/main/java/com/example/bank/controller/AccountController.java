package com.example.bank.controller;

import com.example.bank.pojo.Account;
import com.example.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    public AccountController() {
    }

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value ="/account/addAccount", method = RequestMethod.POST)
    public boolean addAccount(@RequestBody Account account){
        try {
            accountService.addAccount(account);
            System.out.println(account);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @GetMapping("/account")
    public List<Account> getAccount(){
        return accountService.getAccount();
    }

    @RequestMapping(value ="/account/updateAccount", method = RequestMethod.PUT)
    public boolean updateAccount(@RequestBody Account account){
        try {
            accountService.updateAccount(account);
            System.out.println(account);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    @DeleteMapping("/account/deleteAccount")
    public boolean deleteAccount(@RequestBody Account account){
        try {
            accountService.deleteAccount(account);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
