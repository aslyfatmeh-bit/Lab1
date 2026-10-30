
package com.example.lab2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class UserLoader {

    public static ArrayList<User> loadUsers(String fileName) {
        ArrayList<User> users = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(fileName))) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s+");

                if (parts.length != 2) {
                    continue;
                }

                try {
                    User user = new User(parts[0], parts[1]);
                    users.add(user);
                } catch (IllegalArgumentException e) {
                    // Ignore invalid users
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        }

        return users;
    }
}
