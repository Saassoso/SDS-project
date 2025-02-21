package com.auth.server_auth.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AuthServer {
    public static void main(String[] args) {
        try {
            AuthService authService = new AuthServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("AuthService", authService);
            System.out.println("Authentication RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

