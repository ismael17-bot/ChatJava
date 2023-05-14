package org.ots06;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ControlerCliente implements Runnable {
    public static ArrayList<ControlerCliente> clientes = new ArrayList<>();

    private static final String LOG_FILE = "log.txt";
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

                if (mensagemCliente == null || mensagemCliente.isEmpty() || mensagemCliente.equals("!sair") ){
                    finalizaAll(socket, bufferedReader, bufferedWriter);
                    break;
                }
                broadcastMenssagem(mensagemCliente);
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
                    if (mensagemEnviar.isEmpty() || mensagemEnviar == null || mensagemEnviar.equals("!sair") ){
                        System.out.println("nome "+nomeCliente+" saiu");
                        finalizaAll(socket, bufferedReader, bufferedWriter);
                    }
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
            //fazer algo dps
        }
    }

    public void removeCliente() {

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String logMessage = "Nome do cliente: "+ nomeCliente + socket.getInetAddress() + " desconectou " + dateFormat.format(date);
        System.out.println(logMessage);
        writeLog(LOG_FILE, logMessage);
        clientes.remove(this);
        broadcastMenssagem("SERVIDOR: "+ nomeCliente +" desconectou-se");
    }

    private static void writeLog(String logFile, String logMessage) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            writer.write(logMessage + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
