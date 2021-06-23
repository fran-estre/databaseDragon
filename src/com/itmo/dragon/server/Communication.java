package com.itmo.dragon.server;

import com.itmo.dragon.shared.commands.Command;
import com.itmo.dragon.shared.commands.DataBox;
import com.itmo.dragon.shared.commands.SerializationHandler;
import com.itmo.dragon.shared.commands.SizeMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;

public class Communication {
    private final DatagramSocket datagramSocket;

    public Communication(Integer port) throws SocketException {
        datagramSocket = new DatagramSocket(port);
    }

    public void listen() {
        try {
            System.out.println("Server working at " + InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("There was an exception while getting local host address. " + e.getMessage());
        }

        ProcessHandler processHandler = new ProcessHandler();
        while (!ServerApp.getExit()) {
            byte[] buffer = new byte[SerializationHandler.SIZE + SerializationHandler.HEADER];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            try {
                datagramSocket.receive(datagramPacket);
                SizeMessage sizeMessage = (SizeMessage) SerializationHandler.deserialize(datagramPacket.getData());
                if (sizeMessage.Size <= 0) continue;

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int repetition = SerializationHandler.getRepetition(sizeMessage.Size);
                for (int i = 0; i < repetition; i++) {
                    datagramSocket.receive(datagramPacket);
                    int partSize = SerializationHandler.SIZE * (i + 1) < sizeMessage.Size ? SerializationHandler.SIZE : sizeMessage.Size - SerializationHandler.SIZE * i;
                    bos.write(datagramPacket.getData(), 0, partSize);
                }
                buffer = bos.toByteArray();

                Command command = (Command) SerializationHandler.deserialize(buffer);
                String response = processHandler.processCommand(command);
                AnswerToClient(datagramPacket.getAddress(), datagramPacket.getPort(), response);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("There was an exception while receiving the datagramPacket " + e.getMessage());
                datagramSocket.close();
            }
        }
    }

    private void AnswerToClient(InetAddress clientAddress, int clientPort, String response) throws IOException {
        DataBox dataBox = new DataBox();
        dataBox.setResponse(response);
        byte[] responseBytes = SerializationHandler.serialize(dataBox);
        if (responseBytes == null) {
            throw new InternalError("Size array is 0");
        }

        int repetition = SerializationHandler.getRepetition(responseBytes.length);
        SizeMessage sizeMessage = new SizeMessage();
        sizeMessage.Size = responseBytes.length;
        byte[] sizeBytes = SerializationHandler.serialize(sizeMessage);
        if (sizeBytes == null) {
            throw new InternalError("Size array is 0");
        }

        DatagramPacket sizePacket = new DatagramPacket(sizeBytes, sizeBytes.length, clientAddress, clientPort);
        this.datagramSocket.send(sizePacket);

        int offset = 0;
        for (int i = 0; i < repetition; i++) {
            int partSize = SerializationHandler.SIZE * (i + 1) < responseBytes.length ? SerializationHandler.SIZE : responseBytes.length - SerializationHandler.SIZE * i;
            byte[] part = new byte[partSize];
            System.arraycopy(responseBytes, offset, part, 0, partSize);
            offset = offset + SerializationHandler.SIZE;
            DatagramPacket responsePacket = new DatagramPacket(part, part.length, clientAddress, clientPort);
            this.datagramSocket.send(responsePacket);
        }
    }
}