package io.conduktor.demos.kafka.Consummer;

import java.io.*;
import java.net.*;

public class TCPAlertClient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public TCPAlertClient(String host, int port) throws IOException {
    
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendAlert(String alertMessage) {
        out.println(alertMessage); 
    }

    public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }
}

