package rpc;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client {

    private static final Logger LOGGER = LogManager.getLogger(Client.class);

    public static void main(String[] args) {

        if (args.length < 2) {
            LOGGER.error("java Client1 <Host Name> <Port Number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try {
            // Take the Data set size from user
            Scanner in = new Scanner(System.in);

            // get request type
            String requestType = "";
            System.out.println("Please Enter Request type.that is: PUT / GET/ DEL");
            requestType = in.nextLine();

            // get key
            String key = "";
            System.out.println("Please Enter a Key");
            key = in.nextLine();

            // get message
            String msgValue = "";
            if (!"".equals(requestType) && requestType.equalsIgnoreCase("PUT")) {
                System.out.println("Please Enter Data for Key: " + key);
                msgValue = in.nextLine();
            }

            Registry registry = LocateRegistry.getRegistry(hostName, portNumber);

            ClientServerInterface messageServer = (ClientServerInterface) registry
                    .lookup("ClientServerInterfaceImpl");

            if (!"".equals(requestType) && requestType.equalsIgnoreCase("PUT")) {

                messageServer.put(key, msgValue);
                LOGGER.info("Put request is completed. Please check the log");

            } else if (!"".equals(requestType) && requestType.equalsIgnoreCase("GET")) {
                messageServer.get(key);
                LOGGER.info("Get request is completed. Please check the log");

            } else if (!"".equals(requestType) && requestType.equalsIgnoreCase("DEL")) {
                messageServer.delete(key);
                LOGGER.info("Delete request is completed. Please check the log");

            } else {
                LOGGER.error("Unknown request type: " + requestType);
            }

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

    }

}