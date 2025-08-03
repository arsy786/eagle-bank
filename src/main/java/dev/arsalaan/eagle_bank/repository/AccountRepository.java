package dev.arsalaan.eagle_bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.arsalaan.eagle_bank.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

  List<Account> findByUserId(Long userId);

}
