package org.example.userservice.service;


import org.example.userservice.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User findById(UUID id);

    List<User> findAll();

    User create(User user);

    User update(User user);

    void delete(User user);
}
