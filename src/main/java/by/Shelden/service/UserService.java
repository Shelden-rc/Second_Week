package by.Shelden.service;

import by.Shelden.dao.Dao;
import by.Shelden.dao.UserDao;
import by.Shelden.entity.User;
import by.Shelden.util.HibernateUtil;

import java.util.List;

public class UserService {
    private final Dao dao = new UserDao();

    public void createUser(String name, String email, int age){
        dao.save(new User(name, email, age));
    }

    public User findUser(Long id){
        return dao.getById(id);
    }

    public List<User> getAllUsers(){
        return dao.getAll();
    }

    public void updateUser(User user){
        dao.update(user);
    }

    public void deleteUser(Long id){
        dao.deleteById(id);
    }

    public void shutDown(){
        HibernateUtil.shutdown();
    }
}
