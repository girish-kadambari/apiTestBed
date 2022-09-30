package com.testsigma.api_bed_test.controller;

import com.testsigma.api_bed_test.Execption.UserNotFound;
import com.testsigma.api_bed_test.constants.URLConstants;
import com.testsigma.api_bed_test.dto.UserDTO;
import com.testsigma.api_bed_test.mapper.UserMapper;
import com.testsigma.api_bed_test.model.User;
import com.testsigma.api_bed_test.request.UserRequest;
import com.testsigma.api_bed_test.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping(value = URLConstants.AUTH_TYPE + "/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersController {

    private final UsersService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDTO> index(){
        List<User> userList = userService.findAll();
        List<UserDTO> userDTOList = userMapper.map(userList);
        return userDTOList;
    }

    @GetMapping("/{id}")
    public UserDTO show(@PathVariable("id") Long id) throws UserNotFound {
        User user = userService.findById(id);
        UserDTO userDTO = userMapper.map(user);
        return userDTO;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody UserRequest request) throws Exception {
        User user = userMapper.map(request);
        userService.create(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void update(){
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(){
    }


}
