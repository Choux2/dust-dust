package com.yerim.dust;

import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetCoordThread4 extends Thread {
    static public boolean active = false;
    int data = 0;
    public boolean isreceiver;
    String getX, getY;
    String gridx, gridy, coordfrom, coordto;
    Handler handler;
    String key = "c7a2f1eec2766ae52940e7d6949b77a2";
    String x, y;
    boolean parserEnd = false;
    public GetCoordThread4(boolean receiver, String x, String y, String from, String to)
    {
        Log.e("스레드 받은 파라메터", x +" " +y +" " + from +" " + to);
        handler=new Handler();
        isreceiver=receiver;
        gridx=x; gridy=y; coordfrom=from; coordto=to;
        getX=getY=null;
    }

    public void run(){

        if(active){
            InputStream is = null;
            try{
                parserEnd = false;
                data = 0;
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                String queryUrl = "https://dapi.kakao.com/v2/local/geo/transcoord.json?"
                        +"x="+gridx+"&y="+gridy+"&input_coord="+coordfrom+"&output_coord="+coordto;
                URL url = new URL(queryUrl);
                Log.e("get transcoord", queryUrl);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

                String json = "";
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();

                httpCon.setRequestProperty("Authorization",key);
                httpCon.setRequestProperty("Accept", "application/json");
                httpCon.setRequestProperty("Content-type", "application/json");
                httpCon.setRequestMethod("GET");

                httpCon.setDoOutput(true);
                httpCon.setDoInput(true);

                OutputStream os = httpCon.getOutputStream();
                os.write(json.getBytes("euc-kr"));
                os.flush();

                String str, receiveMsg;

                if (httpCon.getResponseCode() == httpCon.HTTP_OK) {
                    is = httpCon.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(is, "UTF-8");
                    // convert inputstream to string
                    Log.e("responseBodyReader", responseBodyReader.toString().length() + "");
                    BufferedReader reader = new BufferedReader(responseBodyReader);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                    //Log.i("receiveMsg : ", receiveMsg);
                    reader.close();
                    JSONObject jobject=new JSONObject(receiveMsg);

                    String filter=jobject.getString("documents");
                    Log.e("filter",filter);

                    JSONArray jArray=new JSONArray(filter);
                    jobject=jArray.getJSONObject(0);

                    x=jobject.getString("x");
                    y=jobject.getString("y");
                    parserEnd=true;
                    Log.e("검색결과", x+" "+y);
                }else{
                    Log.e("통신 결과", httpCon.getResponseCode() + "에러");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            if(parserEnd){

                showtext();
            }
        }
}

    private void showtext() {
        handler.post((new Runnable() {
            @Override
            public void run() {
                active=false;
                MainActivity4.TransCoordThreadResponse(x, y);
            }
        }));
    }
}
