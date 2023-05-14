package org.ots06.version1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static final int PORT = 4001;
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        Servidor server = new Servidor();
        server.start();
    }

    public void start() {
        try {
            System.out.println("Servidor iniciado");
            serverSocket = new ServerSocket(PORT);
            connectionClientLoop();
        } catch (IOException e) {
            System.out.println("Erro ao iniciar o servidor " + e.getMessage());
        }

        System.out.println("Servidor finalizado");
    }

    private void connectionClientLoop() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> connectionClientLoop(clientSocket)).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void connectionClientLoop(Socket clientSocket) {
        try {
            System.out.println("Novo Cliente: " + clientSocket.getRemoteSocketAddress() + " se conectou");
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true) {
                String msg = in.readLine();
                if (msg == null) {
                    System.out.println("Cliente: " + clientSocket.getRemoteSocketAddress() + " desconectado");
                    break;
                }
                System.out.println("Mensagem recebida do cliente " + clientSocket.getRemoteSocketAddress() + ": " + msg);
            }

            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}