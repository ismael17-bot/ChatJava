package org.ots06;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ControlerCliente implements Runnable {
    public static ArrayList<ControlerCliente> clientes = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nomeCliente;
    public ControlerCliente(Socket socket) {

        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.nomeCliente = bufferedReader.readLine();
            clientes.add(this);
            broadcastMenssagem("SERVIDOR: "+ nomeCliente +" Conectou-se");
        } catch (IOException e) {
            finalizaAll(socket, bufferedReader, bufferedWriter);
        }

    }

    public void run(){
        String mensagemCliente;

        while (socket.isConnected()) {
            try {
                mensagemCliente = bufferedReader.readLine();
                broadcastMenssagem(mensagemCliente);
                if (mensagemCliente.contentEquals("!sair")) {
                    finalizaAll(socket, bufferedReader, bufferedWriter);
                    break;
                }
            } catch (IOException e) {
                finalizaAll(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMenssagem(String mensagemEnviar){
        for (ControlerCliente controlerCliente : clientes){
            try{
                if(!controlerCliente.nomeCliente.equals(nomeCliente)){
                    controlerCliente.bufferedWriter.write(mensagemEnviar);
                    controlerCliente.bufferedWriter.newLine();
                    controlerCliente.bufferedWriter.flush();
                }
            } catch (IOException e){
                finalizaAll(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    private void finalizaAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeCliente();
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

    public void removeCliente() {
        clientes.remove(this);
        broadcastMenssagem("SERVIDOR: "+ nomeCliente +" desconectou-se");
    }


}
