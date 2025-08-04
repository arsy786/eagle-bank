package dev.arsalaan.eagle_bank.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import dev.arsalaan.eagle_bank.dto.TransactionRequest;
import dev.arsalaan.eagle_bank.dto.TransactionResponse;
import dev.arsalaan.eagle_bank.model.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

  TransactionResponse toTransactionResponse(Transaction transaction);

  List<TransactionResponse> toTransactionResponseList(List<Transaction> transactions);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "account", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Transaction toTransaction(TransactionRequest transactionRequest);

}
