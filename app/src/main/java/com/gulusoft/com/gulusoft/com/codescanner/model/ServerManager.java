package com.gulusoft.com.gulusoft.com.codescanner.model;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServerManager {

    private String lastErrorMessage = "";
    private ServerSocket serverSocket=null;
    private Map<WebSocket, String> userMap=new HashMap<WebSocket, String>();
    private boolean isRunning = false;
    public ServerManager(){

    }

    public boolean IsRunning() {
        return this.isRunning;
    }

    public void UserLogin(String userName,WebSocket socket){
        if (userName!=null||socket!=null) {
            userMap.put(socket, userName);
            Log.i("TAG","LOGIN:"+userName);
            SendMessageToAll(userName+"...Login...");
        }
    }

    public void UserLeave(WebSocket socket){
        if (userMap.containsKey(socket)) {
            String userName=userMap.get(socket);
            Log.i("TAG","Leave:"+userName);
            userMap.remove(socket);
            SendMessageToAll(userName+"...Leave...");
        }
    }

    public void SendMessageToUser(WebSocket socket,String message){
        if (socket!=null) {
            socket.send(message);
        }
    }

    public void SendMessageToUser(String userName,String message){
        Set<WebSocket> ketSet=userMap.keySet();
        for(WebSocket socket : ketSet){
            String name=userMap.get(socket);
            if (name!=null) {
                if (name.equals(userName)) {
                    socket.send(message);
                    break;
                }
            }
        }
    }

    public void SendMessageToAll(String message){
        Set<WebSocket> ketSet=userMap.keySet();
        for(WebSocket socket : ketSet){
            String name=userMap.get(socket);
            if (name!=null) {
                socket.send(message);
            }
        }
    }

    public boolean Start(int port){

        if (port<0) {
            Log.i("TAG","Port error...");
            return false;
        }

        Log.i("TAG","Start ServerSocket...");

        WebSocketImpl.DEBUG=false;
        try {
            serverSocket=new ServerSocket(this,port);
            serverSocket.start();
            Log.i("TAG","Start ServerSocket Success...");
            this.isRunning = true;
            return true;
        } catch (Exception e) {
            this.isRunning = false;
            Log.i("TAG","Start Failed...");
            e.printStackTrace();
            this.lastErrorMessage = e.getMessage();
            return false;
        }
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public boolean Stop(){
        try {
            serverSocket.stop();
            Log.i("TAG","Stop ServerSocket Success...");
            this.isRunning = false;
            return true;
        } catch (Exception e) {
            Log.i("TAG","Stop ServerSocket Failed...");
            e.printStackTrace();
            return false;
        }
    }
}
