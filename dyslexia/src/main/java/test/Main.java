package test;

import entities.*;
import services.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserServiceImp userService = new UserServiceImp();
        // ---------------------------------------
        // 1. Create and add a Dyslexic Student
        // ---------------------------------------
        DyslexicStudent student = new DyslexicStudent();
        student.setEmail("student55@example.com");
        student.setPassword("student123");
        student.setFirstName("Emily");
        student.setLastName("Williams");
        student.setBirthDay(Date.valueOf("1990-05-12"));
        student.setGender("Female");
        student.setProfileImage("student.jpg");
        student.setDateCreation(new Timestamp(System.currentTimeMillis()));
        student.setAuthCode("STUDENT123");
        student.setDyslexiaType(DyslexiaType.PHONOLOGICAL);
        student.setSeverityLevel(SeverityLevel.MODERATE);
        student.setSpecificNeeds("Extra time for reading tasks");
        student.setGlobalProgress("Making good progress");
        student.setRole(Roles.ROLE_STUDENT);

        userService.createUser(student);
        System.out.println("✅ Student created with ID: " + student.getIdUser());
        // ---------------------------------------
        // 2. Retrieve user by ID
        // ---------------------------------------
        System.out.println("\n Finding user by ID (" + student.getIdUser() + "):");
        User foundUser = userService.getUserById(student.getIdUser());
        if (foundUser != null) {
            System.out.println(foundUser);
        } else {
            System.out.println("User not found.");
        }

        // ---------------------------------------
        // 3. Retrieve user by email
        // ---------------------------------------
        System.out.println("\nFinding user by email (student55@example.com):");
        foundUser = userService.getUserByEmail("student55@example.com");
        if (foundUser != null) {
            System.out.println(foundUser);
        } else {
            System.out.println("User not found.");
        }
        // ---------------------------------------
        // 4. Update the student
        // ---------------------------------------
        if (foundUser instanceof DyslexicStudent ds) {
            ds.setGlobalProgress("Improved significantly");
            ds.setSpecificNeeds("Still needs extra reading time");
            userService.updateUser(ds);

            System.out.println("\n Updated student:");
            User updated = userService.getUserById(ds.getIdUser());
            System.out.println(updated != null ? updated : "Failed to retrieve updated student.");
        }
        // ---------------------------------------
        // 5. Display all users
        // ---------------------------------------
        System.out.println("\n All users:");
        List<User> allUsers = userService.getAllUsers();
        if (allUsers.isEmpty()) {
            System.out.println("No users found.");
        } else {
            allUsers.forEach(System.out::println);
        }

        // ---------------------------------------
        // 6. Delete the user
        // ---------------------------------------
        System.out.println("\n Deleting student with ID: " + student.getIdUser());
        userService.removeUSER(student.getIdUser());
        System.out.println("User deleted successfully.");

        // ---------------------------------------
        // 7. Display remaining users
        // ---------------------------------------
        System.out.println("\n Remaining users:");
        List<User> remainingUsers = userService.getAllUsers();
        if (remainingUsers.isEmpty()) {
            System.out.println("No users remaining.");
        } else {
            remainingUsers.forEach(System.out::println);
        }
    }
}





