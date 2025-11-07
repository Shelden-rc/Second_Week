package by.Shelden.dao;

import by.Shelden.entity.UserEntity;
import java.util.List;

/**
 * DAO-интерфейс для работы с сущностью {@link UserEntity}.
 */
public interface UserDao {
    /**
     * Сохраняет нового пользователя в базе данных.
     * @param userEntity сущность пользователя (без id)
     * @return сохраненного пользователя
     */
    UserEntity save(UserEntity userEntity);
    /**
     * Ищет пользователя по идентификатору.
     * @param id идентификатор
     * @return Optional с пользователем, если найден
     */
    UserEntity getById(Long id);
    /**
     * Возвращает всех пользователей.
     * @return список всех пользователей
     */
    List<UserEntity> getAll();
    /**
     * Обновляет данные существующего пользователя.
     * @param id идентификатор
     * @param userEntity данные для обновления
     * @return Обновленного опльзователя
     */
     UserEntity update(Long id, UserEntity userEntity);
    /**
     * Удаляет пользователя по идентификатору.
     * @param id идентификатор
     */
    void deleteById(Long id);
}
