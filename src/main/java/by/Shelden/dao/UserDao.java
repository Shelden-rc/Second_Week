package by.Shelden.dao;

import by.Shelden.entity.User;
import by.Shelden.util.HibernateUtil;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class UserDao implements Dao{

    private static final Logger log = LoggerFactory.getLogger(UserDao.class);

    public void save(User user){
        Transaction tx = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Попытка добавления обьекта");
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        }catch (Exception e){
            if(tx != null){
                tx.rollback();
            }
            log.error("Ошбика добавления обьекта", e);
        }
    }

    public User getById(Long id){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Попытка поиска обьекта");
            User user = session.find(User.class, id);
            return user;
        } catch (Exception e) {
            log.error("Ошибка поиска обьекта", e);
            return null;
        }
    }

    public List<User> getAll(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            log.info("Попытка получения списка обьектов");
            List<User> list = session
                    .createQuery("FROM User", User.class)
                    .list();
            return list;
        } catch (Exception e) {
            log.error("Ошибка получения списка обьектов", e);
            return List.of();
        }
    }

    public void update(User user){
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Попытка обновления пользователя");
            tx = session.beginTransaction();
            session.merge(user);
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
            User user = session.find(User.class, id);
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
