package org.example.userservice.service.impl;

import lombok.AllArgsConstructor;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.example.userservice.service.KafkaProducerService;
import org.example.userservice.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public User findById(final UUID id) {

        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

    }

    @Override
    public List<User> findAll() {

        return userRepository.findAll();
    }

    @Override
    public User create(final User user) {

        User savedUser = userRepository.save(user);

        kafkaProducerService.sendMessage(savedUser.getId());

        return savedUser;
    }

    @Override
    public User update(final User user) {

        return userRepository.save(user);
    }

    @Override
    public void delete(final User user) {


    }
}
