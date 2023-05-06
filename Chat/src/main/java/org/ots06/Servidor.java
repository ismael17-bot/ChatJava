package org.ots06;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static final int PORT = 4001;
    private ServerSocket serverSocket;
    private BufferedReader in;

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
            System.out.println("Erro ao iniciar o servidor "+e.getMessage());
        }

        System.out.println("Servidor finalizado");
    }

    private void connectionClientLoop() {
        while(true){
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente: "+clientSocket.getRemoteSocketAddress()+" se conectou");
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String msg = in.readLine();
                System.out.println(
                        "Mensagem recebida do cliente: "+clientSocket.getRemoteSocketAddress()
                                +"Mensagem: "+msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
