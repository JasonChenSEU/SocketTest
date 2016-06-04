package com.example.jason.serversockettest.Client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by Jason on 2016/4/5.
 */
public class ChatManager {

//    private static final String TAG = "ChatManager";
    private static final ChatManager cm = new ChatManager();
    public static boolean isSocketAlive = true;
    Vector<ChatSocket> vector = new Vector<>();

    private ChatManager(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isSocketAlive){
                    try {
                        Thread.sleep(10000);//10s
                        System.out.println("Start cleaning...");
                        if(!vector.isEmpty()){
                            Iterator<ChatSocket> it = vector.iterator();
                            while(it.hasNext()){
                                ChatSocket cs = it.next();
                                if(cs.socket.isClosed()) {
                                    it.remove();
                                    System.out.println("ChatSocket#" + cs.ID + " is closed. Remove it from vector");
                                    cs = null;
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static ChatManager getInstance(){
        return cm;
    }

    public void add(ChatSocket cs){
        vector.add(cs);
    }

    public void publish(ChatSocket chatSocket, String out,int ID){
        String time = new SimpleDateFormat("(HH:mm:ss)").format(new Date(System.currentTimeMillis()));
        for (int i = 0; i < vector.size(); i++) {
            if (!chatSocket.socket.isClosed()) {
                if (chatSocket == vector.get(i))
                    vector.get(i).getOutput("I "+ time + ": " + out);
                else
                    vector.get(i).getOutput("Client#" + ID + time + ": " + out);
            }
        }
    }
}
