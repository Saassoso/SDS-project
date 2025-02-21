package com.auth.server_auth.rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthService extends Remote {
    boolean login(String username, String password) throws RemoteException;
    boolean register(String username, String password) throws RemoteException;
}