package com.example.cinema_reservation.service;

import com.example.cinema_reservation.exception.UserAlreadyExistException;
import com.example.cinema_reservation.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    List<User> getAllUsers();
    User registerUser(User user) throws UserAlreadyExistException;
    User registerAdmin(User user) throws UserAlreadyExistException;
    Optional<User> findUserById(Long id);
    Optional<User> findUserByUsername(String username);
    User saveUser(User user);
    boolean userExist(String username);
    boolean deleteUserById(Long id);
    int usersCount();
}
