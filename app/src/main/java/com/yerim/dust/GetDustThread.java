package com.yerim.dust;

import android.os.Handler;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;

public class GetDustThread extends Thread {
    static public boolean active = false;
    int data = 0;
    public boolean isreceiver;
    String stotalCount;
    String[] stvAddr, stvDate, stvpm10, stvpm10grade, stvpm25, stvpm25grade,
            stvo3, stvo3grade, stvno2, stvno2grade, stvco, stvcograde, stvso2, stvso2grade;
    boolean btotalCount, btvAddr, btvDate, btvpm10, btvpm10grade, btvpm25, btvpm25grade,
            btvo3, btvo3grade, btvno2, btvno2grade, btvco, btvcograde, btvso2, btvso2grade;
    boolean tResponse;
    String dongName;
    Handler handler;
    String key = "w92QPUmJTmYwIVQK3bC%2FuZjvZmOOFue9li%2B3TLsCkxZwQjCYpQMAAwKn5xcGd%2BPsbPJysNWrCyBTIuRe10ZtUw%3D%3D";

    public GetDustThread(boolean receiver, String dong){

        Log.w("dong name", dong);
        handler = new Handler();
        isreceiver = receiver;
        dongName = dong;

        btotalCount=btvAddr=btvDate=btvpm10=btvpm10grade=btvpm25=btvpm25grade=btvo3
                =btvo3grade=btvno2=btvno2grade=btvco=btvcograde=btvso2=btvso2grade=false;
    }

    public void run(){

        if(active){
            try{
                stvAddr = new String[100]; stvDate = new String[100]; stvpm10 = new String[100];
                stvpm10grade = new String[100]; stvpm25 = new String[100]; stvpm25grade = new String[100];
                stvo3 = new String[100]; stvo3grade = new String[100]; stvno2 = new String[100];
                stvno2grade = new String[100]; stvco = new String[100]; stvcograde = new String[100];
                stvso2 = new String[100]; stvso2grade = new String[100];
                data = 0;
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                String queryUrl = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?"
                        + "stationName=" +dongName+"&dataTerm=DAILY&numOfRows=50&ServiceKey=" + key;
                Log.w("url check ", queryUrl);
                URL url=new URL(queryUrl);		//URL
                InputStream is=url.openStream();
                xpp.setInput(is,"UTF-8");
                int eventType=xpp.getEventType();

                while(eventType!= XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                    case XmlPullParser.START_TAG:
                        if(xpp.getName().equals("dataTime")){ //측정일시
                            btvDate = true;
                        }
                        if(xpp.getName().equals("cityName")){ //측정장소
                            btvAddr = true;
                        }
                        if(xpp.getName().equals("pm10Value")){ //미세먼지
                            btvpm10 = true;
                        }
                        if(xpp.getName().equals("pm10Grade")){ //미세먼지 등급
                            btvpm10grade = true;
                        }
                        if(xpp.getName().equals("pm25Value")){ //초미세먼지
                            btvpm25 = true;
                        }
                        if(xpp.getName().equals("pm25Grade")){ //초미세먼지 등급
                            btvpm25grade = true;
                        }
                        if(xpp.getName().equals("o3Value")){ //오존
                            btvo3 = true;
                        }
                        if(xpp.getName().equals("o3Grade")){ //오존 지수
                            btvo3grade = true;
                        }
                        if(xpp.getName().equals("no2Value")){ //이산화질소
                            btvno2 = true;
                        }
                        if(xpp.getName().equals("no2Grade")){ //이산화질소 지수
                            btvno2grade = true;
                        }
                        if(xpp.getName().equals("coValue")){ //일산화탄소
                            btvco = true;
                        }
                        if(xpp.getName().equals("coGrade")){ //일산화탄소 지수
                            btvcograde = true;
                        }
                        if(xpp.getName().equals("so2Value")){ //아황산가스
                            btvso2 = true;
                        }
                        if(xpp.getName().equals("so2Grade")){ //아황산가스 지수
                            btvso2grade = true;
                        }
                        if(xpp.getName().equals("totalCount")){
                            btotalCount = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if(btvDate){
                            stvDate[data] = xpp.getText();
                            btvDate = false;
                        }
                        if(btvAddr){
                            stvAddr[data] = xpp.getText();
                            btvAddr = false;
                        }
                        if(btvpm10){
                            stvpm10[data] = xpp.getText();
                            btvpm10 = false;
                        }
                        if(btvpm10grade){
                            stvpm10grade[data] = xpp.getText();
                            btvpm10grade = false;
                        }
                        if(btvpm25){
                            stvpm25[data] = xpp.getText();
                            btvpm25 = false;
                        }
                        if(btvpm25grade){
                            stvpm25grade[data] = xpp.getText();
                            btvpm25grade = false;
                        }
                        if(btvo3){
                            stvo3[data] = xpp.getText();
                            btvo3 = false;
                        }
                        if(btvo3grade){
                            stvo3grade[data] = xpp.getText();
                            btvo3grade = false;
                        }
                        if(btvno2){
                            stvno2[data] = xpp.getText();
                            btvno2 = false;
                        }
                        if(btvno2grade){
                            stvno2grade[data] = xpp.getText();
                            btvno2grade = false;
                        }
                        if(btvco){
                            stvco[data] = xpp.getText();
                            btvco = false;
                        }
                        if(btvcograde){
                            stvcograde[data] = xpp.getText();
                            btvcograde = false;
                        }
                        if(btvso2){
                            stvso2[data] = xpp.getText();
                            btvso2 = false;
                        }
                        if(btvso2grade){
                            stvso2grade[data] = xpp.getText();
                            btvso2grade = false;
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
                        if(xpp.getName().equals("item")){
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

    private void view_text(){

        handler.post(new Runnable() {
            @Override
            public void run() {

                active=false;
                if(tResponse){
                    tResponse=false;
                    data=0;
                    MainActivity2.DustThreadResponse(stotalCount, stvDate, stvpm10, stvpm10grade, stvpm25, stvpm25grade,
                            stvo3, stvo3grade, stvno2, stvno2grade, stvco, stvcograde, stvso2, stvso2grade);
                }
            }
        });
    }
}