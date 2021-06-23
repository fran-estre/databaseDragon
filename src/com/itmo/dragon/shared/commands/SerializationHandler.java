package com.itmo.dragon.shared.commands;

import java.io.*;

public class SerializationHandler {
    public static final int SIZE = 512;
    public static final int HEADER = 128; // I guess

    public static byte[] serialize(Object data) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(data);
            oos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Serializable deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o;
        try {
            o = new ObjectInputStream(b);
            return (Serializable) o.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getRepetition(int size) {
        int residue = size % SIZE;
        int division = (size - residue) / SIZE;

        return residue == 0 ? division : division + 1;
    }
}
