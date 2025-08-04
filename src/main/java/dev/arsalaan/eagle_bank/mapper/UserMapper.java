package dev.arsalaan.eagle_bank.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import dev.arsalaan.eagle_bank.dto.UserRequest;
import dev.arsalaan.eagle_bank.dto.UserResponse;
import dev.arsalaan.eagle_bank.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserResponse toUserResponse(User user);

  List<UserResponse> toUserResponseList(List<User> users);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "password", ignore = true)
  User toUser(UserRequest userRequest);

}