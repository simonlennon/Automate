package com.simonlennon.automate.getip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

/**
 * Created by simon.lennon on 10/10/2014.
 */
public class GetExternalIP {

    public static void main(String[] args){
        try {
            System.out.print(getIP("localhost:9090"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }

    public static String getIP(String routerIp) throws IOException {

        URL url = new URL("http://"+routerIp+"/htmlcode/html/index_content.asp");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        InputStream inputStream = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while((line=reader.readLine())!=null){
            if(line.indexOf("var ppp_info")>-1){
                return extractIP(line);
            }
        }

        return null;
    }

    private static String extractIP(String htmlLine){
        StringTokenizer st = new StringTokenizer(htmlLine,"\"");
        String ip = null;
        for (int i=0;i<6;i++){
            ip = st.nextElement().toString();
        }
        return ip;
    }


}
