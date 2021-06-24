package com.itmo.dragon.server;

import com.itmo.dragon.shared.entities.Dragon;
import com.itmo.dragon.shared.entities.User;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Серверный модуль должен осуществлять выполнение команд по управлению коллекцией.
 *
 * @author Francisco Estrella
 * @version 0.1
 */
public class ServerApp {
    public static Hashtable<Long, Dragon> dragonsHashtable = new Hashtable<>();
    private static String fileName;
    private static String initialization;
    private static Boolean exit = false;
    private static User currentUser;

    public static Boolean getExit() {
        return exit;
    }

    public static void setExit(Boolean exit) {
        ServerApp.exit = exit;
        if (exit) System.exit(0);
    }

    public static String getFileName() {
        return fileName;
    }

    private static void setFileName(String fileName) {
        ServerApp.fileName = fileName;
    }

    public static String getInitialization() {
        return initialization;
    }

    public static void setInitialization(String initialization) {
        ServerApp.initialization = initialization;
    }

    public static void main(String[] args) {
        int port;
        if (args.length != 2) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the port: ");
            port = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter the file name: ");
            setFileName(scanner.nextLine());
        } else {
            port = Integer.parseInt(args[0]);
            setFileName(args[1]);
        }
        if (!createIfNotExists(getFileName())) {
            System.out.println("The file is invalid.");
            return;
        }

        try {
            KeyboardHandler keyboardHandler = new KeyboardHandler();
            Thread t1 = new Thread(keyboardHandler);
            t1.start();

            Communication communication = new Communication(port);
            communication.listen();
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("There was a socket exception. " + e.getMessage());
        }
    }

    private static boolean createIfNotExists(String filename) {
        File file = new File(filename);
        if (file.exists())
            return true;

        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}