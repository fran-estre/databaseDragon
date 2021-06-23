package com.itmo.dragon.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader {

    public String read(String fileName) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new java.io.FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        String line;
        StringBuilder allData = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                allData.append(line).append("\n");
            }
            return allData.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}