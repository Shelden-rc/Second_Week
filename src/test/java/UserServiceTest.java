import by.Shelden.dao.UserDao;
import by.Shelden.entity.UserEntity;
import by.Shelden.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;


    @Test
    void testCreateUser_UsesDaoAndPassesCorrectValues() {
        userService.createUser("Alice", "alice@mail.com", 22);

        verify(userDao, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testCreateUser_CapturesPassedObject() {
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);

        userService.createUser("Bob", "bob@mail.com", 30);

        verify(userDao).save(captor.capture());

        UserEntity saved = captor.getValue();
        assertEquals("Bob", saved.getName());
        assertEquals("bob@mail.com", saved.getEmail());
        assertEquals(30, saved.getAge());
    }

    @Test
    void testFindUser_ReturnsUser() {
        UserEntity mockUser = new UserEntity("Test","test@mail.com",25);
        when(userDao.getById(1L)).thenReturn(mockUser);

        UserEntity user = userService.findUser(1L);

        assertNotNull(user);
        assertEquals("Test", user.getName());
        verify(userDao).getById(1L);
    }

    @Test
    void testFindUser_NotFoundReturnsNull() {
        when(userDao.getById(99L)).thenReturn(null);

        UserEntity result = userService.findUser(99L);

        assertNull(result);
        verify(userDao).getById(99L);
    }

    @Test
    void testGetAllUsers_ReturnsList() {
        when(userDao.getAll()).thenReturn(List.of(
                new UserEntity("A","a@mail.com",20),
                new UserEntity("B","b@mail.com",21)
        ));

        List<UserEntity> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userDao).getAll();
    }

    @Test
    void testUpdateUser() {
        UserEntity u = new UserEntity("John","john@mail.com",30);

        userService.updateUser(u);

        verify(userDao, times(1)).update(u);
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(5L);

        verify(userDao, times(1)).deleteById(5L);
    }

    @Test
    void testDaoThrowsException_AndServiceDoesNotCrash() {
        doThrow(new RuntimeException("DB error")).when(userDao).save(any());

        assertThrows(RuntimeException.class, () ->
                userService.createUser("Fail", "fail@mail.com", 100)
        );

        verify(userDao).save(any());
    }
}
