package com.itmo.dragon.client;

import com.itmo.dragon.shared.entities.User;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Клиентский модуль должен в интерактивном режиме считывать команды
 *
 * @author Francisco Estrella
 * @version 0.1
 */
public class ClientApp {
    private static User user = new User();

    private static Communication communication;

    public static User getUser() {
        return user;
    }

    public static Communication getCommunication() {
        return communication;
    }

    private static void setCommunication(Communication communication) {
        ClientApp.communication = communication;
    }

    public static void main(String[] args) {
        String serverAddress;
        int port;
        Scanner scanner = new Scanner(System.in);
        if (args.length != 2) {
            System.out.print("Enter the server address: ");
            serverAddress = scanner.nextLine();
            System.out.print("Enter the port: ");
            port = Integer.parseInt(scanner.nextLine());
        } else {
            serverAddress = args[0];
            port = Integer.parseInt(args[1]);
        }

        System.out.print("Enter your name: ");
        user.setName(scanner.nextLine());
        System.out.print("Enter your password: ");
        user.setPassword(scanner.nextLine());

        if (initializeCommunication(serverAddress, port))
            new CommandReader().readConsoleCommand();
    }

    private static Boolean initializeCommunication(String serverAddress, Integer port) {
        Scanner scanner = new Scanner(System.in);
        do {
            try {
                setCommunication(new Communication(serverAddress, port));
                return true;
            } catch (SocketException e) {
                e.printStackTrace();
                System.out.println("There was an exception while connecting to the server. " + e.getMessage());
            } catch (UnknownHostException e) {
                e.printStackTrace();
                System.out.println("There was an unknown exception. " + e.getMessage());
            }
            System.out.println("Would you like to try again (yes/no)?");
        } while (scanner.nextLine().equalsIgnoreCase("YES"));
        return false;
    }
}