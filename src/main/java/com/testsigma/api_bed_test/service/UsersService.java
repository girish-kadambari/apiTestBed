package com.testsigma.api_bed_test.service;

import com.testsigma.api_bed_test.Execption.UserNotFound;
import com.testsigma.api_bed_test.model.User;
import com.testsigma.api_bed_test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor(onConstructor = @__({@Autowired, @Lazy}))
@Transactional
public class UsersService {
    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) throws UserNotFound {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow();
    }

    public void create(User user){
        userRepository.save(user);
    }
}
