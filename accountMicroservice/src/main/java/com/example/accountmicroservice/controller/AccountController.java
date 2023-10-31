package com.example.accountmicroservice.controller;


import com.example.accountmicroservice.exception.AccountException;
import com.example.accountmicroservice.pojo.Account;
import com.example.accountmicroservice.service.AccountService;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

@RestController
@RefreshScope
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Value("${user.message}")
    String message;
    private static final Logger logger = Logger.getLogger(AccountController.class);

    public AccountController() {
    }

    public AccountController(AccountService accountService) {

        this.accountService = accountService;
    }

    @GetMapping("/endpoint")
    public String retrieveLimits(){
        return message;
    }

    @RequestMapping(value = "/elk")
    public String helloWorld() {
        String response = "Welcome to JavaInUse" + new Date();
        logger.log(Level.INFO, response);

        return response;
    }

    @RequestMapping(value ="/account/addAccount", method = RequestMethod.POST)
    public ResponseEntity<String> addAccount(@RequestBody Account account){
        try {
            Account response = accountService.addAccount(account);

            if (response != null) {
                return ResponseEntity.ok("Add Account successfully");
            } else {
                throw new AccountException("Add Account not successfully");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add account");
        }
    }

    @GetMapping("/account")
    public List getAccount() throws Exception {
        try {
            List accounts = accountService.getAccount();
            logger.info("get list of account success");
            System.out.println(accounts);
            return accounts;
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @RequestMapping(value ="/account/updateAccount", method = RequestMethod.PUT)
    public ResponseEntity<String> updateAccount(@RequestBody Account account){
        try {
            Account response = accountService.updateAccount(account);

            if (response != null) {
                return ResponseEntity.ok("update Account successfully");
            } else {
                logger.warn("update account into database not success check input");
                throw new AccountException("update Account not successfully");
            }
        } catch (AccountException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update account");
        }
    }

    @DeleteMapping("/account/deleteAccount")
    public ResponseEntity<String> deleteAccount(@RequestBody Account account){
        try {
            boolean response = accountService.deleteAccount(account);

            if (response) {
                return ResponseEntity.ok("Delete Account successfully");
            } else {
                throw new AccountException("Delete Account not successfully");
            }
        } catch (AccountException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete account");
        }
    }

    @GetMapping("/account/getAccountById/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable("accountId") int accountId) throws AccountException {
        try{
            Account account = accountService.getFindById(accountId);
            logger.info("get account by account_id success");
            return ResponseEntity.ok(account);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new AccountException(e.getMessage());
        }
    }

    @GetMapping("/circuit-breaker") public String serviceB() {
        return "Service B is called from Service A";
    }
}
