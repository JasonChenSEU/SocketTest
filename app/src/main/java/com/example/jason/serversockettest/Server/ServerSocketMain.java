package com.example.jason.serversockettest.Server;

import com.example.jason.serversockettest.Server.ServerSocketListener;

/**
 * Created by Jason on 2016/4/5.
 */
public class ServerSocketMain {
    public static void main(String[] args){
        //port范围1-65535
//        try {
//            ServerSocket serverSocket = new ServerSocket(20000);
//            Socket socket = serverSocket.accept();
//
//            System.out.println("有客户端连接到本机20000端口");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        new ServerSocketListener().start();

    }
}
