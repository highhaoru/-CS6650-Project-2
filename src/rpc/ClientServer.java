package rpc;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import handler.MsgStoreHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientServer extends UnicastRemoteObject implements ClientServerInterface, Runnable {

    private static final long serialVersionUID = 1L;

    private Thread t;

    private static final Logger LOGGER = LogManager.getLogger(ClientServer.class);

    private String requestType;
    private String key;
    private String msgVal;
    private String returnMsg;

    public ClientServer() throws RemoteException {
    }

    public ClientServer(String requestType, String key, String msgVal) throws RemoteException {

        this.requestType = requestType;
        this.key = key;
        this.msgVal = msgVal;
    }

    public void put(String key, String value) throws RemoteException {
        LOGGER.info("key is:" + key + " Value is:" + value);
        ClientServer serverThread = new ClientServer("PUT", key, value);
        serverThread.start();

    }

    public String get(String key) throws RemoteException {
        LOGGER.info("key is: " + key);
        ClientServer serverThread = new ClientServer("GET", key, "");
        serverThread.start();
        return this.returnMsg;

    }

    public void delete(String key) throws RemoteException {
        LOGGER.info("key is: " + key);
        ClientServer serverThread = new ClientServer("DEL", key, "");
        serverThread.start();

    }

    public void start() {
        LOGGER.info("Starting thread..");
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    @Override
    public void run() {
        LOGGER.info("Run New thread: " + Thread.currentThread().getName() + " is started..");
        LOGGER.debug("requestType: " + requestType + " msgKey" + this.key);

        try {
            if (!Objects.equals(this.requestType, "") && this.requestType.equalsIgnoreCase("PUT")) {
                addTo(this.key, this.msgVal);
            } else if (!this.requestType.equals("") && this.requestType.equalsIgnoreCase("GET")) {
                getFrom(this.key);
            } else if (!requestType.equals("") && requestType.equalsIgnoreCase("DEL")) {
                deleteFrom(this.key);
            } else {
                LOGGER.error("Unknown request type: " + requestType + " is received.");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public synchronized void addTo(String key, String value) throws RemoteException {
        LOGGER.info("key is:" + key + " Value is:" + value);
        MsgStoreHelper msgHelper = new MsgStoreHelper();
        HashMap<String, String> retrievedMap = msgHelper.readHashMap();
        if (retrievedMap != null) {
            retrievedMap.put(key, value);
            msgHelper.writeHashMap(retrievedMap);
            LOGGER.info("Put successful");
        } else {
            HashMap<String, String> msgStrore = new HashMap<String, String>();
            msgStrore.put(key, value);
            msgHelper.writeHashMap(msgStrore);
            LOGGER.info("Put successful");
        }
        LOGGER.info("PUT request completed");

    }

    public void getFrom(String key) throws RemoteException {
        LOGGER.debug(" get From Data Store.. key is:" + key);
        MsgStoreHelper msgHelper = new MsgStoreHelper();
        HashMap<String, String> retrievedMap = msgHelper.readHashMap();
        if (retrievedMap != null) {
            LOGGER.debug("Retrieved Map size: "+retrievedMap.size());
            if (retrievedMap.containsKey(key.trim())) {
                LOGGER.debug("Retrieved message is: "+retrievedMap.get(key.trim()));
                this.returnMsg = retrievedMap.get(key.trim());
                LOGGER.info("Retrieve successful");
            } else {
                String failureMsg = "There is no key-value pair for key: " + key;
                LOGGER.error(failureMsg);
            }
        } else {
            LOGGER.error("Data store not created.");

        }
        LOGGER.info("Get request completed");

    }

    public synchronized void deleteFrom(String key) throws RemoteException {
        LOGGER.debug("Delete From Data Store key is:" + key);
        MsgStoreHelper msgHelper = new MsgStoreHelper();
        HashMap<String, String> retrievedMap = msgHelper.readHashMap();
        if (retrievedMap != null) {
            LOGGER.debug("Retrieved Map size: "+retrievedMap.size());
            if (retrievedMap.containsKey(key.trim())) {

                retrievedMap.remove(key.trim());
                //Update the message store after removal
                msgHelper.writeHashMap(retrievedMap);
                LOGGER.info("Delete successful");
                LOGGER.debug("New size after deletion: "+retrievedMap.size());

            } else {
                String failureMsg = "There is no key-value pair for key: " + key;
                LOGGER.error(failureMsg);
            }
        } else {
            LOGGER.error("Data store not created.");

        }
        LOGGER.info("Delete request completed");

    }

    public static void main(String[] args) {

    }
}
