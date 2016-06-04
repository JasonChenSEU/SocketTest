package com.example.jason.serversockettest.Server;

import com.example.jason.serversockettest.Client.ChatManager;
import com.example.jason.serversockettest.Client.ChatSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jason on 2016/4/5.
 */
public class ServerSocketListener extends Thread {

    private int ID = 0;
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(20000);

            //处理多个客户端连接到SocketServer
            while(true) {
                Socket socket = serverSocket.accept();

                System.out.println("Client: " + ++ID +" has connected to the Server.");
                //创建新的线程处理
                ChatSocket cs = new ChatSocket(socket, ID);
                cs.start();
                ChatManager.getInstance().add(cs);
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
