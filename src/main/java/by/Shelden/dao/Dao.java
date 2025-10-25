package by.Shelden.dao;

import by.Shelden.entity.User;
import java.util.List;

public interface Dao {
    void save(User user);
    User getById(Long id);
    List<User> getAll();
    void update(User user);
    void deleteById(Long id);
}
