
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class UsersApp {

    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();

        // Read users from the input file
        try (Scanner scanner = new Scanner(new File("users.txt"))) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                // Split username and password
                String[] parts = line.split("\\s+");

                if (parts.length != 2) {
                    System.out.println(line +
                            " Please enter a valid Email as username");
                    continue;
                }

                String username = parts[0];
                String password = parts[1];

                try {
                    User user = new User(username, password);
                    users.add(user);
                } catch (IllegalArgumentException e) {
                    System.out.println(line + " " + e.getMessage());
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: users.txt");
            return;
        }

        // Sort valid users by username
        users.sort(Comparator.comparing(User::getUsername));

        // Print sorted valid users
        for (User user : users) {
            System.out.println(user);
        }
    }
}
