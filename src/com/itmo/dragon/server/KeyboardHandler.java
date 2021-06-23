package com.itmo.dragon.server;

import java.util.Scanner;

public class KeyboardHandler implements Runnable {
    public void run() {
        String data;
        Scanner scanner = new Scanner(System.in);
        System.out.println("READFILE: to read dragons from file\nSAVE: to save file\nSAVEDB: to save to database\nEXIT: to finish execution");
        ProcessHandler processHandler = new ProcessHandler();
        while (!(data = scanner.nextLine().toUpperCase()).equals("EXIT")) {
            switch (data) {
                case "SAVE" -> {
                    System.out.println("Saving file...");
                    System.out.println(processHandler.saveToFile());
                }
                case "SAVEDB" -> {
                    System.out.println("Saving to database...");
                    System.out.println(processHandler.save());
                }
                case "READFILE" -> {
                    ServerApp.dragonsHashtable = new XmlReader().read(ServerApp.getFileName());
                    System.out.println("Read from file...");
                }
            }
            System.out.println("READFILE: to read dragons from file\nSAVE: to save file\nSAVEDB: to save to database\nEXIT: to finish execution");
        }
        ServerApp.setExit(true);
    }
}