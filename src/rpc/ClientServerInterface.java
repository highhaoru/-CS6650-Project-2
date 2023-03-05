package rpc;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientServerInterface extends Remote{

    public void put(String key, String value) throws RemoteException;
    public String get(String key) throws RemoteException;
    public void delete(String key) throws RemoteException;

}
