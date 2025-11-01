import by.Shelden.dao.UserDaoImp;
import by.Shelden.entity.UserEntity;
import by.Shelden.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers
public class UserDaoImpTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    private static UserDaoImp userDao;

    @BeforeAll
    static void setup() {
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", "test");
        System.setProperty("hibernate.connection.password", "test");
        System.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        userDao = new UserDaoImp();
    }

    @AfterAll
    static void teardown() {
        HibernateUtil.shutdown();
        postgres.stop();
    }

    @Test
    void testSaveAndGetById() {
        UserEntity user = new UserEntity("Test", "test@test.com", 20);
        userDao.save(user);

        UserEntity fetched = userDao.getById(user.getId());
        Assertions.assertNotNull(fetched);
        Assertions.assertEquals("Test", fetched.getName());
    }

    @Test
    void testGetAll() {
        userDao.save(new UserEntity("U1", "u1@test.com", 30));
        userDao.save(new UserEntity("U2", "u2@test.com", 40));

        List<UserEntity> list = userDao.getAll();
        Assertions.assertTrue(list.size() >= 2);
    }

    @Test
    void testUpdate() {
        UserEntity u = new UserEntity("Name","email@test.com",25);
        userDao.save(u);

        u.setName("Updated");
        userDao.update(u);

        UserEntity updated = userDao.getById(u.getId());
        Assertions.assertEquals("Updated", updated.getName());
    }

    @Test
    void testDelete() {
        UserEntity u = new UserEntity("Del","del@test.com",30);
        userDao.save(u);

        userDao.deleteById(u.getId());

        Assertions.assertNull(userDao.getById(u.getId()));
    }
}
