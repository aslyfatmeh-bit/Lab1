public class User {
    private final String username;
    private final String password;

    // Constructor: validates user data
    public User(String username, String password) {
        if (username == null || !isValidEmail(username)) {
            throw new IllegalArgumentException(
                    "Please enter a valid Email as username");
        }

        if (username.length() > 50) {
            throw new IllegalArgumentException(
                    "Username is too long, try something shorter");
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                    "Your password is too short, add more characters");
        }

        if (password.length() > 12) {
            throw new IllegalArgumentException(
                    "Your password is too long, try a shorter one");
        }

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException(
                    "Please enter a valid password");
        }

        this.username = username;
        this.password = password;
    }

    // Check email format
    private static boolean isValidEmail(String email) {
        return email.matches(
                "[A-Za-z0-9._+%\\-]+@" +
                        "[A-Za-z0-9][A-Za-z0-9.\\-]*\\." +
                        "[A-Za-z]{2,}"
        );
    }

    // Check password characters and required types
    private static boolean isValidPassword(String password) {
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;

        String symbols = "~!@#$%^&*()_+";

        for (char c : password.toCharArray()) {
            if ((c >= 'A' && c <= 'Z') ||
                    (c >= 'a' && c <= 'z')) {
                hasLetter = true;
            } else if (c >= '0' && c <= '9') {
                hasDigit = true;
            } else if (symbols.indexOf(c) >= 0) {
                hasSymbol = true;
            } else {
                return false;
            }
        }

        return hasLetter && hasDigit && hasSymbol;
    }

    // Getter for sorting
    public String getUsername() {
        return username;
    }

    // String representation of the user
    @Override
    public String toString() {
        return username + " " + password;
    }
}
