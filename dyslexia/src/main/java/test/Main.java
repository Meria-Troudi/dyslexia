package test;

import entities.*;
import services.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserServiceImp userService = new UserServiceImp();
        // 1. Create Dyslexic Student
        DyslexicStudent student = new DyslexicStudent();
        student.setEmail("test.student@example.com");
        student.setPassword("test123");
        student.setFirstName("OriginalName");
        student.setLastName("LastName");
        student.setBirthDay(Date.valueOf("2000-01-01"));
        student.setGender("Female");
        student.setPhoneNumber(112233445);
        student.setProfileImage("student.jpg");
        student.setDateCreation(new Timestamp(System.currentTimeMillis()));
        student.setBanned(false);
        student.setDyslexiaType(DyslexiaType.MIXED);
        student.setSeverityLevel(SeverityLevel.MODERATE);
        student.setSpecificNeeds("Needs original");
        student.setRole(Roles.ROLE_STUDENT);

        // 2. Create user
        userService.createUser(student);
        System.out.println("✅ Created student with ID: " + student.getIdUser());

        // 3. Update fields
        student.setFirstName("UpdatedName");
        student.setSpecificNeeds("Updated needs");
        userService.updateUser(student);
        System.out.println("✅ Updated student name to: " + student.getFirstName());

        // 4. Optional: fetch back and verify
        User fetched = userService.getUserById(student.getIdUser());
        if (fetched instanceof DyslexicStudent ds) {
            System.out.println("🔎 Fetched updated student name: " + ds.getFirstName());
            System.out.println("🔎 Fetched updated needs: " + ds.getSpecificNeeds());
        } else {
            System.out.println("⚠ Could not verify updated student.");
        }
        // 5. Create Psy
        Psy psy = new Psy();
        psy.setEmail("test.psy@example.com");
        psy.setPassword("psy123");
        psy.setFirstName("Therapist");
        psy.setLastName("Mindwell");
        psy.setBirthDay(Date.valueOf("1980-05-10"));
        psy.setGender("Male");
        psy.setPhoneNumber(998877665);
        psy.setProfileImage("psy.jpg");
        psy.setDateCreation(new Timestamp(System.currentTimeMillis()));
        psy.setBanned(false);
        psy.setBiography("Experienced psychologist in learning disabilities.");
        psy.setSchedule("Mon-Fri 10am-4pm");
        psy.setRole(Roles.ROLE_PSY);

// 6. Create psy
        userService.createUser(psy);
        System.out.println("✅ Created Psy with ID: " + psy.getIdUser());

// 7. Update psy
        psy.setSchedule("Mon-Fri 8am-2pm");
        psy.setBiography("Updated biography of the psychologist.");
        userService.updateUser(psy);
        System.out.println("✅ Updated Psy schedule to: " + psy.getSchedule());

// 8. Optional: fetch back and verify
        User fetchedPsy = userService.getUserById(psy.getIdUser());
        if (fetchedPsy instanceof Psy p) {
            System.out.println("🔎 Fetched updated Psy biography: " + p.getBiography());
            System.out.println("🔎 Fetched updated Psy schedule: " + p.getSchedule());
        } else {
            System.out.println("⚠ Could not verify updated Psy.");
        }

    }
}





