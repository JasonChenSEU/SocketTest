package com.example.jason.serversockettest.AppClient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

    private ScrollView mScrollView;
    private LinearLayout mLinearLayout;
    private RelativeLayout mRelativeLayoutMain;

    Button m_btnLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        m_btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });


        mRelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = mRelativeLayoutMain.getRootView().getHeight() - mRelativeLayoutMain.getHeight();

                //make sure tha etContent hasFocus and HeightDiff > 1280-1024(don't know why-->maybe the content height is working now)
                if(etContent.hasFocus() && heightDiff > 256){
                    Log.v(TAG, "RootView Height: " +  mRelativeLayoutMain.getRootView().getHeight());
                    Log.v(TAG, "View Height: " +  mRelativeLayoutMain.getHeight());
                    Log.v(TAG, "heightDiff = " + heightDiff);
                    mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }
        });

        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

    }

    /**
     * init views
     */
    private void initViews() {
        mRelativeLayoutMain = (RelativeLayout) findViewById(R.id.layout_main);

        etIP = (EditText) findViewById(R.id.etIP);

        mLinearLayout = (LinearLayout) findViewById(R.id.layout_content);

        mScrollView = (ScrollView) findViewById(R.id.scrollview_chat);

        etContent = (EditText) findViewById(R.id.etContent);

        m_btnLink = (Button) findViewById(R.id.btnLink);
    }


    //---------------------------------

    Socket socket = null;
    PrintWriter writer = null;
    BufferedReader reader = null;


    /**
     * Connect to server using AsyncTask and the input ip + default port(20000)
     */
    public void connect(){

        final String str = etIP.getText().toString();

        AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
            String line = null;
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    socket = new Socket(str, 20000);
                    writer = new PrintWriter(socket.getOutputStream(), true);
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

                updateView(values[0]);

                super.onProgressUpdate(values);
            }
        };
        read.execute();
    }

    /**
     * @param strContent
     * Add view to Chat window with strContent
     * Automatically scroll to bottom
     */
    private void updateView(String strContent) {
        View view = addSingleMsg(strContent);
        mLinearLayout.addView(view);
        //addView之后等待绘制完成，移动到底端
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    /**
     * @param strContent info
     * @return view to be added
     * Left/right is depended by prefix: I (
     *
     */
    private View addSingleMsg(String strContent) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        lp.bottomMargin = 10;
        LayoutInflater inflater = LayoutInflater.from(this);
        View view;
        if(strContent.startsWith("I (")){
            lp.gravity = Gravity.END;
            view = inflater.inflate(R.layout.layout_singlemsg_right,null);
            TextView tv = (TextView) view.findViewById(R.id.tvRightMsg);
            tv.setText(strContent);
        }else{
            view = inflater.inflate(R.layout.layout_singlemsg_left,null);
            TextView tv = (TextView) view.findViewById(R.id.tvLeftMsg);
            tv.setText(strContent);
        }
        view.setLayoutParams(lp);
        return view;
    }

    /**
     * send
     */
    public void send(){
        String out = etContent.getText().toString();
        writer.println(out);
        etContent.setText("");
    }

}
