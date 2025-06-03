package bees.io.Berzza.service.impl;

import bees.io.Berzza.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    Map<String, Double> users = new HashMap<>();
    @Override
    public Double getBalance(String userId) {

        return users.getOrDefault(userId,null);
    }

    @Override
    public void saveUser(String user) {
        users.put(user, 1000.0);
        System.out.println(users);

    }

    @Override
    public void addToBalance(String user, Double amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        users.put(user, users.getOrDefault(user, 0.0) + amount);
    }

    @Override
    public void removeFromBalance(String user, Double amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        double currentBalance = users.getOrDefault(user, 0.0);

        if (currentBalance < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        users.put(user, currentBalance - amount);
    }




}
