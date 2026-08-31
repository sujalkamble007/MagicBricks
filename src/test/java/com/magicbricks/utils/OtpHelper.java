package com.magicbricks.utils;

import java.util.Scanner;

/**
 * Handles manual OTP entry from the tester via console prompt.
 * When an OTP is sent during login testing, this utility pauses execution
 * and waits for the tester to type the OTP they received on their phone.
 */
public class OtpHelper {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Prompts the tester to enter the OTP received on their mobile.
     * Blocks until valid input is provided.
     *
     * @return the OTP string entered by the tester
     */
    public static String waitForOtpInput() {
        String otp = "";
        while (otp.isEmpty()) {
            System.out.println();
            System.out.println("==========================================================");
            System.out.println(">>> OTP sent to mobile number.");
            System.out.println(">>> Please enter the OTP received on your phone below:");
            System.out.println("==========================================================");
            System.out.print(">>> OTP: ");

            otp = scanner.nextLine().trim();

            if (otp.isEmpty()) {
                System.out.println(">>> OTP cannot be empty. Please try again.");
            }
        }

        System.out.println(">>> OTP entered: " + otp + " — Resuming test execution...");
        System.out.println();
        return otp;
    }
}
