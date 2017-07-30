/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.natanaelsimoes;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class TCPArduino {

    private Socket socket;
    private String host;
    private int port;
    private int timeout;

    public TCPArduino(String host, int port) throws Exception, IOException {
        this.socketCreate(host, port, 10);
        this.socketListen();
    }

    public TCPArduino(String host, int port, int timeout) throws Exception {
        this.socketCreate(host, port, timeout);
        this.socketListen();
    }

    private void socketCreate(String host, int port, int timeout) throws Exception {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.socket = new Socket(this.host, this.port);
        this.socket.setSoTimeout(this.timeout * 1000);
    }

    private void socketListen() throws Exception, IOException {
        while (true) {
            try {
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                String dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
                String message = inFromServer.readLine();
                System.out.println(String.format("[%s]: %s", dateTime, message));
                PrintWriter file = new PrintWriter(this.host, "UTF-8");
                file.println(message);
                file.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                this.socketCreate(this.host, this.port, this.timeout);
            }
        }
    }

    public static void main(String argv[]) throws Exception {
        if (argv.length == 2) {
            new TCPArduino(argv[0], Integer.parseInt(argv[1]));
        } else {
            new TCPArduino(argv[0], Integer.parseInt(argv[1]), Integer.parseInt(argv[2]));
        }
    }

}
