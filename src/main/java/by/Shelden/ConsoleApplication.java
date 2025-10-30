package by.Shelden;

import by.Shelden.entity.UserEntity;
import by.Shelden.service.UserService;

import by.Shelden.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ConsoleApplication {
    private static final Logger log = LoggerFactory.getLogger(ConsoleApplication.class);
    private static final UserService userService = new UserService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running){
            switch (selectOption(scanner)){
                case 1 -> createUser(scanner);
                case 2 -> findUser(scanner);
                case 3 -> getAllUsers();
                case 4 -> updateUser(scanner);
                case 5 -> deleteUser(scanner);
                case 0 -> {
                    running = false;
                    HibernateUtil.shutdown();
                    log.info("Закрытие программы");
                    System.out.println("Выход из программы");
                }
                default -> System.out.println("Некорректный ввод\n\n");
            }
        }
    }

    public static int selectOption(Scanner scanner){
        System.out.println("""


                    ============= МЕНЮ =============
                    1. Создать Пользователя
                    2. Найти пользователя по ID
                    3. Вывести всех пользователей
                    4. Обновить данные Пользователя
                    5. Удалить Пользователя
                    0. Выход
                    ================================
                    """);
        System.out.print("\nВыберите опцию: ");
        int option = scanner.nextInt();
        scanner.nextLine();
        System.out.println();

        if(option < 0 || option > 5) {
            System.out.println("Неккоректный ввод.\n\n");
            selectOption(scanner);
        }

        return option;
    }

    public static void createUser(Scanner scanner){
        System.out.println("Введите имя пользователя:");
        String name = scanner.nextLine();
        System.out.println("Введите почту:");
        String email = scanner.nextLine();
        System.out.println("Введите возраст пользователя");
        int age = scanner.nextInt();
        scanner.nextLine();
        userService.createUser(name, email, age);
    }

    public static void findUser(Scanner scanner){
        System.out.println("Введите ID пользователя:");
        Long id = scanner.nextLong();
        scanner.nextLine();
        UserEntity userEntity = userService.findUser(id);
        if(userEntity != null){
            System.out.println(userEntity);
        }else {
            System.out.println("Пользователь не найден");
        }
    }

    public static void getAllUsers(){
        userService.getAllUsers().forEach(System.out::println);
    }

    public static void updateUser(Scanner scanner){
        System.out.println("Введите ID пользователя:");
        Long id = scanner.nextLong();
        scanner.nextLine();
        UserEntity userEntity = userService.findUser(id);
        if(userEntity != null){
            System.out.println(userEntity);
        }else {
            System.out.println("Пользователь не найден");
            return;
        }
        System.out.println("Введите новое имя пользователя:");
        String name = scanner.nextLine();
        System.out.println("Введите новую почту:");
        String email = scanner.nextLine();
        System.out.println("Введите новый возраст пользователя");
        int age = scanner.nextInt();
        scanner.nextLine();
        userEntity.setName(name);
        userEntity.setEmail(email);
        userEntity.setAge(age);
        try {
            userService.updateUser(userEntity);
        }catch (Exception e){
            System.out.println("Ошибка обновления: " + e.getMessage());
        }
    }

    public static void deleteUser(Scanner scanner){
        System.out.println("Введите ID пользователя:");
        Long id = scanner.nextLong();
        scanner.nextLine();
        UserEntity userEntity = userService.findUser(id);
        if(userEntity != null){
            System.out.println(userEntity);
        }else {
            System.out.println("Пользователь не найден");
            return;
        }
        try {
            userService.deleteUser(id);
        }catch (Exception e){
            System.out.println("Ошибка удаления" + e.getMessage());
        }
    }
}
