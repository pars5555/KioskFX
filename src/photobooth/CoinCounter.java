/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author default
 */
public class CoinCounter {

    private static CoinCounter instance = null;
    private final String dataFileFullPath;
    private long amd100Qty;
    private long amd200Qty;
    private long amd500Qty;

    public CoinCounter() {
        dataFileFullPath = Global.getJarDir() + "/data.dat";
        File fout = new File(dataFileFullPath);
        try {
            if (!fout.exists()) {
                fout.createNewFile();
            }
        } catch (Exception ex) {
        }
        initCacheValues();
    }

    public static CoinCounter getInstance() {
        if (instance == null) {
            instance = new CoinCounter();
        }
        return instance;
    }

    public boolean setFileContent(String contents) {
        try (PrintWriter out = new PrintWriter(new FileWriter(dataFileFullPath))) {
            out.print(contents);
            out.close();
            return true;
        } catch (IOException ex) {
        }
        return false;
    }

    private String getFileContent() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(dataFileFullPath));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    private JSONObject getData() {

        String data;
        try {
            data = getFileContent();
            JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(data);
        } catch (IOException | ParseException ex) {
        }
        return new JSONObject();
    }

    private void setData(JSONObject data) {
        amd100Qty = (long) data.getOrDefault("amd_100", 0L);
        amd200Qty = (long) data.getOrDefault("amd_200", 0L);
        amd500Qty = (long) data.getOrDefault("amd_500", 0L);
        String toJSONString = data.toJSONString();
        setFileContent(toJSONString);
    }

    public void addAmd100Coin() {
        JSONObject data = getData();
        long amd_100_count = (long) data.getOrDefault("amd_100", 0L);
        data.put("amd_100", amd_100_count + 1);
        setData(data);
    }

    public void addAmd200Coin() {
        JSONObject data = getData();
        long amd_200_count = (long) data.getOrDefault("amd_200", 0L);
        data.put("amd_200", amd_200_count + 1);
        setData(data);
    }

    public void addAmd500Coin() {
        JSONObject data = getData();
        long amd_500_count = (long) data.getOrDefault("amd_500", 0L);
        data.put("amd_500", amd_500_count + 1);
        setData(data);
    }

    public long getTotalAmd() {
        JSONObject data = getData();
        long amd_100_count = (long) data.getOrDefault("amd_100", 0L);
        long amd_200_count = (long) data.getOrDefault("amd_200", 0L);
        long amd_500_count = (long) data.getOrDefault("amd_500", 0L);
        return amd_100_count * 100 + amd_200_count * 200 + amd_500_count * 500;
    }

    public long getAmd100CoinsCount() {
        JSONObject data = getData();
        return (long) data.getOrDefault("amd_100", 0L);
    }

    public long getAmd200CoinsCount() {
        JSONObject data = getData();
        return (long) data.getOrDefault("amd_200", 0L);
    }

    public long getAmd500CoinsCount() {
        JSONObject data = getData();
        return (long) data.getOrDefault("amd_500", 0L);
    }

    public void reset() {
        ServerConnection.getInstance().sendResetCounterPost(amd100Qty, amd200Qty, amd500Qty);
        JSONObject data = new JSONObject();
        setData(data);
    }

    public long getAmd100QtyFromCache() {
        return amd100Qty;
    }

    public long getAmd200QtyFromCache() {
        return amd200Qty;
    }

    public long getAmd500QtyFromCache() {
        return amd500Qty;
    }

    private void initCacheValues() {
        JSONObject data = getData();
        amd100Qty = (long) data.getOrDefault("amd_100", 0L);
        amd200Qty = (long) data.getOrDefault("amd_200", 0L);
        amd500Qty = (long) data.getOrDefault("amd_500", 0L);
    }
}
