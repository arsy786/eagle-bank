package dev.arsalaan.eagle_bank.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import dev.arsalaan.eagle_bank.dto.AccountRequest;
import dev.arsalaan.eagle_bank.dto.AccountResponse;
import dev.arsalaan.eagle_bank.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

  AccountResponse toAccountResponse(Account account);

  List<AccountResponse> toAccountResponseList(List<Account> accounts);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "accountNumber", ignore = true)
  @Mapping(target = "balance", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Account toAccount(AccountRequest accountRequest);

}