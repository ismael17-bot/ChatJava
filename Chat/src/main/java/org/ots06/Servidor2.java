package org.ots06;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor2 {

    private ServerSocket servidorSocket;

    public Servidor2(ServerSocket servidorSocket){
        this.servidorSocket = servidorSocket;
    }

    public void startServidor(){
        try{
            while (!servidorSocket.isClosed()){
                Socket socket = servidorSocket.accept();
                System.out.println("Novo cliente conectado");
                ControlerCliente controlerCliente = new ControlerCliente(socket);

                Thread thread = new Thread(controlerCliente);
                thread.start();
            }
        } catch (IOException e){
            System.out.println("Erro na conexão");
        }
    }

    public void fechaServidor(){
        if(servidorSocket != null){
            try {
                servidorSocket.close();
            } catch (IOException e) {
                System.out.println("Erro ao fechar socket");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Servidor2 server = new Servidor2(serverSocket);

        server.startServidor();
    }
}
