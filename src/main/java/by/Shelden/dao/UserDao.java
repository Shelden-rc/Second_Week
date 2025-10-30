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
     * @return сохранённая сущность пользователя с сгенерированным id
     */
    void save(UserEntity userEntity);
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
     * @param userEntity сущность с заполненным id
     * @return обновлённая сущность
     */
    void update(UserEntity userEntity);
    /**
     * Удаляет пользователя по идентификатору.
     * @param id идентификатор
     * @return true, если удаление было выполнено; false, если пользователь не найден
     */
    void deleteById(Long id);
}
