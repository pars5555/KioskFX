/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth;

import photobooth.managers.UpdateManager;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author default
 */
public class ServerConnection implements Runnable {

    private static ServerConnection instance = null;
    private Thread thread = null;

    private ServerConnection() {

    }

    public static ServerConnection getInstance() {
        if (instance == null) {
            instance = new ServerConnection();
        }
        return instance;
    }

    public void init() {
        thread = new Thread(this);
        thread.start();
    }

    public boolean checkConnectionToServer() {
        String serverHost = Config.getInstance().getString("server_host");
        Socket sock = new Socket();
        InetSocketAddress addr = new InetSocketAddress(serverHost, 80);
        try {
            sock.connect(addr, 3000);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                sock.close();
            } catch (IOException e) {
            }
        }
    }

    public String sendResetCounterPost(Long amd100Qty, Long amd200Qty, Long amd500Qty) {
        String postUrl = Config.getInstance().getString("server_reset_counter_url");
        String serialNumber = Global.getInstance().getToken();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("serial_number", serialNumber);
        parameters.put("amd100_qty", String.valueOf(amd100Qty));
        parameters.put("amd200_qty", String.valueOf(amd200Qty));
        parameters.put("amd500_qty", String.valueOf(amd500Qty));
        return postToServer(postUrl, parameters);
    }

    private String pingToServer() {
        String postUrl = Config.getInstance().getString("server_ping_url");
        String deviceTitle = Config.getInstance().getString("device_title");
        String serverPingUrl = Config.getInstance().getString("server_ping_url");
        String serverImagePostUrl = Config.getInstance().getString("server_image_post_url");
        String cameraAvailable = Config.getInstance().getString("camera_available");
        String notWorking = Config.getInstance().getString("not_working");
        String serverHost = Config.getInstance().getString("server_host");
        String serialNumber = Global.getInstance().getToken();
        Integer carwashId = Config.getInstance().getInt("carwash_id");
        //Long amd100QtyFromCache = CoinCounter.getInstance().getAmd100QtyFromCache();
        //Long amd200QtyFromCache = CoinCounter.getInstance().getAmd200QtyFromCache();
       // Long amd500QtyFromCache = CoinCounter.getInstance().getAmd500QtyFromCache();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("serial_number", serialNumber);
        parameters.put("carwash_id", String.valueOf(carwashId));
       // parameters.put("amd_100_qty", String.valueOf(amd100QtyFromCache));
       // parameters.put("amd_200_qty", String.valueOf(amd200QtyFromCache));
       // parameters.put("amd_500_qty", String.valueOf(amd500QtyFromCache));
        parameters.put("device_title", deviceTitle);
        parameters.put("server_ping_url", serverPingUrl);
        parameters.put("server_image_post_url", serverImagePostUrl);
        parameters.put("camera_available", cameraAvailable);
        parameters.put("not_working", notWorking);
        parameters.put("server_host", serverHost);
        return postToServer(postUrl, parameters);
    }

    public void postPictureToServer(String postUrl, BufferedImage bi) {
        String iFileName = "image.jpg";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String serialNumber = Global.getInstance().getToken();
        try {
            String serverHost = Config.getInstance().getString("server_host");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "JPG", baos);
            byte[] bytesOut = baos.toByteArray();
            URL connectURL = new URL("http://" + serverHost + postUrl);
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();

            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"token\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(serialNumber);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + iFileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.write(bytesOut);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.flush();
            InputStream is = conn.getInputStream();
            // retrieve the response from server
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String s = b.toString();
            Global.print("post picture: " + s);
            dos.close();
        } catch (Exception ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String postToServer(String postUrl, Map<String, String> parameters) {
        String serverHost = Config.getInstance().getString("server_host");
        URL url;
        try {
            url = new URL("http://" + serverHost + postUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            //con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            String urlParameters = "";
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String paramName = entry.getKey();
                String paramValue = entry.getValue();
                String encodedParamValue = URLEncoder.encode(paramValue, "UTF-8");
                urlParameters += paramName + "=" + encodedParamValue + "&";
            }
            if (urlParameters.endsWith("&")) {
                urlParameters = urlParameters.substring(0, urlParameters.length() - 1);
            }
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String serverResponse = response.toString();
            return serverResponse;
        } catch (Exception ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!checkConnectionToServer()) {
                continue;
            }
           // String serverJsonResponse = pingToServer();
           // Global.print("server response: " + serverJsonResponse);
            //doDeviceActions(serverJsonResponse);
            Global.delay(2000);
        }

    }

    private void doDeviceActions(String serverJsonResponse) {
        if (serverJsonResponse == null || serverJsonResponse.isEmpty()) {
            return;
        }
        try {
            JSONParser parser = new JSONParser();
            JSONObject responseObject = (JSONObject) parser.parse(serverJsonResponse);
            JSONArray actions = (JSONArray) responseObject.get("actions");
            if (!Objects.isNull(actions)) {
                for (Object action : actions) {
                    JSONObject jsonAction = (JSONObject) action;
                    if (jsonAction.containsKey("action")) {
                        String actionString = (String) jsonAction.get("action");
                        switch (actionString) {
                            case "reset_counter":
                                resetDeviceCounterAction();
                                break;
                            case "restart_device":
                                restartDeviceAction();
                                break;                           
                            case "set_config_param":
                                setConfigParam(jsonAction);
                                break;                            
                            default:
                                Global.print("Unknown Action:" + actionString);
                                break;
                        }
                    }
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void resetDeviceCounterAction() {
        //CoinCounter.getInstance().reset();
    }

    private void restartDeviceAction() {
        LinuxCommandsUtil.rebootSystem();
        Global.print("System is now rebooting...");
    }

    

    private void setConfigParam(JSONObject jsonAction) {
        String variableName = (String) jsonAction.get("variable_name");
        String value = (String) jsonAction.get("value");
        Config.getInstance().setValue(variableName, value);
    }

}
