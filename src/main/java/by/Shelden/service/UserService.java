package by.Shelden.service;

import by.Shelden.dao.UserDaoImp;
import by.Shelden.directMail.EmailNotificationSender;
import by.Shelden.dto.OperationType;
import by.Shelden.dto.UserDto;
import by.Shelden.dto.UserEvent;
import by.Shelden.util.UserMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private static final String TOPIC = "user-events";

    private final UserDaoImp userDao;
    private final UserMapper mapper;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private final EmailNotificationSender emailService;

    public UserService(UserDaoImp userDao, UserMapper mapper,KafkaTemplate<String, UserEvent> kafkaTemplate, EmailNotificationSender emailService) {
        this.userDao = userDao;
        this.mapper = mapper;
        this.kafkaTemplate = kafkaTemplate;
        this.emailService = emailService;
    }

    public UserDto createUser(UserDto userToCreate) {
        UserDto saved = mapper.entityToDto(userDao.save(mapper.dtoToEntity(userToCreate)));
        emailService.sendDirect(saved.email(), saved.name(), OperationType.CREATE);
        kafkaTemplate.send(TOPIC, saved.email(),new UserEvent(OperationType.CREATE, saved.email()));
        return saved;
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
        UserDto user = mapper.entityToDto(userDao.getById(id));
        userDao.deleteById(id);
        emailService.sendDirect(user.email(), user.name(), OperationType.DELETE);
        kafkaTemplate.send(TOPIC, user.email(), new UserEvent(OperationType.DELETE, user.email()));
    }

}
