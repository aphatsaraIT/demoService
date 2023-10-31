package com.example.bank.service;

import com.example.bank.pojo.Account;
import com.example.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAccount(){
        return accountRepository.findAll();
    }

    public boolean addAccount(Account account){
        try {
            accountRepository.save(account);
            System.out.println("Service1");
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public boolean updateAccount(Account account){
        try {
            accountRepository.save(account);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean deleteAccount(Account account){
        try {
            accountRepository.delete(account);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
