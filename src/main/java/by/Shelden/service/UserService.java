package by.Shelden.service;

import by.Shelden.dao.UserDaoImp;
import by.Shelden.dto.UserDto;
import by.Shelden.util.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDaoImp userDao;
    private final UserMapper mapper;

    public UserService(UserDaoImp userDao, UserMapper mapper) {
        this.userDao = userDao;
        this.mapper = mapper;
    }

    public UserDto createUser(UserDto userToCreate) {
        return mapper.entityToDto(userDao.save(mapper.dtoToEntity(userToCreate)));
    }

    public UserDto getUser(Long id){
        return mapper.entityToDto(userDao.getById(id));
    }

    public List<UserDto> getAllUsers(){
        return userDao.getAll().stream()
                .map(mapper::entityToDto)
                .toList();
    }

    public UserDto updateUser(Long id, UserDto userDto){
        return mapper.entityToDto(userDao.update(id, mapper.dtoToEntity(userDto)));
    }

    public void deleteUser(Long id){
        userDao.deleteById(id);
    }

}
