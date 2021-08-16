package com.fkoc.bankingmanagement.repository;

import com.fkoc.bankingmanagement.entity.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findByRefTransactionId(String refTransactionId);

}

