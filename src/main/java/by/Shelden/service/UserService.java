package by.Shelden.service;

import by.Shelden.dao.UserDao;
import by.Shelden.dao.UserDaoImp;
import by.Shelden.entity.UserEntity;

import java.util.List;

public class UserService {
    private final UserDao dao = new UserDaoImp();

    public void createUser(String name, String email, int age){
        dao.save(new UserEntity(name, email, age));
    }

    public UserEntity findUser(Long id){
        return dao.getById(id);
    }

    public List<UserEntity> getAllUsers(){
        return dao.getAll();
    }

    public void updateUser(UserEntity userEntity){
        dao.update(userEntity);
    }

    public void deleteUser(Long id){
        dao.deleteById(id);
    }

}
