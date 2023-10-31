package com.example.accountmicroservice.service;


import com.example.accountmicroservice.controller.AccountController;
import com.example.accountmicroservice.exception.AccountException;
import com.example.accountmicroservice.pojo.Account;
import com.example.accountmicroservice.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    private static final Logger logger = Logger.getLogger(AccountService.class);

    @Cacheable("accountList")
    public List getAccount() throws JsonProcessingException {
        List resp = accountRepository.findAll();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resp);
        System.out.println(json);
        if(resp.isEmpty()){
            return null;
        }

        return objectMapper.readValue(json, new TypeReference<List>() {});
    }

//    @CacheEvict(value = "account", allEntries = true)
//    @CachePut(value = "account", key = "#account.account_id")
    @Caching(evict = { @CacheEvict(value = "accountList", allEntries = true), },
            put = { @CachePut(value = "account", key = "#account.account_id") })
    public Account addAccount(Account account) throws Exception {
        if (account.getAccount_number().isEmpty()|| account.getAccount_type().isEmpty()){
            throw new AccountException("Data must not be empty.");
        }
        try {
            accountRepository.save(account);
            logger.info("Account add into database success");
            return account;
        }catch (Exception e){
            logger.error("Data wrong or database can't connect : "+ e.getMessage());
            return null;
        }
    }

    @Caching(evict = { @CacheEvict(value = "accountList", allEntries = true), },
            put = { @CachePut(value = "account", key = "#account.account_id") })
    public Account updateAccount(Account account) throws Exception {
        if (account.getAccount_number().isEmpty()|| account.getAccount_type().isEmpty()){
            throw new AccountException("Data must not be empty.");
        }

        Account accountRes = accountRepository.findByAccount_id(account.getAccount_id());
        if (accountRes == null) {
            throw new AccountException("Account_id have not in the database");
        }

        try {
            accountRepository.save(account);
            logger.info("update account success");
            return account;
        } catch (Exception e) {
            logger.error("Data wrong or database can't connect : "+ e.getMessage());
            return null;
        }
    }

//    @CacheEvict(value = "account", key = "#account.account_id")
        @Caching(evict = { @CacheEvict(value = "accountList", allEntries = true),

        @CacheEvict(value = "account", key = "#account.account_id"), })
    public boolean deleteAccount(Account account) throws Exception{
        if (account.getAccount_number().isEmpty()|| account.getAccount_type().isEmpty()){
            throw new AccountException("Data must not be empty.");
        }

        Account accountRes = accountRepository.findByAccount_id(account.getAccount_id());
        if (accountRes == null) {
            throw new AccountException("Account_id have not in the database");
        }

        try {
            accountRepository.delete(account);
            logger.info("delete account from database success");
            return true;
        } catch (Exception e) {
            logger.info("database fail : " + e.getMessage());
            return false;
        }
    }

    @Cacheable(value = "account", key = "#account_id", unless = "#result==null")
    public Account getFindById(int account_id) throws AccountException {
        Account account = accountRepository.findByAccount_id(account_id);
        if (account == null) {
            throw new AccountException("account_id is not found in the database");
        }
        return account;
    }
}
