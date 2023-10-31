package com.example.accountmicroservice.service;

import com.example.accountmicroservice.exception.AccountException;
import com.example.accountmicroservice.pojo.Account;
import com.example.accountmicroservice.repository.AccountRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class AccountServiceTest {
    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void addAccountTest_success() throws Exception {
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("deposit");

        doReturn(account).when(accountRepository).save(account);

        Account result = accountService.addAccount(account);

        verify(accountRepository, times(1)).save(account);
        assertEquals(account, result);
    }

    @Test
    public void addAccountTest_cannot_number_empty_failed() {
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("");
        account.setAccount_type("Fixed Deposit Account");

        try {
            accountService.addAccount(account);
            Assert.fail();
        } catch (AccountException e) {
            assertEquals("Data must not be empty.", e.getMessage());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        verify(accountRepository, never()).save(account);
    }

    @Test
    public void addAccountTest_cannot_type_empty_failed() {
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("");

        try {
            accountService.addAccount(account);
            Assert.fail();
        } catch (AccountException e) {
            assertEquals("Data must not be empty.", e.getMessage());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        verify(accountRepository, never()).save(account);
    }

    @Test
    public void addAccountTest_cannot_connect_db_failed() throws Exception {
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("deposit");

        doThrow(new RuntimeException("Database connection failed")).when(accountRepository).save(account);

        Account result = accountService.addAccount(account);

        verify(accountRepository, times(1)).save(account);
        assertEquals(null, result);
    }

    @Test
    public void getAccountTest_success() throws Exception {
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("deposit");

        Account account2= new Account();
        account2.setAccount_id(2);
        account2.setAccount_number("0381732924");
        account2.setAccount_type("deposit");

        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(account2);

        doReturn(accounts).when(accountRepository).findAll();

        List<Account> data = accountService.getAccount();

        verify(accountRepository, times(1)).findAll();

        assertEquals(accounts.size(), data.size());
        assertEquals(accounts.get(0).getAccount_id(), data.get(0).getAccount_id());
        assertEquals(accounts.get(0).getAccount_number(), data.get(0).getAccount_number());
        assertEquals(accounts.get(0).getAccount_type(), data.get(0).getAccount_type());

        assertEquals(accounts.get(1).getAccount_id(), data.get(1).getAccount_id());
        assertEquals(accounts.get(1).getAccount_number(), data.get(1).getAccount_number());
        assertEquals(accounts.get(1).getAccount_type(), data.get(1).getAccount_type());
    }

    @Test
    public void updateAccountTest_success() throws Exception {
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("deposit");

        Account account2= new Account();
        account2.setAccount_id(2);
        account2.setAccount_number("0381732924");
        account2.setAccount_type("deposit");

        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(account2);

        doReturn(account).when(accountRepository).findByAccount_id(1);
        doReturn(account).when(accountRepository).save(account);

        Account result = accountService.updateAccount(account);

        verify(accountRepository, times(1)).save(account);
        assertEquals(account, result);
    }

    @Test
    public void updateAccountTest_cannot_number_empty_failed() {
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("");
        account.setAccount_type("Fixed Deposit Account");

        try {
            accountService.updateAccount(account);
            Assert.fail();
        } catch (AccountException e) {
            assertEquals("Data must not be empty.", e.getMessage());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        verify(accountRepository, never()).save(account);
    }

    @Test
    public void updateAccountTest_cannot_type_empty_failed() {
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("");

        try {
            accountService.updateAccount(account);
            Assert.fail();
        } catch (AccountException e) {
            assertEquals("Data must not be empty.", e.getMessage());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        verify(accountRepository, never()).save(account);
    }

    @Test
    public void updateAccountTest_db_connection_failed() throws Exception {
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("deposit");

        Account account2= new Account();
        account2.setAccount_id(2);
        account2.setAccount_number("0381732924");
        account2.setAccount_type("deposit");

        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(account2);

        doReturn(account).when(accountRepository).findByAccount_id(1);
        doThrow(new RuntimeException("Database connection failed")).when(accountRepository).save(account);

        Account result = accountService.updateAccount(account);

        verify(accountRepository, times(1)).save(account);
        assertEquals(null, result);
    }

    @Test
    public void updateAccountTest_data_have_not_db_failed() {
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("deposit");

        Account account2= new Account();
        account2.setAccount_id(2);
        account2.setAccount_number("0381732924");
        account2.setAccount_type("deposit");

        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(account2);

        Account account3 = new Account();
        account3.setAccount_id(111);
        account3.setAccount_number("6796846990");
        account3.setAccount_type("deposit");

        try{
            accountService.updateAccount(account3);
            verify(accountRepository, times(1)).save(account);
            Assert.fail();
        } catch (AccountException e){
            assertEquals("Account_id have not in the database", e.getMessage());
        } catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void deleteAccountTest_success() throws Exception {
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("deposit");

        Account account2= new Account();
        account2.setAccount_id(2);
        account2.setAccount_number("0381732924");
        account2.setAccount_type("deposit");

        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(account2);

        doReturn(account).when(accountRepository).findByAccount_id(1);
        doNothing().when(accountRepository).delete(any(Account.class));

        boolean result = accountService.deleteAccount(account);

        verify(accountRepository, times(1)).delete(account);
        assertTrue(result);
    }

    @Test
    public void deleteAccountTest_data_have_not_db_failed() {
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("deposit");

        Account account2= new Account();
        account2.setAccount_id(2);
        account2.setAccount_number("0381732924");
        account2.setAccount_type("deposit");

        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(account2);

        Account account3 = new Account();
        account3.setAccount_id(111);
        account3.setAccount_number("6796846990");
        account3.setAccount_type("deposit");

        try{
            accountService.deleteAccount(account3);
            verify(accountRepository, times(1)).delete(account);
            Assert.fail();
        } catch (AccountException e){
            assertEquals("Account_id have not in the database", e.getMessage());
        } catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }

}
