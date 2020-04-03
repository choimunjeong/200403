package com.example.hansol.xml_200321_hs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Page4 extends AppCompatActivity {

    // 날짜 관련 변수들
    Calendar myCalendar = Calendar.getInstance();
    EditText editDate;
    SimpleDateFormat sdf;
    String time;

    // 이용권 관련 변수
    EditText editDays;

    // 출발역 관련 변수
    EditText editDepSt;

    // 도착역 관련 변수
    EditText editDesSt;

    // 경로 최적화 관련 변수
    Button buttonOk;

    //자동완성 변수
    AutoCompleteTextView searchStation_page4;

    // svg지도
    WebView mabView_page4;

    //txt 관련 변수
    int i = 0;
    String readStr = "";
    private List<String> list;  //데이터를 넣을 리스트 변수
    Handler handler =new Handler();
    String[] code_name = null;
    String[] code = new String[237];
    String[] name = new String[237];
    String next_text[] = new String[8];  //다음 페이지에 넘길 값 배열

    //textview 위젯 연결 변수
    TextView[] text = new TextView[6];
    int stringtext[] = {R.id.text1,R.id.text2,R.id.text3,R.id.text4,R.id.text5,R.id.text6};

    //프로그레스바
    ProgressDialog asyncDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4);
        asyncDialog = new ProgressDialog(Page4.this);
        //검색 리스트 구현 부분
        list = new ArrayList<String>();            //리스트를 생성
        settingList();                             //리스트에 검색될 단어를 추가한다


        // 날짜 선택 눌렀을 때 DatePicker보여주기
        editDate = (EditText) findViewById(R.id.entDate_page4);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Page4.this, myDatePicker,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // 이용권 눌렀을 때 다이얼로그 띄우기
        editDays = (EditText) findViewById(R.id.entDays_page4);
        editDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayShow();
            }
        });


        //다음 액티비티로 이동(경로최적화 버튼 누르면
        buttonOk = (Button)findViewById(R.id.buttonOK_page4);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorShow();    // 공백인지 확인
                Toast.makeText(getApplicationContext(), "눌러짐;;;", Toast.LENGTH_SHORT).show();
            }
        });


        // 출발역 눌렀을 때 다이얼로그 띄우기
        editDepSt = (EditText)findViewById(R.id.entStation_page4);
        editDepSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                depShow();
            }
        });


        // 도착역 눌렀을 때 다이얼로그 띄우기
        editDesSt = (EditText)findViewById(R.id.entDesStation_page4);
        editDesSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desShow();
            }
        });


        //textview 위젯 연결
        for(i=0; i<6; i++){
            text[i] = (TextView)findViewById(stringtext[i]);
        }


        // svg 지도
        mabView_page4 = (WebView)findViewById(R.id.webView_page4);

        //웹뷰 자바스크립트 사용가능하도록 선언
        mabView_page4.getSettings().setJavaScriptEnabled(true);
        mabView_page4.getSettings().setLoadWithOverviewMode(true);
        mabView_page4.getSettings().setDisplayZoomControls(false);  //웹뷰 돋보기 없앰

        //웹뷰 줌기능
        mabView_page4.getSettings().setBuiltInZoomControls(true);
        mabView_page4.getSettings().setSupportZoom(true);
        mabView_page4.setWebViewClient(new WebViewClient());

        //웹뷰를 로드함
        mabView_page4.loadUrl("file:///android_asset/index2.html");

        //자바스크립트에서 메시지를 받을 수 있게 함 + 글자 비교해서 이미지 나오도록
        mabView_page4.addJavascriptInterface(new Object(){
            @JavascriptInterface
            public void send(final String msg){
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        //1_용산  <-이렇게 전달되는 걸 '_' 단위로 쪼개서 isStart에 넣는다.
                        String[] isStart = msg.split("_");

                        //넣을 text가 비어있는지 검사 후 넣기
                        //또, 출발+도착과 같은 역이 있으면 안돼.  ->dialog_Show()
                        //또, text 자리가 꽉 차있으면 안내해줘야해.  ->no_Show()
                        for(i=0;i < 6; i++){
                            if(msg.equals(text[i].getText())){
                                dialog_Show();
                                break;
                            }
                            else if("".equals(text[i].getText()) ){

                                if(msg.contains("1")){
                                    editDepSt.setText(isStart[1]);
                                    break;
                                } else if(msg.contains("2")){
                                    text[i].setText(isStart[1]);
                                    break;
                                } else if (msg.contains("3")){
                                    editDesSt.setText(isStart[1]);
                                    break;
                                }
                            }
                            else if(i==5 && !msg.contains("3") && !msg.contains("1")){
                                no_Show();
                                break;
                            }
                            else if(msg.contains("3")){
                                editDesSt.setText(isStart[1]);
                                break;
                            }
                            else if(msg.contains("1")){
                                editDepSt.setText(isStart[1]);
                                break;
                            }
                        }
                    }});
            }
        }, "android");

        //자바스크립트에서 메시지를 받을 수 있게 함 + 글자 비교해서 텍스트뷰에서 삭제하도록
        mabView_page4.addJavascriptInterface(new Object(){
            @JavascriptInterface
            public void delete(final String msg){
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        //지우려는 역을 text에서 찾고, 배열을 한칸씩 앞으로 당겨준다.
                        for(i=0;i < 6; i++){

                            //찾은 역이 있는 text번호(i)를 찾으면
                                if(msg.contains(text[i].getText())){

                                //text 배열을 한칸씩 당긴다.
                                for(int j=i; j < text.length; j++){
                                    if(j != text.length-1){
                                        text[j].setText(text[j+1].getText());

                                        if(j < text.length && text[j+1].getText().equals("")){
                                            break;
                                        }
                                    }
                                    else{
                                        text[j].setText("");
                                    }
                                }
                                break;
                            }
                        }
                    }});
            }
        }, "android2");

        mabView_page4.setWebChromeClient(new WebChromeClient());


        //자동완성
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.searchStation_page4);    //객체 연결
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list));   //아답터에 연결


        //자동입력에서 항목을 터치했을 때, 키보드가 바로 내려감 + 웹뷰에서 해당역에 출경도 버튼 띄워짐-!!!!!!!!!!!!!!!!!!!!!!
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(autoCompleteTextView.getText().toString() != null) {
//                    Toast.makeText(getApplicationContext(), autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                    mabView_page4.loadUrl("javascript:setMessage('"+autoCompleteTextView.getText().toString()+"')");
                }

                //키보드 내림
                InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
            }
        });

    }

    //날짜 값을 받아온다.
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    //선택된 날짜를 edittext에 적용시킨다.
    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";
        sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText editDate = (EditText) findViewById(R.id.entDate_page4);
        time = sdf.format(myCalendar.getTime());

        editDate.setText(time);
    }

    // 이용권 눌렀을 때
    void dayShow(){
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("3일권");
        ListItems.add("5일권");
        ListItems.add("7일권");

        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이용권 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String selectedText = items[i].toString();
                // 선택된 일 수는 토스트로 띄움
                Toast.makeText(Page4.this, selectedText, Toast.LENGTH_SHORT).show();
                editDays.setText(selectedText);
            }
        });
        builder.show();
    }


    // 출발역 눌렀을 때
    void depShow(){
        //자동완성
        final AutoCompleteTextView editText = new AutoCompleteTextView(this);
        editText.setThreshold(1);
        editText.setDropDownHeight(500);
        editText.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list));   //아답터에 연결


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("출발역 검색");
        builder.setView(editText);
        builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editDepSt.setText(editText.getText());
                mabView_page4.loadUrl("javascript:setStart('"+editText.getText()+"')");
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }


    // 도착역 눌렀을 때
    void desShow(){
        //자동완성
        final AutoCompleteTextView editText = new AutoCompleteTextView(this);
        editText.setThreshold(1);
        editText.setDropDownHeight(500);
        editText.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list));   //아답터에 연결

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("도착역 검색");
        builder.setView(editText);
        builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editDesSt.setText(editText.getText());
                mabView_page4.loadUrl("javascript:setEnd('"+editText.getText()+"')");
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }


    void errorShow(){
        if (editDate.getText().toString().length()==0 ||
                editDays.getText().toString().length()==0||
                editDepSt.getText().toString().length()==0||
                editDesSt.getText().toString().length()==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("입력되지 않은 항목이 있습니다. 한번 더 확인 해 주세요.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        }
        else if(text[0].getText().toString().length() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("최소 1개 이상의 경유역을 선택해주세요.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        } else{

            //다음 화면으로 값을 넘겨주기 전에 code와 name을 받아야 한다.
            for(int i=0; i<237; i++){
                if((editDepSt.getText().toString()).equals(name[i])){

                    next_text[0] = code[i] + "," + name[i];
                    break;
                }
            }

            for(int i=0; i<6; i++){
                //아무것도 없을 때
                if(text[i].getText().toString().length() == 0){
                    next_text[i+1] = null;
                }
                else{
                    for(int j=0; j<237; j++){
                        if((text[i].getText().toString()).equals(name[j])){
                            next_text[i+1] = code[j] + "," + name[j];
                        }
                    }
                }
            }

            //도착역의 code,name 얻기
            for(int i=0; i<237; i++){
                if((editDesSt.getText().toString()).equals(name[i])){
                    next_text[7] = code[i] + "," + name[i];
                    break;
                }
            }


            Intent intent = new Intent(getApplicationContext(), Page4_1.class);
            intent.putExtra("text0", next_text[0]);
            intent.putExtra("text1", next_text[1]);
            intent.putExtra("text2", next_text[2]);
            intent.putExtra("text3", next_text[3]);
            intent.putExtra("text4", next_text[4]);
            intent.putExtra("text5", next_text[5]);
            intent.putExtra("text6", next_text[6]);
            intent.putExtra("text7", next_text[7]);
            startActivity(intent);
        }
    }


    //리스트에 검색될 단어를 추가한다. txt파일을 for문으로 쪼개서 넣었다.
    private void settingList(){
        AssetManager am = getResources().getAssets() ;
        InputStream is = null;
        try{
            is = am.open("station3.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String str = null;
            while(((str = reader.readLine()) != null)){ readStr += str +"\n"; }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] arr = readStr.split("\n");  //한 줄씩 자른다.

        //code,name으로 되어있는 line을 ','를 기준으로 다시 자른다.
        for(int i=0; i<arr.length; i++){
            code_name = arr[i].split(",");

            code[i] = code_name[0];
            name[i] = code_name[1];

            list.add(name[i]);

            Log.i("야야야여기다ㅏㅏㅏ", code[i]+"-"+name[i]);
        }
    }


    //경유 추가하는데 이미 추가가 되어있다면 다이얼로그 띄움
    void dialog_Show(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("이미 추가한 경유역입니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }


    //경유 추가하는데 자리가 없으면 없다고 다이얼로그 띄움
    void no_Show(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("추가할 수 있는 개수를 초과했습니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

}
