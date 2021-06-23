package com.itmo.dragon.client;

import com.itmo.dragon.shared.commands.DataBox;
import com.itmo.dragon.shared.commands.SerializationHandler;
import com.itmo.dragon.shared.commands.SizeMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;

public class Communication {
    private final Integer port;
    private final DatagramSocket datagramSocket;
    private final InetAddress serverAddress;

    public Communication(String serverAddress, Integer port) throws SocketException, UnknownHostException {
        this.port = port;
        datagramSocket = new DatagramSocket(this.port);
        this.serverAddress = InetAddress.getByName(serverAddress);
    }

    public boolean send(byte[] command) throws IOException {
        int repetition = SerializationHandler.getRepetition(command.length);
        SizeMessage sizeMessage = new SizeMessage();
        sizeMessage.Size = command.length;
        byte[] sizeBytes = SerializationHandler.serialize(sizeMessage);
        if (sizeBytes == null) return false;

        DatagramPacket sizePacket = new DatagramPacket(sizeBytes, sizeBytes.length, serverAddress, port);
        this.datagramSocket.send(sizePacket);

        int offset = 0;
        for (int i = 0; i < repetition; i++) {
            int partSize = SerializationHandler.SIZE * (i + 1) < command.length ? SerializationHandler.SIZE : command.length - SerializationHandler.SIZE * i;
            byte[] part = new byte[partSize];
            System.arraycopy(command, offset, part, 0, partSize);
            offset = offset + SerializationHandler.SIZE;
            DatagramPacket datagramPacket = new DatagramPacket(part, part.length, serverAddress, port);
            this.datagramSocket.send(datagramPacket);
        }
        return true;
    }

    public String receive() {
        byte[] buffer = new byte[SerializationHandler.SIZE + SerializationHandler.HEADER];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        try {
            datagramSocket.receive(datagramPacket);
            SizeMessage sizeMessage = (SizeMessage) SerializationHandler.deserialize(datagramPacket.getData());
            if (sizeMessage.Size <= 0) return "Size message 0";

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int repetition = SerializationHandler.getRepetition(sizeMessage.Size);
            for (int i = 0; i < repetition; i++) {
                datagramSocket.receive(datagramPacket);
                int partSize = SerializationHandler.SIZE * (i + 1) < sizeMessage.Size ? SerializationHandler.SIZE : sizeMessage.Size - SerializationHandler.SIZE * i;
                bos.write(datagramPacket.getData(),0,partSize);
            }
            buffer = bos.toByteArray();
            DataBox dataBox = (DataBox) SerializationHandler.deserialize(buffer);

            return dataBox.getResponse();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
