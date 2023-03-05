package handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MsgStoreHelper {
    private static final Logger LOGGER = LogManager.getLogger(MsgStoreHelper.class);

    public void writeHashMap1(HashMap<String, Object> hashMap) {

        String fileDirectory = RpcHandler.getInstance().getProperty("MSG_STORE_LOCATION");

        File directory = new File(String.valueOf(fileDirectory));
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File(fileDirectory + "/HashMapStore.txt");

        FileOutputStream f;
        try {
            f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(f);
            s.writeObject(hashMap);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public HashMap<String, Object> readHashMap1() {
        // HashMap<String, Object> fileObj = new HashMap<String, Object>();

        String fileDirectory = RpcHandler.getInstance().getProperty("MSG_STORE_LOCATION");
        File file = new File(fileDirectory + "/HashMapStore.txt");
        FileInputStream f;
        HashMap<String, Object> fileObj2 = null;
        try {

            f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(f);
            fileObj2 = (HashMap<String, Object>) s.readObject();
            if (fileObj2 != null) {

                for (String item : fileObj2.keySet()) {
                    String value = fileObj2.get(item).toString();
                    System.out.println(item + " " + value);
                }
            }
            s.close();

        } catch (FileNotFoundException e) {
            LOGGER.error("Message store file is not yet created.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return fileObj2;
    }

    public static void main(String... args) throws IOException, ClassNotFoundException {
        HashMap<String, Object> fileObj = new HashMap<String, Object>();
        MsgStoreHelper helper = new MsgStoreHelper();
        // helper.writeHashMap(fileObj);
        // helper.readHashMap1();
    }

    public void writeHashMap(HashMap<String, String> hashMap) {
        FileWriter fstream;
        BufferedWriter out;

        // create your filewriter and bufferedreader
        try {

            String fileDirectory = RpcHandler.getInstance().getProperty("MSG_STORE_LOCATION");

            // String PATH = "/remote/dir/server/";
            // String directoryName = PATH.concat(this.getClassName());
            // String fileName = id + getTimeStamp() + ".txt";

            File directory = new File(String.valueOf(fileDirectory));
            if (!directory.exists()) {
                directory.mkdir();
            }

            File file = new File(fileDirectory + "/HashMapStore.txt");

            fstream = new FileWriter(file);

            out = new BufferedWriter(fstream);

            // initialize the record count
            int count = 0;

            // create your iterator for your map
            Iterator<Entry<String, String>> it = hashMap.entrySet().iterator();

            // then use the iterator to loop through the map, stopping when we
            // reach
            // the
            // last record in the map or when we have printed enough records
            while (it.hasNext() && count < hashMap.size()) {

                // the key/value pair is stored here in pairs
                Map.Entry<String, String> pairs = it.next();
                System.out.println("Value is " + pairs.getValue());

                // since you only want the value, we only care about
                // pairs.getValue(), which is written to out
                out.write(pairs.getKey() + "," + pairs.getValue() + "\n");

                // increment the record count once we have printed to the file
                count++;
            }
            // lastly, close the file and end
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public HashMap<String, String> readHashMap() {
        //String csvFile = RpcPropertiesHandler.getInstance().getProperty("MSG_STORE_LOCATION");
        String fileDirectory = RpcHandler.getInstance().getProperty("MSG_STORE_LOCATION");
        File file = new File(fileDirectory + "/HashMapStore.txt");
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        HashMap<String, String> maps = null;

        try {

            br = new BufferedReader(new FileReader(file));
            maps = new HashMap<String, String>();
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] keyValue = line.split(cvsSplitBy);
                maps.put(keyValue[0], keyValue[1]);

            }

            // loop map
            for (Map.Entry<String, String> entry : maps.entrySet()) {

                LOGGER.debug("key:" + entry.getKey() + " , value:" + entry.getValue());

            }

        } catch (FileNotFoundException e) {
            LOGGER.error("Message store file is not yet created.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return maps;

    }

}
