package handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class RpcHandler {

    private final Properties configProp = new Properties();

    private RpcHandler() {

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("Rpc.properties");
        try {
            configProp.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class InstanceHolder {
        private static final RpcHandler INSTANCE = new RpcHandler();
    }

    public static RpcHandler getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public String getProperty(String key) {
        return configProp.getProperty(key);
    }

    public Set<String> getAllPropertyNames() {
        return configProp.stringPropertyNames();
    }

    public boolean containsKey(String key) {
        return configProp.containsKey(key);
    }
}