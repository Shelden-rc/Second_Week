package by.Shelden.util;

import by.Shelden.entity.UserEntity;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory(){
        try {
            log.info("Запуск Hibernate SessionFactory");
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();
            return new MetadataSources(registry)
                    .addAnnotatedClass(UserEntity.class)
                    .buildMetadata()
                    .buildSessionFactory();

        }catch (Exception ex){
            log.error("Ошибка создания сессии", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        log.info("Получение сессии");
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            log.info("Закрытие сессии");
            sessionFactory.close();
        }
    }

}
