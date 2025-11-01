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

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }

    private static SessionFactory buildSessionFactory() {
        try {
            log.info("Запуск Hibernate SessionFactory");

            StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml");

            String url = System.getProperty("hibernate.connection.url");
            String user = System.getProperty("hibernate.connection.username");
            String pass = System.getProperty("hibernate.connection.password");
            String hbm2ddl = System.getProperty("hibernate.hbm2ddl.auto");

            if (url != null) registryBuilder.applySetting("hibernate.connection.url", url);
            if (user != null) registryBuilder.applySetting("hibernate.connection.username", user);
            if (pass != null) registryBuilder.applySetting("hibernate.connection.password", pass);
            if (hbm2ddl != null) registryBuilder.applySetting("hibernate.hbm2ddl.auto", hbm2ddl);

            StandardServiceRegistry registry = registryBuilder.build();

            return new MetadataSources(registry)
                    .addAnnotatedClass(UserEntity.class)
                    .buildMetadata()
                    .buildSessionFactory();

        } catch (Exception ex) {
            log.error("Ошибка создания сессии", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            log.info("Закрытие сессии");
            sessionFactory.close();
        }
    }
}
