package com.example.wildanifqie.aplikasipenampilkode;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Wildanifqie on 29/12/2017.
 */

public class ConnectInternetTask  extends AsyncTaskLoader<String>{
    private String url;

    public ConnectInternetTask(Context context, String url) {
        super(context);
        this.url=url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        InputStream in;

        try {
            URL myUrl = new URL(url);
            HttpURLConnection myConn = (HttpURLConnection) myUrl.openConnection();
            myConn.setReadTimeout(10000);
            myConn.setConnectTimeout(20000);
            myConn.setRequestMethod("GET");
            myConn.connect();

            in = myConn.getInputStream();

            BufferedReader myBuf = new BufferedReader(new InputStreamReader(in));
            StringBuilder st = new StringBuilder();
            String line="";

            while ((line = myBuf.readLine()) != null) {
                st.append(line+" \n");
            }

            myBuf.close();
            in.close();

            return st.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error Get Page Source";
    }
}
