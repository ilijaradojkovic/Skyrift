package bees.io.Berzza.service;

public interface UserService {

    Double getBalance(String userId);

    void saveUser(String user);

    void addToBalance(String user,Double amount);

    void removeFromBalance(String user,Double amount);


}
