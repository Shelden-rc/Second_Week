package by.Shelden.api;

import by.Shelden.dto.UserDto;
import by.Shelden.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/User_Service")
public class UserApi {

    private static final Logger log = LoggerFactory.getLogger(UserApi.class);

    private final UserService userService;

    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create_user")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userToCreate){
        log.info("Called method createUser");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userToCreate));
    }

    @PostMapping("/update_user/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserDto userToUpdate){
        log.info("Called method updateUser");

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUser(id, userToUpdate));
    }

    @GetMapping("/get_user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id){
        log.info("Called method getUser");

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUser(id));
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        log.info("Called method getAllUsers");

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id){
        log.info("Called method deleteUser");

        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
