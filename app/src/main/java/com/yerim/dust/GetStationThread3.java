package com.yerim.dust;

import android.os.Handler;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

public class GetStationThread3 extends Thread {
    static public boolean active = false;
    int data = 0;
    public boolean isreceiver;
    String stotalCount;
    String[] sStationName, sAddr, sTm;
    boolean bStationName, btotalCount, bAddr, bTm;
    boolean tResponse;
    String sidoName;
    Handler handler;
    String stationUrl; String addr= "addr=";
    String xGrid = "tmX="; String yGrid = "tmY=";
    String key = "w92QPUmJTmYwIVQK3bC%2FuZjvZmOOFue9li%2B3TLsCkxZwQjCYpQMAAwKn5xcGd%2BPsbPJysNWrCyBTIuRe10ZtUw%3D%3D";
    int getAPI = 0;

    public GetStationThread3(boolean receiver, String sido) {

        Log.w("시도 이름", sido);
        handler = new Handler();
        isreceiver = receiver;
        try {
            addr += URLEncoder.encode(sido, "utf-8");
        } catch (Exception e) {

        }
        getAPI = 1;
        stationUrl = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getMsrstnList?"
                + "addr=" +sido+ "&numOfRows=200&ServiceKey=" + key;
    }

    public GetStationThread3(boolean receiver, String gridy, String gridx) {

        Log.w("받은 TM좌표", gridx+","+gridy);
        handler = new Handler();
        isreceiver = receiver;
        xGrid += 244148.546388; yGrid += 412423.75772;
        getAPI = 2;
        stationUrl = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?"
                + xGrid+"&"+yGrid+"&"+ "&numOfRows=200&ServiceKey=" + key;
    }

    public void run(){

        if(active){
            try{
                bStationName = bAddr = bTm = false;
                sStationName = new String[100]; //측정소
                sAddr = new String[100]; //주소
                sTm = new String[100]; //거리
                data = 0;
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
//                String queryUrl = "http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getMsrstnList?"
//                        + "addr=" +sidoName+"&numOfRows=50&ServiceKey=" + key;
                Log.w("url check ", stationUrl);
                URL url=new URL(stationUrl);		//URL
                InputStream is=url.openStream();
                xpp.setInput(is,"UTF-8");			//utf-8 setting
                int eventType=xpp.getEventType();

                while(eventType!= XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                    case XmlPullParser.START_TAG:
                        if(xpp.getName().equals("stationName")){ //측정일시
                            bStationName = true;
                        }
                        if(xpp.getName().equals("addr")){ //측정일시
                            bAddr = true;
                        }
                        if(xpp.getName().equals("tm")){ //측정일시
                            bTm = true;
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
                        if(bAddr){
                            sAddr[data] = xpp.getText();
                            bAddr = false;
                        }
                        if(bTm){
                            sTm[data] = xpp.getText();
                            bTm = false;
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
                        if(xpp.getName().equals("tm")){
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
                    Log.e("station cnt",""+stotalCount);
                    if(getAPI==1)
                        MainActivity3.StationThreadResponse(stotalCount, sStationName);
                    else if(getAPI==2)
                        MainActivity3.NearStationThreadResponse(sStationName, sAddr, sTm);
                    data=0;
                }
            }
        });
    }
}