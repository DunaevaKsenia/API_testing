package ru.yandex.stellarburgers;

import com.github.javafaker.Faker;
import lombok.Data;

@Data
public class User {
    private String name;
    private String email;
    private String password;


    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public static User randomUser() {
        Faker faker = new Faker();
        String userName = faker.name().username();
        String userPassword = faker.internet().password(8, 10);
        String userEmail = faker.internet().emailAddress();

        return new User(userName, userEmail, userPassword);
    }
}
