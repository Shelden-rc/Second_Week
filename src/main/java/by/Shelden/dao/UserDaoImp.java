package by.Shelden.dao;

import by.Shelden.entity.UserEntity;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDaoImp implements UserDao{

    private static final Logger log = LoggerFactory.getLogger(UserDaoImp.class);

    private final UserRepository userRepository;

    public UserDaoImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity save(UserEntity userEntity){
        log.info("Try to save Entity");

        userRepository.save(userEntity);
        return userEntity;
    }

    public UserEntity getById(Long id){
        log.info("Try to get Entity");

        return userRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(
                "Not found reservation by id = " + id
        ));
    }

    public List<UserEntity> getAll(){
        log.info("Try to getAll");

        return userRepository.findAll();
    }

    public UserEntity update(Long id, UserEntity userEntity){
        log.info("Try to update");

        var entityToUpdate = userRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(
                        "Not found reservation by id = " + id
                ));
        entityToUpdate.setName(userEntity.getName());
        entityToUpdate.setEmail(userEntity.getEmail());
        entityToUpdate.setAge(userEntity.getAge());

        userRepository.save(entityToUpdate);
        return entityToUpdate;
    }

    public void deleteById(Long id){
        log.info("Try to delete");

        var entityToDelete = userRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(
                        "Not found reservation by id = " + id
                ));

        userRepository.delete(entityToDelete);
    }
}
