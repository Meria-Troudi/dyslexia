package services;
import entities.*;

import java.sql.ResultSet;
import java.util.List;

public interface UserService {
    public  void createUser(User u);
    public void removeUSER(int id);
   public void updateUser(User u);
   public List<User> getAllUsers();

   // public User getUserById(int id);
   // public User getUserByEmail(String email);

}
