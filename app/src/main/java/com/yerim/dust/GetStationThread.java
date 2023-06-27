package com.yerim.dust;

import android.os.Handler;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;

public class GetStationThread extends Thread {
    static public boolean active = false;
    int data = 0;
    public boolean isreceiver;
    String stotalCount;
    String[] sStationName;
    boolean bStationName, btotalCount;
    boolean tResponse;
    String sidoName;
    Handler handler;
    String key = "w92QPUmJTmYwIVQK3bC%2FuZjvZmOOFue9li%2B3TLsCkxZwQjCYpQMAAwKn5xcGd%2BPsbPJysNWrCyBTIuRe10ZtUw%3D%3D";

    public GetStationThread(boolean receiver, String sido){

        Log.w("시도 이름", sido);
        handler = new Handler();
        isreceiver = receiver;
        sidoName = sido;

       bStationName = false;
    }

    public void run(){

        if(active){
            try{
                sStationName = new String[100];
                data = 0;
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                String queryUrl = "http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getMsrstnList?"
                        + "addr=" +sidoName+"&numOfRows=50&ServiceKey=" + key;
                Log.w("url check ", queryUrl);
                URL url=new URL(queryUrl);		//URL
                InputStream is=url.openStream();
                xpp.setInput(is,"UTF-8");			//utf-8 setting
                int eventType=xpp.getEventType();

                while(eventType!= XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                    case XmlPullParser.START_TAG:
                        if(xpp.getName().equals("stationName")){ //측정일시
                            bStationName = true;
                        }
                        if(xpp.getName().equals("totalCount")){ //측정장소
                            btotalCount = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if(bStationName){
                            sStationName[data] = xpp.getText();
                            bStationName = false;
                        }
                        if(btotalCount){
                            stotalCount = xpp.getText();
                            btotalCount = false;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(xpp.getName().equals("response")){
                            tResponse = true;
                            view_text();
                        }
                        if(xpp.getName().equals("dmY")){
                            data++;
                        }
                        break;
                    }
                    eventType = xpp.next();
                }

            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void view_text() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                active=false;
                if(tResponse){
                    tResponse=false;
                    Log.e("station cnt",""+data);
                    MainActivity2.StationThreadResponse(data, sStationName);
                    data=0;
                }
            }
        });
    }
}