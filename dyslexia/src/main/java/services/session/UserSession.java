package services.session;

import entities.User;

public class UserSession {
    public static UserSession CURRENT_USER;
    private User user;

    public UserSession(User userLoggedIn) {
        this.user = userLoggedIn;
    }
    public static UserSession getCurrentUser() {
        return CURRENT_USER;
    }
    public static void setCurrentUser(UserSession currentUser) {
        CURRENT_USER = currentUser;
    }
    public static UserSession getSession(User userLoggedIn) {
        if (CURRENT_USER == null) {
            CURRENT_USER = new UserSession(userLoggedIn);
        }return CURRENT_USER;
    }
    public User getUserLoggedIn() {
        return user;
    }

    public static void logout() {
        CURRENT_USER = null;
        System.out.println("User logged out, redirecting to login");
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userLoggedIn=" + user +
                '}';
    }
}
