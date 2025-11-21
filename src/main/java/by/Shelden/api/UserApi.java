package by.Shelden.api;

import by.Shelden.dto.UserDto;
import by.Shelden.exeptions.ErrorResponse;
import by.Shelden.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(
        name = "User Service API",
        description = "Управление пользователями: создание, обновление, получение и удаление"
)
@RestController
@RequestMapping("/User_Service")
public class UserApi {

    private static final Logger log = LoggerFactory.getLogger(UserApi.class);

    private final UserService userService;

    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Создание пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Создано",
                    content = @Content(schema = @Schema(implementation = EntityModel.class))),
            @ApiResponse(responseCode = "409", description = "Email уже существует",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/create_user")
    public ResponseEntity<EntityModel<UserDto>> createUser(@Valid @RequestBody UserDto userToCreate) {
        log.info("Called method createUser");
        UserDto created = userService.createUser(userToCreate);

        EntityModel<UserDto> resource = EntityModel.of(created,
                linkTo(methodOn(UserApi.class).getUser(created.id())).withSelfRel(),
                linkTo(methodOn(UserApi.class).getAllUsers()).withRel("users")
        );

        return ResponseEntity
                .created(linkTo(methodOn(UserApi.class).getUser(created.id())).toUri())
                .body(resource);
    }

    @Operation(summary = "Обновить пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь обновлен",
                    content = @Content(schema = @Schema(implementation = EntityModel.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/update_user/{id}")
    public ResponseEntity<EntityModel<UserDto>> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userToUpdate) {
        log.info("Called method updateUser");
        UserDto updated = userService.updateUser(id, userToUpdate);

        EntityModel<UserDto> resource = EntityModel.of(updated,
                linkTo(methodOn(UserApi.class).getUser(id)).withSelfRel()
        );

        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(schema = @Schema(implementation = EntityModel.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/get_user/{id}")
    public ResponseEntity<EntityModel<UserDto>> getUser(@PathVariable Long id) {
        log.info("Called method getUser");
        UserDto user = userService.getUser(id);

        EntityModel<UserDto> resource = EntityModel.of(user,
                linkTo(methodOn(UserApi.class).getUser(id)).withSelfRel(),
                linkTo(methodOn(UserApi.class).updateUser(id, null)).withRel("update").withType("POST"),
                linkTo(methodOn(UserApi.class).deleteUser(id)).withRel("delete").withType("DELETE"),
                linkTo(methodOn(UserApi.class).getAllUsers()).withRel("users")
        );

        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Получить всех пользователей")
    @GetMapping("/get_all")
    public ResponseEntity<CollectionModel<EntityModel<UserDto>>> getAllUsers() {
        log.info("Called method getAllUsers");
        List<EntityModel<UserDto>> users = userService.getAllUsers().stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserApi.class).getUser(user.id())).withSelfRel()))
                .toList();

        CollectionModel<EntityModel<UserDto>> collection = CollectionModel.of(users,
                linkTo(methodOn(UserApi.class).getAllUsers()).withSelfRel(),
                linkTo(methodOn(UserApi.class).createUser(null)).withRel("create").withType("POST")
        );

        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Удалить пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным ID не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Called method deleteUser");
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
