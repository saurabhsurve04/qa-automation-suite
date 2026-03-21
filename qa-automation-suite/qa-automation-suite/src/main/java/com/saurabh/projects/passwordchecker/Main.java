package com.saurabh.projects.passwordchecker;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your password: \n");
        String password = input.nextLine();
        PasswordChecker passwordChecker = new PasswordChecker(password);
        int score = passwordChecker.passwordScore(password);
        passwordChecker.passwordStrength(score);

    }
}
