package com.saurabh.projects.passwordchecker;

public class PasswordChecker {

    private String password;

    public PasswordChecker(String password) {
        this.password = password;
    }

    public static boolean checkPasswordLength(String password) {
        int length = password.length();
        if (length < 8) {
            System.out.println("Password length should be at least 8 characters, current password length is " + length);
            return false;
        } else {
            System.out.println("Current Password length is: " + length);
            return true;
        }
    }

    public static boolean checkUpperCase(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                System.out.println("Password contains upper case characters");
                return true;
            }
        }
        return false;
    }

    public static boolean checkLowerCase(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i))) {
                System.out.println("Password contains lower case characters");
                return true;
            }
        }
        return false;
    }

    public static boolean checkPasswordHasNumber(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                System.out.println("Password contains numbers");
                return true;
            }
        }
        return false;
    }

    public static boolean checkPasswordHasSpecialCharacter(String password) {
        String specialChars = "!@#$%^&*";
        for (int i = 0; i < password.length(); i++) {
            if (specialChars.indexOf(password.charAt(i)) != -1) {
                System.out.println("Password contains special characters");
                return true;
            }
        }
        return false;
    }

    public int passwordScore(String password) {
        int score = 0;
        if (checkPasswordLength(password)) {
            score++;
        }
        if (checkUpperCase(password)) {
            score++;
        }
        if (checkLowerCase(password)) {
            score++;
        }
        if (checkPasswordHasNumber(password)) {
            score++;
        }
        if (checkPasswordHasSpecialCharacter(password)) {
            score++;
        }
        System.out.println("Password score: " + score + "\n");
        return score;
    }

    public void passwordStrength(int score) {
        if (score == 5) {
            System.out.println("Password Strength: Strong");
        } else if (score < 5 && score >= 3) {
            System.out.println("Password Strength: Medium");
        } else {
            System.out.println("Password Strength: Weak");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
