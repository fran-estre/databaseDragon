package com.itmo.dragon.client;

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
    private static Communication communication;

    public static Communication getCommunication() {
        return communication;
    }

    private static void setCommunication(Communication communication) {
        ClientApp.communication = communication;
    }

    public static void main(String[] args) {
        String serverAddress;
        int port;
        if (args.length != 2) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the server address: ");
            serverAddress = scanner.nextLine();
            System.out.print("Enter the port: ");
            port = Integer.parseInt(scanner.nextLine());
        } else {
            serverAddress = args[0];
            port = Integer.parseInt(args[1]);
        }

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