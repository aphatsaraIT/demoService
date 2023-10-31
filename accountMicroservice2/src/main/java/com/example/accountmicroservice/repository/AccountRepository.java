package com.example.accountmicroservice.repository;


import com.example.accountmicroservice.pojo.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query(value="SELECT * FROM account_flyway2 u WHERE u.account_id = ?1", nativeQuery = true)
    Account findByAccount_id(int account_id);
}
