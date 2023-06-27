package com.yerim.dust;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText edit;
    TextView text;

    String key="w92QPUmJTmYwIVQK3bC%2FuZjvZmOOFue9li%2B3TLsCkxZwQjCYpQMAAwKn5xcGd%2BPsbPJysNWrCyBTIuRe10ZtUw%3D%3D";

    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m2);

        edit= (EditText)findViewById(R.id.edit);
        text= (TextView)findViewById(R.id.text);
    }

    public void mOnClick(View v)
    {
        switch (v.getId()){
            case R.id.button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        data = getXmlData(); // 아래 메소드를 호출하여 XML data parsing, String 객체로 얻어오기

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText(data); //textView에 문자열 data 출력
                            }
                        });
                    }
                }).start();
                break;
        }
    }


    String getXmlData() { //XML data parsing (api 호출)
        StringBuffer buffer = new StringBuffer();

        String str = edit.getText().toString();
        String sidoName = URLEncoder.encode(str);

        //요청 URL
        String queryUrl = "http://apis.data.go.kr/B552584/ArpltnStatsSvc/getCtprvnMesureSidoLIst?"
                + "sidoName=" + sidoName  +"&searchCondition=DAILY&&pageNo=1&numOfRows=1000&ServiceKey=" + key;

        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//태그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("sidoName")){
                            buffer.append("주소 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//addr 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("dataTime")){
                            buffer.append("측정일시 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("sidoName")){
                            buffer.append("시도명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("cityName")){
                            buffer.append("시군구 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("so2Value")){
                            buffer.append("아황산가스 평균농도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        }
                        else if(tag.equals("o3Value")){
                            buffer.append("오존 평균농도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        }
                        else if(tag.equals("no2Value")){
                            buffer.append("이산화질소 평균농도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        }
                        else if(tag.equals("pm10Value")){
                            buffer.append("미세먼지(PM10) \n" +
                                    "평균농도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("  ,  ");
                        }
                        else if(tag.equals("pm25Value")){
                            buffer.append("미세먼지(PM2.5) \n" +
                                    "평균농도 :");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //태그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈

                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }

        buffer.append("파싱 끝\n");

        return buffer.toString();//StringBuffer 문자열 객체 반환

    }

}
