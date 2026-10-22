import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class UsersApp {

    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();

        // Read and validate users from the input file
        try (Scanner scanner = new Scanner(new File("users.txt"))) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s+");

                if (parts.length != 2) {
                    System.out.println(line + " Please enter a valid Email as username");
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

        // Sort valid users alphabetically by username
        users.sort(Comparator.comparing(User::getUsername));

        // Save valid users to the output file
        try (PrintWriter writer = new PrintWriter("out.txt")) {

            for (User user : users) {
                writer.println(user);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Cannot create out.txt");
        }
    }
}

