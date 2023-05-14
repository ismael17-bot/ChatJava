package org.ots06;

import javax.imageio.IIOException;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente2 {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nomeCliente;

    public Cliente2(Socket socket, String nomeCliente) {
        try {
            this.socket = socket;
            this.nomeCliente = nomeCliente;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            finalizaAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public void enviaMensagem(){
        try{
            bufferedWriter.write(nomeCliente);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(true){
                String mensagemEnviar = scanner.nextLine();
                if (mensagemEnviar.equals("!sair")) {
                    break;
                }
                bufferedWriter.write(nomeCliente + ": " + mensagemEnviar);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            socket.close();
        } catch (IOException e) {
            finalizaAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMensagem() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mensagemChat;

                while (socket.isConnected()){
                    try {
                        mensagemChat = bufferedReader.readLine();
                        System.out.println(mensagemChat);
                    } catch (IOException e) {
                        finalizaAll(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    private void finalizaAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Erro ao encerrar "+e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite seu nome para utilizar no chat: ");
        String nome = scanner.nextLine();
        Socket socket1 = new Socket("localhost", 1234);

        Cliente2 cliente = new Cliente2(socket1, nome);
        cliente.listenForMensagem();
        cliente.enviaMensagem();
    }
}
