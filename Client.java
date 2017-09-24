import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) throws Exception {
          try{
              ServerSocket s = new ServerSocket(34000);
              while(true){
                Socket clientsocket = s.accept();
                System.out.println("connection accepted");
                BufferedReader in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
                try {
                        String url=in.readLine();
                        System.out.println(url);
                        if(url.isEmpty()){
                          continue;
                        }
                        if(url.startsWith("http://")){
                            String toClient = sendPost(url);
                            if(!toClient.isEmpty()) {
                                if (Desktop.isDesktopSupported()) {
                                    // Windows
                                    Desktop.getDesktop().browse(new URI(toClient));
                                } else {
                                    // Ubuntu
                                    Runtime runtime = Runtime.getRuntime();
                                    runtime.exec("/usr/bin/firefox -new-window " + url);
                                }
                            }
                        }
                 }
                catch (Exception e) {
                    System.out.println(e.toString());
                    continue;
                }
              }
          }
          catch (Exception e) {
            System.out.println(e.toString());
          }
    }

    private static String sendPost(String url) throws Exception {
        String[] splitUrl = url.split("\\?");
        url = splitUrl[0];

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = splitUrl[1];

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
        return response.toString().trim();
    }
}