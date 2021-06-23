package com.itmo.dragon.server;

import com.itmo.dragon.shared.entities.Dragon;

import java.io.IOException;
import java.util.Hashtable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class XmlReader {

    public Hashtable<Long, Dragon> read(String fileName) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        String line;
        StringBuilder data = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                data.append(line).append("\n");
            }
            if (data.length()==0)
                return new Hashtable<>();

            data = new StringBuilder(data.substring(0, data.length() - 1));
            DragonsReader dragonsReader = new DragonsReader();
            return dragonsReader.read(data.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}