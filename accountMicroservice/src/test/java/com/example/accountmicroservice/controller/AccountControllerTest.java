package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.exception.AccountException;
import com.example.accountmicroservice.pojo.Account;
import com.example.accountmicroservice.service.AccountService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {
    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void addAccountTest_success() throws Exception{
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("deposit");

        doReturn(account).when(accountService).addAccount(any(Account.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String accountJson = objectMapper.writeValueAsString(account);

        MvcResult result = mvc.perform(post("/account/addAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content().string("Add Account successfully"))
                        .andReturn();

        String response = result.getResponse().getContentAsString();

        verify(accountService, times(1)).addAccount(any(Account.class));
        assertEquals("Add Account successfully", response);
    }

    @Test
    public void addAccountTest_cannot_number_empty_failed() throws Exception{
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("");
        account.setAccount_type("deposit");

        doReturn(null).when(accountService).addAccount(any(Account.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String accountJson = objectMapper.writeValueAsString(account);

        MvcResult result = mvc.perform(post("/account/addAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                        .andExpect(status().isInternalServerError())
                        .andExpect(MockMvcResultMatchers.content().string("Failed to add account"))
                        .andReturn();

        String response = result.getResponse().getContentAsString();

        verify(accountService, times(1)).addAccount(any(Account.class));
        assertEquals("Failed to add account", response);
    }

    @Test
    public void addAccountTest_cannot_type_empty_failed() throws Exception{
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("0381732924");
        account.setAccount_type("");

        doReturn(null).when(accountService).addAccount(any(Account.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String accountJson = objectMapper.writeValueAsString(account);

        MvcResult result = mvc.perform(post("/account/addAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                        .andExpect(status().isInternalServerError())
                        .andExpect(MockMvcResultMatchers.content().string("Failed to add account"))
                        .andReturn();

        String response = result.getResponse().getContentAsString();

        verify(accountService, times(1)).addAccount(any(Account.class));
        assertEquals("Failed to add account", response);
    }

    @Test
    public void addAccountTest_cannot_connect_db_failed() throws Exception {
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("deposit");

        doThrow(new AccountException("Cannot connect to the database")).when(accountService).addAccount(any(Account.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String accountJson = objectMapper.writeValueAsString(account);

        MvcResult result = mvc.perform(post("/account/addAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                        .andExpect(status().isInternalServerError())
                        .andExpect(MockMvcResultMatchers.content().string("Failed to add account"))
                        .andReturn();

        String response = result.getResponse().getContentAsString();

        verify(accountService, times(1)).addAccount(any(Account.class));
        assertEquals("Failed to add account", response);
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

       doReturn(accounts).when(accountService).getAccount();

        MvcResult result = mvc.perform(get("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(accounts.size()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].account_id").value(account.getAccount_id()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].account_number").value(account.getAccount_number()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].account_type").value(account.getAccount_type()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[1].account_id").value(account2.getAccount_id()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[1].account_number").value(account2.getAccount_number()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[1].account_type").value(account2.getAccount_type()))
                        .andReturn();

        String response = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Account> responseAccounts = objectMapper.readValue(response, new TypeReference<>() {});

        verify(accountService, times(1)).getAccount();
        assertEquals(accounts.size(), responseAccounts.size());

        for (int i = 0; i < accounts.size(); i++) {
            assertEquals(accounts.get(i).getAccount_id(), responseAccounts.get(i).getAccount_id());
            assertEquals(accounts.get(i).getAccount_number(), responseAccounts.get(i).getAccount_number());
            assertEquals(accounts.get(i).getAccount_type(), responseAccounts.get(i).getAccount_type());
        }
    }

    @Test
    public void updateAccountTest_success() throws Exception {
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846999");
        account.setAccount_type("deposit");

        doReturn(account).when(accountService).updateAccount(any(Account.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String accountJson = objectMapper.writeValueAsString(account);

        MvcResult result = mvc.perform(put("/account/updateAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content().string("update Account successfully"))
                        .andReturn();

        String response = result.getResponse().getContentAsString();
        System.out.println(response);

        verify(accountService, times(1)).updateAccount(any(Account.class));
        assertEquals("update Account successfully", response);
    }

    @Test
    public void updateAccountTest_cannot_number_empty_failed() throws Exception{
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("");
        account.setAccount_type("deposit");

        doReturn(null).when(accountService).updateAccount(any(Account.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String accountJson = objectMapper.writeValueAsString(account);

        MvcResult result = mvc.perform(put("/account/updateAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                        .andExpect(status().isInternalServerError())
                        .andExpect(MockMvcResultMatchers.content().string("update Account not successfully"))
                        .andReturn();

        String response = result.getResponse().getContentAsString();

        verify(accountService, times(1)).updateAccount(any(Account.class));
        assertEquals("update Account not successfully", response);
    }

    @Test
    public void updateAccountTest_cannot_type_empty_failed() throws Exception{
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("");

        doReturn(null).when(accountService).updateAccount(any(Account.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String accountJson = objectMapper.writeValueAsString(account);

        MvcResult result = mvc.perform(put("/account/updateAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                        .andExpect(status().isInternalServerError())
                        .andExpect(MockMvcResultMatchers.content().string("update Account not successfully"))
                        .andReturn();

        String response = result.getResponse().getContentAsString();

        verify(accountService, times(1)).updateAccount(any(Account.class));
        assertEquals("update Account not successfully", response);
    }

    @Test
    public void updateAccountTest_cannot_connect_db_failed() throws Exception {
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("deposit");

        doThrow(new AccountException("Cannot connect to the database")).when(accountService).updateAccount(any(Account.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String accountJson = objectMapper.writeValueAsString(account);

        MvcResult result = mvc.perform(put("/account/updateAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                        .andExpect(status().isInternalServerError())
                        .andExpect(MockMvcResultMatchers.content().string("Cannot connect to the database"))
                        .andReturn();

        String response = result.getResponse().getContentAsString();

        verify(accountService, times(1)).updateAccount(any(Account.class));
        assertEquals("Cannot connect to the database", response);
    }

    @Test
    public void updateAccountTest_data_have_not_db_failed() throws Exception{
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("deposit");

        doThrow(new AccountException("Account not found in the database")).when(accountService).updateAccount(any(Account.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String accountJson = objectMapper.writeValueAsString(account);

        MvcResult result = mvc.perform(put("/account/updateAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                        .andExpect(status().isInternalServerError())
                        .andExpect(MockMvcResultMatchers.content().string("Account not found in the database"))
                        .andReturn();

        String response = result.getResponse().getContentAsString();

        verify(accountService, times(1)).updateAccount(any(Account.class));
        assertEquals("Account not found in the database", response);
    }

    @Test
    public void deleteAccountTest_success() throws Exception{
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846999");
        account.setAccount_type("deposit");

        doReturn(true).when(accountService).deleteAccount(any(Account.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String accountJson = objectMapper.writeValueAsString(account);

        MvcResult result = mvc.perform(delete("/account/deleteAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content().string("Delete Account successfully"))
                        .andReturn();

        String response = result.getResponse().getContentAsString();

        verify(accountService, times(1)).deleteAccount(any(Account.class));
        assertEquals("Delete Account successfully", response);
    }

    @Test
    public void deleteAccountTest_data_have_not_db_failed() throws Exception{
        Account account = new Account();
        account.setAccount_id(1);
        account.setAccount_number("6796846990");
        account.setAccount_type("deposit");

        doThrow(new AccountException("Account not found in the database")).when(accountService).deleteAccount(any(Account.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String accountJson = objectMapper.writeValueAsString(account);

        MvcResult result = mvc.perform(delete("/account/deleteAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                        .andExpect(status().isInternalServerError())
                        .andExpect(MockMvcResultMatchers.content().string("Account not found in the database"))
                        .andReturn();

        String response = result.getResponse().getContentAsString();

        verify(accountService, times(1)).deleteAccount(any(Account.class));
        assertEquals("Account not found in the database", response);
    }
}
