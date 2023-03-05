package rpc;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RmiRegister {
    private static final Logger LOGGER = LogManager.getLogger(RmiRegister.class);

    public static void main(String[] args) {

        if (args.length < 1) {
            LOGGER.error(" java RmiRegister <Port Number>");
            System.exit(1);
        }

        int portNo = Integer.parseInt(args[0]);

        try{
            ClientServerInterface messageServerObject = new ClientServer();
            Registry registry = LocateRegistry.createRegistry(portNo);
            registry.bind("ClientServerInterfaceImpl", messageServerObject);
            LOGGER.info("Object binding is done");

        }catch(Exception ignored){

        }

    }

}