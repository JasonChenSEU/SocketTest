package com.example.jason.serversockettest.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Jason on 2016/4/5.
 */
public class ChatSocket extends Thread {
    Socket socket;
    int ID;

    public ChatSocket(Socket s, int ID){
        socket = s;
        this.ID = ID;
    }

    public void getOutput(String string){
        try {
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            pw.println(string);
//            socket.getOutputStream().write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            String line = null;
            while((line = br.readLine()) != null){
                ChatManager.getInstance().publish(this,line,ID);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
