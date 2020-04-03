package com.example.hansol.xml_200321_hs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Page4_1 extends AppCompatActivity {

    //위젯
    WebView web;
    TextView routeDetail_page4_1;
    SubwayController controller;
    SubwayBuilder builder = new SubwayBuilder();
    Subway subway = null;

    String[] get_text;

    //쓰레기값을 걸러준 문자를 넣기위함
    String[] text = new String[8];

    //앞페이지에서 넘어온 값을 받음
    String[] next_text = new String[8];

    //코드와 이름
    String[] code  = new String[8];
    String[] name  = new String[8];

    //입력받은 역을 개수를 저장
    int number = 0;

    public Activity main = Page4_1.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4_1);

        //위젯연결
        web = (WebView) findViewById(R.id.mabView_page4_1);
        routeDetail_page4_1 = (TextView)findViewById(R.id.routeDetail_page4_1);

        //앞페이지에서 값 받아옴
        Intent intent = getIntent();
        next_text[0] = intent.getExtras().getString("text0");
        next_text[1] = intent.getExtras().getString("text1");
        next_text[2] = intent.getExtras().getString("text2");
        next_text[3] = intent.getExtras().getString("text3");
        next_text[4] = intent.getExtras().getString("text4");
        next_text[5] = intent.getExtras().getString("text5");
        next_text[6] = intent.getExtras().getString("text6");
        next_text[7] = intent.getExtras().getString("text7");


        //맨 앞에 있던 값은 쓰레기 값이 같이 있음, 그래서 쓰레기값을 걸러주는 작업이 필요
        //앞에서 입력한 것만 작업해줌
        for (int i = 0; i < 8; i++) {
            if (next_text[i] != null && i < 7) {
                //값을 코드/이름으로 분리함
                String[] code_name = next_text[i].split(",");
                code[i] = code_name[0];
                name[i] = code_name[1];

                text[i] = code[i].replaceAll("[^0-9]+", "");  //문자열에서 int형만 추출한다.
                //Toast.makeText(getApplicationContext(), i+"???????????,", Toast.LENGTH_SHORT).show();
                number++;
            } else if(i==7){
                number++;
                String[] code_name = next_text[i].split(",");
                code[i] = code_name[0];
                name[i] = code_name[1];

                text[i] = code[i].replaceAll("[^0-9]+", "");  //문자열에서 int형만 추출한다.
                //Toast.makeText(getApplicationContext(), i+"???????????,", Toast.LENGTH_SHORT).show();
            }
        }
        Toast.makeText(getApplicationContext(), number+"몇갠데,,,", Toast.LENGTH_SHORT).show();


        // 빌더를 생성한다. - 여기서 txt 파일 받아옴
        try {
            builder.readFile(getApplicationContext(), "station3.txt", "link3.txt");
        } catch (SubwayException ex) {
            ex.printStackTrace();
            Log.i("여기서부터 에러가 난거네,,,", "에러남");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.i("에러다", "에러남");
        }


        // 지하철 클래스를 만든다.
        subway = builder.build();


        // 검색을 위한 컨트롤러를 만든든다.
        controller = new SubwayController(subway);


        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setDisplayZoomControls(false);  //웹뷰 돋보기 없앰


        //웹뷰 줌기능
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setSupportZoom(true);
        web.setWebViewClient(new WebViewClient());
        //웹뷰를 로드함
        web.loadUrl("file:///android_asset/index3.html");


        //앞에서 받은 값이 뭐냐에 따라 알고리즘 다르게 진행
        switch (number) {
            case 3:
                //경유지가 1개인 경우
                //0.5초 후, 지도위에 그림
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable()  {
                    public void run() {
                        // 시간 지난 후 실행할 코딩
                        web.loadUrl("javascript:setMessage('"+name[0]+"')");
                        web.loadUrl("javascript:setMessage2('"+name[1]+"')");
                        web.loadUrl("javascript:setMessage3('"+name[7]+"')");
                    }
                }, 500); // 0.5초후

                break;

            case 4:
                //경유지가 2개인 경우
                final String [] result_split = compare_2(text[0], text[1], text[2], text[7]).split(",");

                //0.5초 후, 지도위에 그림
                mHandler = new Handler();
                mHandler.postDelayed(new Runnable()  {
                    public void run() {
                        // 시간 지난 후 실행할 코딩
                        web.loadUrl("javascript:setMessage('"+name[0]+"')");
                        int count = 2;
                        for(int i=1; i < 3; i++){
                            for(int j=1; j < 3; j++){
                                if(result_split[i].equals(code[j])){
                                    web.loadUrl("javascript:setMessage"+ count +"('"+name[j]+"')");
                                    count++;
                                }
                            }
                        }
                        web.loadUrl("javascript:setMessage4('"+name[7]+"')");
                    }
                },500); // 0.5초후

                break;


            case 5:
                //경유지가 3개인 경우
                final String [] result_split2 = compare_3(text[0], text[1], text[2], text[3],text[7]).split(",");

                //0.5초 후, 지도위에 그림
                mHandler = new Handler();
                mHandler.postDelayed(new Runnable()  {
                    public void run() {
                        // 시간 지난 후 실행할 코딩
                        web.loadUrl("javascript:setMessage('"+name[0]+"')");
                        int count = 2;
                        for(int i=1; i < 4; i++){
                            for(int j=1; j < 4; j++){
                                if(result_split2[i].equals(code[j])){
                                    Log.i("여기다 이녀석아!1", count+name[j]);
                                    web.loadUrl("javascript:setMessage"+ count +"('"+name[j]+"')");
                                    count++;
                                }
                            }
                        }
                        web.loadUrl("javascript:setMessage5('"+name[7]+"')");
                    }
                },500); // 0.5초후
                break;


            case 6:
                //경유지가 4개인 경우
                final String [] result_split3 = compare_4(text[0], text[1], text[2], text[3], text[4],text[7]).split(",");
                routeDetail_page4_1.setText(compare_4(text[0], text[1], text[2], text[3], text[4],text[7]));

                //0.5초 후, 지도위에 그림
                mHandler = new Handler();
                mHandler.postDelayed(new Runnable()  {
                    public void run() {
                        // 시간 지난 후 실행할 코딩
                        web.loadUrl("javascript:setMessage('"+name[0]+"')");
                        int count = 2;
                        for(int i=1; i < 5; i++){
                            for(int j=1; j < 5; j++){
                                if(result_split3[i].equals(code[j])){
                                    web.loadUrl("javascript:setMessage"+ count +"('"+name[j]+"')");
                                    Log.i("여기다 문정아앙", count+name[j]);
                                    count++;
                                }
                            }
                        }
                        web.loadUrl("javascript:setMessage6('"+name[7]+"')");
                    }
                },500); // 0.5초후
                break;



            case 7:
                //경유지가 5개인 경우
                final String [] result_split4 = compare_5(text[0], text[1], text[2], text[3], text[4], text[5], text[6]).split(",");
                routeDetail_page4_1.setText(compare_5(text[0], text[1], text[2], text[3], text[4], text[5], text[6]));

                //0.5초 후, 지도위에 그림
                mHandler = new Handler();
                mHandler.postDelayed(new Runnable()  {
                    public void run() {
                        // 시간 지난 후 실행할 코딩
                        web.loadUrl("javascript:setMessage('"+name[0]+"')");
                        int count = 2;
                        for(int i=1; i < 6; i++){
                            for(int j=1; j < 6; j++){
                                if(result_split4[i].equals(code[j])){
                                    web.loadUrl("javascript:setMessage"+ count +"('"+name[j]+"')");
                                    count++;
                                }
                            }
                        }
                        web.loadUrl("javascript:setMessage7('"+name[7]+"')");
                    }
                },500); // 0.5초후
                break;

            default:
                Toast.makeText(getApplicationContext(), "어이쿠", Toast.LENGTH_SHORT).show();
                break;
        }


    }




    //경유지가 1개인 경우
    private String middle_number_1(String station1, String station2, String station3) {
        String search_1 = controller.search(station1, station2); //출발->경유1
        String search_2 = controller.search(station2, station3); //경유1->도착

        return station1 + "," + station2 + "," + station3;
    }


    //경유지가 2개인 경우
    public String middle_number_2(String station1, String station2, String station3) {

        String search_1 = controller.search(station1, station2);  //출발->경유1
        String search_2 = controller.search(station1, station3);  //출발->경유2

        //size(지나간 역 개수)만 추출한다.
        int search_1_int = Integer.parseInt(search_1.replaceAll("[^0-9]", ""));
        int search_2_int = Integer.parseInt(search_2.replaceAll("[^0-9]", ""));

        //size 비교해서 최소값을 반환한다. (값이 같으면 첫번째를 반환함)
        int min = Math.min(search_1_int, search_2_int);

        //station2가 첫번째 경유일때
        if (min == search_1_int) {
            return station2;
        }
        //station3이 첫번째 경유일때
        else {
            return station3;
        }
    }


    //경유지가 3개인 경우
    public String middle_number_3(String station1, String station2, String station3, String station4) {

        String middle1 = middle_number_2(station1, station2, station3);
        String search_1 = controller.search(station1, middle1);  //출발->경유1
        String search_2 = controller.search(station1, station4);  //출발->경유2

        //size(지나간 역 개수)만 추출한다.
        int search_1_int = Integer.parseInt(search_1.replaceAll("[^0-9]", ""));
        int search_2_int = Integer.parseInt(search_2.replaceAll("[^0-9]", ""));

        //size 비교해서 최소값을 반환한다. (값이 같으면 첫번째를 반환함)
        int min = Math.min(search_1_int, search_2_int);

        if (min == search_1_int) {
            return middle1;
        }

        //station4이 첫번째 경유일때
        else
            return station4;
    }


    //경유지가 4개인 경우
    public String middle_number_4(String station1, String station2, String station3, String station4, String station5) {

        String middle1 = middle_number_2(station1, station2, station3);
        String middle2 = middle_number_2(station1, station4, station5);

        String search_1 = controller.search(station1, middle1);  //출발->경유1
        String search_2 = controller.search(station1, middle2);  //출발->경유2

        //size(지나간 역 개수)만 추출한다.
        int search_1_int = Integer.parseInt(search_1.replaceAll("[^0-9]", ""));
        int search_2_int = Integer.parseInt(search_2.replaceAll("[^0-9]", ""));

        //size 비교해서 최소값을 반환한다. (값이 같으면 첫번째를 반환함)
        int min = Math.min(search_1_int, search_2_int);

        if (min == search_1_int) {
            return middle1;
        }

        //station4이 첫번째 경유일때
        else {
            return middle2;
        }
    }


    //경유지가 5개인 경우
    public String middle_number_5(String station1, String station2, String station3, String station4, String station5, String station6) {

        String middle1 = middle_number_3(station1, station2, station3, station4);
        String middle2 = middle_number_2(station1, station5, station6);

        String search_1 = controller.search(station1, middle1);  //출발->경유1
        String search_2 = controller.search(station1, middle2);  //출발->경유2

        //size(지나간 역 개수)만 추출한다.
        int search_1_int = Integer.parseInt(search_1.replaceAll("[^0-9]", ""));
        int search_2_int = Integer.parseInt(search_2.replaceAll("[^0-9]", ""));

        //size 비교해서 최소값을 반환한다. (값이 같으면 첫번째를 반환함)
        int min = Math.min(search_1_int, search_2_int);

        if (min == search_1_int) {
            return middle1;
        } else
            return middle2;
    }


    //경유지가 6개인 경우
    public String middle_number_6(String station1, String station2, String station3, String station4, String station5, String station6, String station7) {

        String middle1 = middle_number_3(station1, station2, station3, station4);
        String middle2 = middle_number_3(station1, station5, station6, station7);

        String search_1 = controller.search(station1, middle1);  //출발->경유1
        String search_2 = controller.search(station1, middle2);  //출발->경유2

        //size(지나간 역 개수)만 추출한다.
        int search_1_int = Integer.parseInt(search_1.replaceAll("[^0-9]", ""));
        int search_2_int = Integer.parseInt(search_2.replaceAll("[^0-9]", ""));

        //size 비교해서 최소값을 반환한다. (값이 같으면 첫번째를 반환함)
        int min = Math.min(search_1_int, search_2_int);

        if (min == search_1_int) {
            return middle1;
        } else
            return middle2;
    }


    //경유 2개 비교
    public String compare_2(String station1, String station2, String station3, String station4) {
        if (middle_number_2(station1, station2, station3) == station2) {
            return middle_number_1(station1, station2, station3) + "," + station4;
        } else {
            return middle_number_1(station1, station3, station2) + "," + station4;
        }
    }


    //경유 3개 비교
    public String compare_3(String station1, String station2, String station3, String station4, String station5) {
        //첫번째 경유역이 station2
        if (middle_number_3(station1, station2, station3, station4) == station2) {

            //두번째 경유역이 station3
            if (middle_number_2(station2, station3, station4) == station3) {
                return compare_2(station1, station2, station3, station4) + ","+ station5;
            }
            //두번째 경유역이 station4
            else {
                return compare_2(station1, station2, station4, station3) + ","+ station5;
            }
        }

        //첫번째 경유역이 station3
        else if (middle_number_3(station1, station2, station3, station4) == station3) {

            //두번째 경유역이 station2
            if (middle_number_2(station3, station2, station4) == station2) {
                return compare_2(station1, station3, station2, station4) + ","+ station5;
            }
            //두번째 경유역이 station4
            else {
                return compare_2(station1, station3, station4, station2) + ","+ station5;
            }
        }

        //첫번째 경유역이 station4
        else {
            //두번째 경유역이 station2
            if (middle_number_2(station4, station2, station3) == station2) {
                return compare_2(station1, station4, station2, station3) + ","+ station5;
            }
            //두번째 경유역이 station3
            else {
                return compare_2(station1, station4, station3, station2) + ","+ station5;
            }
        }
    }


    //경유 4개 비교
    public String compare_4(String station1, String station2, String station3, String station4, String station5, String station6) {

        //첫번째 경유역이 station2
        if (middle_number_4(station1, station2, station3, station4, station5) == station2) {

            //두번째 경유역이 station3
            if (middle_number_3(station2, station3, station4, station5) == station3) {

                //세번째 경유역이 station4  ....(이 방법밖에 없을까...)
                if (middle_number_2(station3, station4, station5) == station4) {
                    String search = controller.search(station5, station6);  //경유4->도착
                    return compare_3(station1, station2, station3, station4, station5)  + "," + station6;
                }
                //세번째 경유역이 station5
                else {
                    String search = controller.search(station4, station6);  //경유4->도착
                    return compare_3(station1, station2, station3, station5, station4)  + "," + station6;
                }
            }

            //두번째 경유역이 station4
            else if (middle_number_3(station2, station3, station4, station5) == station4) {

                //세번째 경유역이 station3
                if (middle_number_2(station4, station3, station5) == station3) {
                    String search = controller.search(station5, station6);  //경유4->도착
                    return compare_3(station1, station2, station4, station3, station5) + "," + station6;
                }

                //세번째 경유역이 station5
                else {
                    String search = controller.search(station3, station6);  //경유4->도착
                    return compare_3(station1, station2, station4, station5, station3) + "," + station6;
                }
            }

            //두번째 경유역이 station5
            else{

                //세번째 경유역이 station3
                if (middle_number_2(station5, station3, station4) == station3) {
                    String search = controller.search(station4, station6);  //경유4->도착
                    return compare_3(station1, station2, station5, station3, station4) + "," + station6;
                }

                //세번째 경유역이 station4
                else {
                    String search = controller.search(station3, station6);  //경유4->도착
                    return compare_3(station1, station2, station5, station4, station3) + "," + station6;
                }
            }
        }

        //첫번째 경유역이 station3
        else if (middle_number_4(station1, station2, station3, station4, station5) == station3) {

            //두번째 경유역이 station2
            if (middle_number_3(station3, station2, station4, station5) == station2) {

                //세번째 경유역이 station4
                if (middle_number_2(station2, station4, station5) == station4) {
                    String search = controller.search(station5, station6);  //경유4->도착
                    return compare_3(station1, station3, station2, station4, station5) + "," + station6;
                }
                //세번째 경유역이 station5
                else {
                    String search = controller.search(station4, station6);  //경유4->도착
                    return compare_3(station1, station3, station2, station5, station4) + "," + station6;
                }
            }

            //두번째 경유역이 station4
            else if (middle_number_3(station3, station2, station4, station5) == station4) {

                //세번째 경유역이 station2
                if (middle_number_2(station4, station2, station5) == station2) {
                    String search = controller.search(station5, station6);  //경유4->도착
                    return compare_3(station1, station3, station4, station2, station5) + "," + station6;
                }

                //세번째 경유역이 station5
                else {
                    String search = controller.search(station3, station6);  //경유4->도착
                    return compare_3(station1, station3, station4, station5, station2) + "," + station6;
                }
            }

            //두번째 경유역이 station5
            else {

                //세번째 경유역이 station2
                if (middle_number_2(station5, station2, station4) == station2) {
                    String search = controller.search(station4, station6);  //경유4->도착
                    return compare_3(station1, station3, station5, station2, station4)  + "," + station6;
                }

                //세번째 경유역이 station4
                else {
                    String search = controller.search(station2, station6);  //경유4->도착
                    return compare_3(station1, station3, station5, station4, station2) + "," + station6;
                }
            }
        }

        //첫번째 경유역이 station4
        else if (middle_number_4(station1, station2, station3, station4, station5) == station4) {

            //두번째 경유역이 station2
            if (middle_number_3(station4, station2, station3, station5) == station2) {

                //세번째 경유역이 station3
                if (middle_number_2(station2, station3, station5) == station3) {
                    String search = controller.search(station5, station6);  //경유4->도착
                    return compare_3(station1, station4, station2, station3, station5) + "," + station6;
                }
                //세번째 경유역이 station5
                else {
                    String search = controller.search(station3, station6);  //경유4->도착
                    return compare_3(station1, station4, station2, station5, station3)  + "," + station6;
                }
            }

            //두번째 경유역이 station3
            else if (middle_number_3(station4, station2, station3, station5) == station3) {

                //세번째 경유역이 station2
                if (middle_number_2(station3, station2, station5) == station2) {
                    String search = controller.search(station5, station6);  //경유4->도착
                    return compare_3(station1, station4, station3, station2, station5) + "," + station6;
                }

                //세번째 경유역이 station5
                else {
                    String search = controller.search(station2, station6);  //경유4->도착
                    return compare_3(station1, station4, station3, station5, station2)  + "," + station6;
                }
            }

            //두번째 경유역이 station5
            else  {

                //세번째 경유역이 station2
                if (middle_number_2(station5, station2, station3) == station2) {
                    String search = controller.search(station3, station6);  //경유4->도착
                    return compare_3(station1, station4, station5, station2, station3)  + "," + station6;
                }

                //세번째 경유역이 station3
                else {
                    String search = controller.search(station2, station6);  //경유4->도착
                    return compare_3(station1, station4, station5, station3, station2)  + "," + station6;
                }
            }
        }

        //첫번째 경유역이 station5
        else {

            //두번째 경유역이 station2
            if (middle_number_3(station5, station2, station3, station4) == station2) {

                //세번째 경유역이 station3
                if (middle_number_2(station2, station3, station4) == station3) {
                    String search = controller.search(station4, station6);  //경유4->도착
                    return compare_3(station1, station5, station2, station3, station4)  + "," + station6;
                }
                //세번째 경유역이 station4
                else {
                    String search = controller.search(station3, station6);  //경유4->도착
                    return compare_3(station1, station5, station2, station4, station3) + "," + station6;
                }
            }

            //두번째 경유역이 station3
            else if (middle_number_3(station5, station2, station3, station4) == station3) {

                //세번째 경유역이 station2
                if (middle_number_2(station3, station2, station4) == station2) {
                    String search = controller.search(station4, station6);  //경유4->도착
                    return compare_3(station1, station5, station3, station2, station4) + "," + station6;
                }

                //세번째 경유역이 station5
                else {
                    String search = controller.search(station2, station6);  //경유4->도착
                    return compare_3(station1, station5, station3, station4, station2) + "," + station6;
                }
            }

            //두번째 경유역이 station4
            else  {

                //세번째 경유역이 station2
                if (middle_number_2(station4, station2, station3) == station2) {
                    String search = controller.search(station3, station6);  //경유4->도착
                    return compare_3(station1, station5, station4, station2, station3) + "," + station6;
                }

                //세번째 경유역이 station3
                else {
                    String search = controller.search(station2, station6);  //경유4->도착
                    return compare_3(station1, station5, station4, station3, station2)  + "," + station6;
                }
            }
        }
    }


    //경유 5개 비교
    public String compare_5(String station1, String station2, String station3, String station4, String station5, String station6, String station7) {

        //첫번째 경유역이 station2
        if (middle_number_5(station1, station2, station3, station4, station5, station6) == station2) {

            //두번째 경유역이 station3
            if (middle_number_4(station2, station3, station4, station5, station6) == station3) {

                //세번째 경유역이 station4
                if (middle_number_3(station3, station4, station5, station6) == station4) {

                    //네번재 경유역이 station5
                    if (middle_number_2(station4, station5, station6) == station5) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station2, station3, station4, station5, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station2, station3, station4, station6, station5)+ "," + station7;
                    }
                }

                //세번째 경유역이 station5
                else if (middle_number_3(station3, station4, station5, station6) == station5) {

                    //네번재 경유역이 station4
                    if (middle_number_2(station5, station4, station6) == station4) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station2, station3, station5, station4, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station2, station3, station5, station6, station4) + "," + station7;
                    }
                }

                //세번째 경유역이 station6
                else {

                    //네번재 경유역이 station4
                    if (middle_number_2(station6, station4, station5) == station4) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station2, station3, station6, station4, station5) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station2, station3, station6, station5, station4) + "," + station7;
                    }
                }
            }

            //두번째 경유역이 station4
            else if (middle_number_4(station2, station3, station4, station5, station6) == station4) {

                //세번째 경유역이 station3
                if (middle_number_3(station4, station3, station5, station6) == station3) {

                    //네번재 경유역이 station5
                    if (middle_number_2(station3, station5, station6) == station5) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station2, station4, station3, station5, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station2, station4, station3, station6, station5) + "," + station7;
                    }
                }

                //세번째 경유역이 station5
                else if (middle_number_3(station4, station3, station5, station6) == station5) {

                    //네번재 경유역이 station3
                    if (middle_number_2(station5, station3, station6) == station3) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station2, station4, station5, station3, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station2, station4, station5, station6, station3) + "," + station7;
                    }
                }

                //세번째 경유역이 station6
                else {

                    //네번재 경유역이 station3
                    if (middle_number_2(station6, station3, station5) == station3) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station2, station4, station6, station3, station5) + "," + station7;
                    }

                    //네번재 경유역이 station5
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station2, station4, station6, station5, station3) + "," + station7;
                    }
                }
            }

            //두번째 경유역이 station5
            else if (middle_number_4(station2, station3, station4, station5, station6) == station5) {

                //세번째 경유역이 station3
                if (middle_number_3(station5, station3, station4, station6) == station3) {

                    //네번재 경유역이 station4
                    if (middle_number_2(station3, station4, station6) == station4) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station2, station5, station3, station4, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station2, station5, station3, station6, station4) + "," + station7;
                    }
                }

                //세번째 경유역이 station3
                else if (middle_number_3(station5, station3, station4, station6) == station3) {

                    //네번재 경유역이 station4
                    if (middle_number_2(station3, station4, station6) == station4) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station2, station5, station3, station4, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station2, station5, station3, station6, station4) + "," + station7;
                    }
                }

                //세번째 경유역이 station6
                else {

                    //네번재 경유역이 station3
                    if (middle_number_2(station6, station3, station4) == station3) {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station2, station5, station6, station3, station4) + "," + station7;
                    }

                    //네번재 경유역이 station4
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station2, station5, station6, station4, station3) + "," + station7;
                    }
                }
            }

            //두번째 경유역이 station6
            else  {

                //세번째 경유역이 station3
                if (middle_number_3(station6, station3, station4, station5) == station3) {

                    //네번재 경유역이 station4
                    if (middle_number_2(station3, station4, station5) == station4) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station2, station6, station3, station4, station5) + "," + station7;
                    }

                    //네번재 경유역이 station5
                    else {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station2, station6, station3, station5, station4) + "," + station7;
                    }
                }

                //세번째 경유역이 station4
                else if (middle_number_3(station6, station3, station4, station5) == station4) {

                    //네번재 경유역이 station3
                    if (middle_number_2(station4, station3, station5) == station3) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station2, station6, station4, station3, station5) + "," + station7;
                    }

                    //네번재 경유역이 station5
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station2, station6, station4, station5, station3) + "," + station7;
                    }
                }

                //세번째 경유역이 station5
                else {

                    //네번재 경유역이 station3
                    if (middle_number_2(station5, station3, station4) == station3) {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station2, station6, station5, station3, station4) + "," + station7;
                    }

                    //네번재 경유역이 station4
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station2, station6, station5, station4, station3) + "," + station7;
                    }
                }
            }

        }

        //첫번째 경유역이 station3
        else if (middle_number_5(station1, station2, station3, station4, station5, station6) == station3) {

            //두번째 경유역이 station2
            if (middle_number_4(station3, station2, station4, station5, station6) == station2) {

                //세번째 경유역이 station4
                if (middle_number_3(station2, station4, station5, station6) == station4) {

                    //네번재 경유역이 station5
                    if (middle_number_2(station4, station5, station6) == station5) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station3, station2, station4, station5, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station3, station2, station4, station6, station5) + "," + station7;
                    }
                }

                //세번째 경유역이 station5
                else if (middle_number_3(station2, station4, station5, station6) == station5) {

                    //네번재 경유역이 station4
                    if (middle_number_2(station5, station4, station6) == station4) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station3, station2, station5, station4, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station3, station2, station5, station6, station4) + "," + station7;
                    }
                }

                //세번째 경유역이 station6
                else {

                    //네번재 경유역이 station4
                    if (middle_number_2(station6, station4, station5) == station4) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station3, station2, station6, station4, station5) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station3, station2, station6, station5, station4) + "," + station7;
                    }
                }
            }

            //두번째 경유역이 station4
            else if (middle_number_4(station3, station2, station4, station5, station6) == station4) {

                //세번째 경유역이 station2
                if (middle_number_3(station4, station2, station5, station6) == station2) {

                    //네번재 경유역이 station5
                    if (middle_number_2(station2, station5, station6) == station5) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station3, station4, station2, station5, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station3, station4, station2, station6, station5) + "," + station7;
                    }
                }

                //세번째 경유역이 station5
                else if (middle_number_3(station4, station2, station5, station6) == station5) {

                    //네번재 경유역이 station2
                    if (middle_number_2(station5, station2, station6) == station2) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station3, station4, station5, station2, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station3, station4, station5, station6, station2) + "," + station7;
                    }
                }

                //세번째 경유역이 station6
                else {

                    //네번재 경유역이 station2
                    if (middle_number_2(station6, station2, station5) == station2) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station3, station4, station6, station2, station5)+ "," + station7;
                    }

                    //네번재 경유역이 station5
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station3, station4, station6, station5, station2) + "," + station7;
                    }
                }
            }

            //두번째 경유역이 station5
            else if (middle_number_4(station3, station2, station4, station5, station6) == station5) {

                //세번째 경유역이 station2
                if (middle_number_3(station5, station2, station4, station6) == station2) {

                    //네번재 경유역이 station4
                    if (middle_number_2(station2, station4, station6) == station4) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station3, station5, station2, station4, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station3, station5, station2, station6, station4) + "," + station7;
                    }
                }

                //세번째 경유역이 station4
                else if (middle_number_3(station5, station2, station4, station6) == station4) {

                    //네번재 경유역이 station2
                    if (middle_number_2(station4, station2, station6) == station2) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station3, station5, station4, station2, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station3, station5, station4, station6, station2) + "," + station7;
                    }
                }

                //세번째 경유역이 station6
                else {

                    //네번재 경유역이 station2
                    if (middle_number_2(station6, station2, station4) == station2) {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station3, station5, station6, station2, station4) + "," + station7;
                    }

                    //네번재 경유역이 station4
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station3, station5, station6, station4, station2)+ "," + station7;
                    }
                }
            }

            //두번째 경유역이 station6
            else {

                //세번째 경유역이 station2
                if (middle_number_3(station6, station2, station4, station5) == station2) {

                    //네번재 경유역이 station4
                    if (middle_number_2(station2, station4, station5) == station4) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station3, station6, station2, station4, station5) + "," + station7;
                    }

                    //네번재 경유역이 station5
                    else {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station3, station6, station2, station5, station4) + "," + station7;
                    }
                }

                //세번째 경유역이 station4
                else if (middle_number_3(station6, station2, station4, station5) == station4) {

                    //네번재 경유역이 station2
                    if (middle_number_2(station4, station2, station5) == station2) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station3, station6, station4, station2, station5) + "," + station7;
                    }

                    //네번재 경유역이 station5
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station3, station6, station4, station5, station2) + "," + station7;
                    }
                }

                //세번째 경유역이 station5
                else {

                    //네번재 경유역이 station2
                    if (middle_number_2(station5, station2, station4) == station2) {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station3, station6, station5, station2, station4) + "," + station7;
                    }

                    //네번재 경유역이 station4
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station3, station6, station5, station4, station2) + "," + station7;
                    }
                }
            }

        }

        //첫번째 경유역이 station4
        else if (middle_number_5(station1, station2, station3, station4, station5, station6) == station4) {

            //두번째 경유역이 station2
            if (middle_number_4(station4, station2, station3, station5, station6) == station2) {

                //세번째 경유역이 station3
                if (middle_number_3(station2, station3, station5, station6) == station3) {

                    //네번재 경유역이 station5
                    if (middle_number_2(station3, station5, station6) == station5) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station4, station2, station3, station5, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station4, station2, station3, station6, station5) + "," + station7;
                    }
                }

                //세번째 경유역이 station5
                else if (middle_number_3(station2, station3, station5, station6) == station5) {

                    //네번재 경유역이 station3
                    if (middle_number_2(station5, station3, station6) == station3) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station4, station2, station5, station3, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station4, station2, station5, station6, station3) + "," + station7;
                    }
                }

                //세번째 경유역이 station6
                else {

                    //네번재 경유역이 station3
                    if (middle_number_2(station6, station3, station5) == station3) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station4, station2, station6, station3, station5)+ "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station4, station2, station6, station5, station3) + "," + station7;
                    }
                }
            }

            //두번째 경유역이 station3
            else if (middle_number_4(station4, station2, station3, station5, station6) == station3) {

                //세번째 경유역이 station2
                if (middle_number_3(station3, station2, station5, station6) == station2) {

                    //네번재 경유역이 station5
                    if (middle_number_2(station2, station5, station6) == station5) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station4, station3, station2, station5, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station4, station3, station2, station6, station5)+ "," + station7;
                    }
                }

                //세번째 경유역이 station5
                else if (middle_number_3(station3, station2, station5, station6) == station5) {

                    //네번재 경유역이 station2
                    if (middle_number_2(station5, station2, station6) == station2) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station4, station3, station5, station2, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station4, station3, station5, station6, station2)+ "," + station7;
                    }
                }

                //세번째 경유역이 station6
                else {

                    //네번재 경유역이 station2
                    if (middle_number_2(station6, station2, station5) == station2) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station4, station3, station6, station2, station5) + "," + station7;
                    }

                    //네번재 경유역이 station5
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station4, station3, station6, station5, station2) + "," + station7;
                    }
                }
            }

            //두번째 경유역이 station5
            else if (middle_number_4(station4, station2, station3, station5, station6) == station5) {

                //세번째 경유역이 station2
                if (middle_number_3(station5, station2, station3, station6) == station2) {

                    //네번재 경유역이 station3
                    if (middle_number_2(station2, station3, station6) == station3) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station4, station5, station2, station3, station6)+ "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station4, station5, station2, station6, station3) + "," + station7;
                    }
                }

                //세번째 경유역이 station3
                else if (middle_number_3(station5, station2, station3, station6) == station3) {

                    //네번재 경유역이 station2
                    if (middle_number_2(station3, station2, station6) == station2) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station4, station5, station3, station2, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station4, station5, station3, station6, station2) + "," + station7;
                    }
                }

                //세번째 경유역이 station6
                else {

                    //네번재 경유역이 station2
                    if (middle_number_2(station6, station2, station3) == station2) {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station4, station5, station6, station2, station3) + "," + station7;
                    }

                    //네번재 경유역이 station3
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station4, station5, station6, station3, station2) + "," + station7;
                    }
                }
            }

            //두번째 경유역이 station6
            else {

                //세번째 경유역이 station2
                if (middle_number_3(station6, station2, station3, station5) == station2) {

                    //네번재 경유역이 station3
                    if (middle_number_2(station2, station3, station5) == station4) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station4, station6, station2, station3, station5) + "," + station7;
                    }

                    //네번재 경유역이 station5
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station3, station6, station2, station5, station3) + "," + station7;
                    }
                }

                //세번째 경유역이 station3
                else if (middle_number_3(station6, station2, station3, station5) == station3) {

                    //네번재 경유역이 station2
                    if (middle_number_2(station3, station2, station5) == station2) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station4, station6, station3, station2, station5)+ "," + station7;
                    }

                    //네번재 경유역이 station5
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station4, station6, station3, station5, station2) + "," + station7;
                    }
                }

                //세번째 경유역이 station5
                else {

                    //네번재 경유역이 station2
                    if (middle_number_2(station5, station2, station3) == station2) {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station4, station6, station5, station2, station3) + "," + station7;
                    }

                    //네번재 경유역이 station3
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station4, station6, station5, station3, station2) + "," + station7;
                    }
                }
            }

        }

        //첫번째 경유역이 station5
        else if (middle_number_5(station1, station2, station3, station4, station5, station6) == station5) {

            //두번째 경유역이 station2
            if (middle_number_4(station5, station2, station3, station4, station6) == station2) {

                //세번째 경유역이 station3
                if (middle_number_3(station2, station3, station4, station6) == station3) {

                    //네번재 경유역이 station4
                    if (middle_number_2(station3, station4, station6) == station4) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station5, station2, station3, station4, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station5, station2, station3, station6, station4) + "," + station7;
                    }
                }

                //세번째 경유역이 station4
                else if (middle_number_3(station2, station3, station4, station6) == station4) {

                    //네번재 경유역이 station3
                    if (middle_number_2(station4, station3, station6) == station3) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station5, station2, station4, station3, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station5, station2, station4, station6, station3) + "," + station7;
                    }
                }

                //세번째 경유역이 station6
                else {

                    //네번재 경유역이 station3
                    if (middle_number_2(station6, station3, station4) == station3) {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station5, station2, station6, station3, station4) + "," + station7;
                    }

                    //네번재 경유역이 station4
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station5, station2, station6, station4, station3) + "," + station7;
                    }
                }
            }

            //두번째 경유역이 station3
            else if (middle_number_4(station5, station2, station3, station4, station6) == station3) {

                //세번째 경유역이 station2
                if (middle_number_3(station3, station2, station4, station6) == station2) {

                    //네번재 경유역이 station4
                    if (middle_number_2(station2, station4, station6) == station4) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station5, station3, station2, station4, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station5, station3, station2, station6, station4) + "," + station7;
                    }
                }

                //세번째 경유역이 station4
                else if (middle_number_3(station3, station2, station4, station6) == station4) {

                    //네번재 경유역이 station2
                    if (middle_number_2(station4, station2, station6) == station2) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station5, station3, station4, station2, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station5, station3, station4, station6, station2)+ "," + station7;
                    }
                }

                //세번째 경유역이 station6
                else {

                    //네번재 경유역이 station2
                    if (middle_number_2(station6, station2, station4) == station2) {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station5, station3, station6, station2, station4) + "," + station7;
                    }

                    //네번재 경유역이 station4
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station5, station3, station6, station4, station2)+ "," + station7;
                    }
                }
            }

            //두번째 경유역이 station4
            else if (middle_number_4(station5, station2, station3, station4, station6) == station4) {

                //세번째 경유역이 station2
                if (middle_number_3(station4, station2, station3, station6) == station2) {

                    //네번재 경유역이 station3
                    if (middle_number_2(station2, station3, station6) == station3) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station5, station4, station2, station3, station6) + "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station4, station5, station2, station6, station3)+ "," + station7;
                    }
                }

                //세번째 경유역이 station3
                else if (middle_number_3(station4, station2, station3, station6) == station3) {

                    //네번재 경유역이 station2
                    if (middle_number_2(station3, station2, station6) == station2) {
                        String search = controller.search(station6, station7);  //경유5->도착
                        return compare_4(station1, station5, station4, station3, station2, station6)+ "," + station7;
                    }

                    //네번재 경유역이 station6
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station5, station4, station3, station6, station2)+ "," + station7;
                    }
                }

                //세번째 경유역이 station6
                else {

                    //네번재 경유역이 station2
                    if (middle_number_2(station6, station2, station3) == station2) {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station5, station4, station6, station2, station3) + "," + station7;
                    }

                    //네번재 경유역이 station3
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station5, station4, station6, station3, station2)+ "," + station7;
                    }
                }
            }

            //두번째 경유역이 station6
            else {

                //세번째 경유역이 station2
                if (middle_number_3(station6, station2, station3, station4) == station2) {

                    //네번재 경유역이 station3
                    if (middle_number_2(station2, station3, station4) == station3) {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station5, station6, station2, station3, station3)+ "," + station7;
                    }

                    //네번재 경유역이 station4
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station5, station6, station2, station4, station3) + "," + station7;
                    }
                }

                //세번째 경유역이 station3
                else if (middle_number_3(station6, station2, station3, station4) == station3) {

                    //네번재 경유역이 station2
                    if (middle_number_2(station3, station2, station3) == station2) {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station5, station6, station3, station2, station3) + "," + station7;
                    }

                    //네번재 경유역이 station4
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station5, station6, station3, station4, station2) + "," + station7;
                    }
                }

                //세번째 경유역이 station4
                else {

                    //네번재 경유역이 station2
                    if (middle_number_2(station4, station2, station3) == station2) {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station5, station6, station4, station2, station3)+ "," + station7;
                    }

                    //네번재 경유역이 station3
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station5, station6, station4, station3, station2)+ "," + station7;
                    }
                }
            }

        }

        //첫번째 경유역이 station6
        else {

            //두번째 경유역이 station2
            if (middle_number_4(station6, station2, station3, station4, station5) == station2) {

                //세번째 경유역이 station3
                if (middle_number_3(station2, station3, station4, station5) == station3) {

                    //네번재 경유역이 station4
                    if (middle_number_2(station3, station4, station5) == station4) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station6, station2, station3, station4, station5)+ "," + station7;
                    }

                    //네번재 경유역이 station5
                    else {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station6, station2, station3, station5, station4)+ "," + station7;
                    }
                }

                //세번째 경유역이 station4
                else if (middle_number_3(station2, station3, station4, station5) == station4) {

                    //네번재 경유역이 station3
                    if (middle_number_2(station4, station3, station5) == station3) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station6, station2, station4, station3, station5)+ "," + station7;
                    }

                    //네번재 경유역이 station5
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station6, station2, station4, station5, station3) + "," + station7;
                    }
                }

                //세번째 경유역이 station5
                else {

                    //네번재 경유역이 station3
                    if (middle_number_2(station5, station3, station4) == station3) {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station6, station2, station5, station3, station4) + "," + station7;
                    }

                    //네번재 경유역이 station4
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station6, station2, station5, station4, station3)+ "," + station7;
                    }
                }
            }

            //두번째 경유역이 station3
            else if (middle_number_4(station6, station2, station3, station4, station5) == station3) {

                //세번째 경유역이 station2
                if (middle_number_3(station3, station2, station4, station5) == station2) {

                    //네번재 경유역이 station4
                    if (middle_number_2(station2, station4, station5) == station4) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station6, station3, station2, station4, station5)+ "," + station7;
                    }

                    //네번재 경유역이 station5
                    else {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station6, station3, station2, station5, station4) + "," + station7;
                    }
                }

                //세번째 경유역이 station4
                else if (middle_number_3(station3, station2, station4, station5) == station4) {

                    //네번재 경유역이 station2
                    if (middle_number_2(station4, station2, station5) == station2) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station6, station3, station4, station2, station5) + "," + station7;
                    }

                    //네번재 경유역이 station5
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station6, station3, station4, station5, station2) + "," + station7;
                    }
                }

                //세번째 경유역이 station5
                else {

                    //네번재 경유역이 station2
                    if (middle_number_2(station5, station2, station4) == station2) {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station6, station3, station5, station2, station4) + "," + station7;
                    }

                    //네번재 경유역이 station4
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station6, station3, station5, station4, station2) + "," + station7;
                    }
                }
            }

            //두번째 경유역이 station4
            else if (middle_number_4(station6, station2, station3, station4, station5) == station4) {

                //세번째 경유역이 station2
                if (middle_number_3(station4, station2, station3, station5) == station2) {

                    //네번재 경유역이 station3
                    if (middle_number_2(station2, station3, station5) == station3) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station6, station4, station2, station3, station5) + "," + station7;
                    }

                    //네번재 경유역이 station5
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station6, station5, station2, station5, station3)+ "," + station7;
                    }
                }

                //세번째 경유역이 station3
                else if (middle_number_3(station4, station2, station3, station5) == station3) {

                    //네번재 경유역이 station2
                    if (middle_number_2(station3, station2, station5) == station2) {
                        String search = controller.search(station5, station7);  //경유5->도착
                        return compare_4(station1, station6, station4, station3, station2, station5)+ "," + station7;
                    }

                    //네번재 경유역이 station5
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station6, station4, station3, station5, station2) + "," + station7;
                    }
                }

                //세번째 경유역이 station5
                else {

                    //네번재 경유역이 station2
                    if (middle_number_2(station5, station2, station3) == station2) {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station6, station4, station5, station2, station3)+ "," + station7;
                    }

                    //네번재 경유역이 station3
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station6, station4, station5, station3, station2) + "," + station7;
                    }
                }
            }

            //두번째 경유역이 station5
            else  {

                //세번째 경유역이 station2
                if (middle_number_3(station5, station2, station3, station4) == station2) {

                    //네번재 경유역이 station3
                    if (middle_number_2(station2, station3, station4) == station3) {
                        String search = controller.search(station4, station7);  //경유5->도착
                        return compare_4(station1, station6, station5, station2, station3, station3)+ "," + station7;
                    }

                    //네번재 경유역이 station4
                    else {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station6, station5, station2, station4, station3)+ "," + station7;
                    }
                }

                //세번째 경유역이 station3
                else if (middle_number_3(station5, station2, station3, station4) == station3) {

                    //네번재 경유역이 station2
                    if (middle_number_2(station3, station2, station3) == station2) {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station6, station5, station3, station2, station3) + "," + station7;
                    }

                    //네번재 경유역이 station4
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station6, station5, station3, station4, station2) + "," + station7;
                    }
                }

                //세번째 경유역이 station4
                else {

                    //네번재 경유역이 station2
                    if (middle_number_2(station4, station2, station3) == station2) {
                        String search = controller.search(station3, station7);  //경유5->도착
                        return compare_4(station1, station6, station5, station4, station2, station3) + "," + station7;
                    }

                    //네번재 경유역이 station3
                    else {
                        String search = controller.search(station2, station7);  //경유5->도착
                        return compare_4(station1, station6, station5, station4, station3, station2)+ "," + station7;
                    }
                }
            }

        }

    }


    //경유 6개 비교
//    public String compare_6(String station1, String station2, String station3, String station4, String station5, String station6, String station7, String station8) {}
}

