package com.testsigma.api_bed_test.mapper;

import com.testsigma.api_bed_test.dto.UserDTO;
import com.testsigma.api_bed_test.model.User;
import com.testsigma.api_bed_test.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

    UserDTO map(User user);

    List<UserDTO> map(List<User> userList);

    User map(UserRequest userRequest);

}

