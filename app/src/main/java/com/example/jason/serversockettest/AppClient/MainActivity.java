package com.example.jason.serversockettest.AppClient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jason.serversockettest.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity  {

    public static final String TAG = "MainActivity";
    EditText etIP,etContent;
    TextView tv;

    Button m_btnLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etIP = (EditText) findViewById(R.id.etIP);
        etContent = (EditText) findViewById(R.id.etContent);
        tv = (TextView) findViewById(R.id.textView);

        m_btnLink = (Button) findViewById(R.id.btnLink);

        m_btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });


//        etIP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                Log.d(TAG,"onFocusChanged. Focus?  " +  hasFocus);
//            }
//        });

        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

    }



    //---------------------------------

    Socket socket = null;
    PrintWriter writer = null;
    BufferedReader reader = null;


    public void connect(){

        final String str = etIP.getText().toString();

        AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
            String line = null;
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    socket = new Socket(str, 20000);
                    writer = new PrintWriter(socket.getOutputStream(), true);
//                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    publishProgress("@Success");
                    writer.println("@Success");
                    while((line = reader.readLine()) != null)
                        publishProgress(line);
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if(values[0].equals("@Success")) {
                    Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    m_btnLink.setClickable(false);
                }
                tv.append(values[0]);
                tv.append("\n");

                super.onProgressUpdate(values);
            }
        };
        read.execute();
    }

    public void send(){
//        writer.write(etContent.getText().toString());
//        writer.flush();
//        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        String out = etContent.getText().toString();
        writer.println(out);
        etContent.setText("");
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        Log.d(TAG,"onTouch in MainActivity");
//        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(etContent.getWindowToken(),0);
//        imm.hideSoftInputFromWindow(etIP.getWindowToken(), 0);
//        return true;
//    }
}
