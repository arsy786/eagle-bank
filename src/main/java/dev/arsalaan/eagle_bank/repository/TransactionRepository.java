package dev.arsalaan.eagle_bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.arsalaan.eagle_bank.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  List<Transaction> findByAccountId(Long accountId);
}