package com.example.userms.pojo;


import lombok.Data;

import java.util.List;

@Data
public class Summary {
    private List<User> userList;
    private List<Account> accounts;

    public Summary() {
    }

    public Summary(List<User> userList, List<Account> accounts) {
        this.userList = userList;
        this.accounts = accounts;
    }
}
