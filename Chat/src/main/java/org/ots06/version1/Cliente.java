package org.ots06.version1;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    private final String SERVER_ADDRESS="127.0.0.1";
    private final int SERVER_PORT = 4001;
    private Socket clienteSocket;
    private Scanner scanner;
    private BufferedWriter out;

    public Cliente(){
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args){
        Cliente cliente = new Cliente();
        cliente.start();
    }

    public void start(){
        try {
            clienteSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectado no servidor "+SERVER_ADDRESS+" Porta: "+SERVER_PORT);
            this.out = new BufferedWriter(new OutputStreamWriter(clienteSocket.getOutputStream()));

            messageLoop();
        } catch (IOException e) {
            System.out.println("Erro ao conectar ao servidor "+e.getMessage());
        }
        System.out.println("Cliente Finalizado");
    }

    private void messageLoop() throws IOException {
        String msg;

        do{
            msg = scanner.nextLine();
            out.write(msg);
            out.newLine();
            out.flush();
        } while (!msg.equalsIgnoreCase("!sair"));

        clienteSocket.close();
    }
}