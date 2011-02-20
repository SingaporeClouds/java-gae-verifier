package com.singpath.verifier;

import java.net.*;
import java.io.*;

public class URLConnectionReader {
    public String read(String url) throws Exception {
        URL yahoo = new URL(url);
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
        String inputLine = "";
        String output = "";

        while ((inputLine = in.readLine()) != null) 
        	output += inputLine;
        in.close();
        return output;
    }
}