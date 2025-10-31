package by.Shelden.dao;

import by.Shelden.entity.UserEntity;
import by.Shelden.util.HibernateUtil;

import org.hibernate.Transaction;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDaoImp implements UserDao{

    private static final Logger log = LoggerFactory.getLogger(UserDaoImp.class);

    public void save(UserEntity userEntity){
        Transaction tx = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Попытка добавления обьекта");
            tx = session.beginTransaction();
            session.persist(userEntity);
            tx.commit();
        }catch (Exception e){
            if(tx != null){
                tx.rollback();
            }
            log.error("Ошбика добавления обьекта", e);
        }
    }

    public UserEntity getById(Long id){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Попытка поиска обьекта");
            return session.find(UserEntity.class, id);
        } catch (Exception e) {
            log.error("Ошибка поиска обьекта", e);
            return null;
        }
    }

    public List<UserEntity> getAll(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            log.info("Попытка получения списка обьектов");
            return session
                    .createQuery("FROM UserEntity", UserEntity.class)
                    .list();
        } catch (Exception e) {
            log.error("Ошибка получения списка обьектов", e);
            return List.of();
        }
    }

    public void update(UserEntity userEntity){
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Попытка обновления пользователя");
            tx = session.beginTransaction();
            session.merge(userEntity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("Ошибка обновления пользователя", e);
        }
    }

    public void deleteById(Long id){
        Transaction tx = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Попытка удаления обьекта");
            tx = session.beginTransaction();
            UserEntity user = session.find(UserEntity.class, id);
            if(user != null){
                session.remove(user);
            }
            tx.commit();
        } catch (Exception e) {
            if(tx != null){
                tx.rollback();
            }
            log.error("Ошибка удаления обьекта", e);
        }
    }
}
