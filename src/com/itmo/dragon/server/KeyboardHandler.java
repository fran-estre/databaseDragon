package com.itmo.dragon.server;

import java.util.Scanner;

public class KeyboardHandler implements Runnable {
    public void run() {
        String data;
        Scanner scanner = new Scanner(System.in);
        System.out.println("SAVE: to save file\nEXIT: to finish execution");
        ProcessHandler processHandler = new ProcessHandler();
        while (!(data = scanner.nextLine().toUpperCase()).equals("EXIT")) {
            if (data.equals("SAVE")) {
                System.out.println("Saving file...");
                System.out.println(processHandler.save());
                System.out.println("SAVE: to save data\nEXIT: to finish execution");
            }
        }
        ServerApp.setExit(true);
    }
}