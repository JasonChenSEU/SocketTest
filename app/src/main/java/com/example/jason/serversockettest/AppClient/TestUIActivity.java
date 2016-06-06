package com.example.jason.serversockettest.AppClient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.jason.serversockettest.R;

public class TestUIActivity extends AppCompatActivity {

    private LinearLayout mLayoutChatWindow;
    private ScrollView mScrollview;

    //#1: 2个线程实现更新：View的post + scrollView的post,isMeasuredFinished用来标注第一个线程是否运行完成
    private boolean isMeasureFinished = false;

    //test:left/right
    private boolean mIsLeft = true;

    //#1,2用来表示view的测量高度
    private int mAddViewHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chatwindow_test);

        initViews();

        //初始化的同时移动到scroll的底端
        mScrollview.post(new Runnable() {
            @Override
            public void run() {
                mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = addSingleMsg();
                mLayoutChatWindow.addView(view);
                //addView之后等待绘制完成，移动到底端
                mScrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                //#1 scrollview的线程
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        while(!isMeasureFinished){
//                            scrollViewToBottom();
//                        }
//                        isMeasureFinished = false;
//                    }
//                }).start();

//                mAddViewHeight = view.getHeight();
//                scrollViewToBottom(measureViewHeight(mLayoutChatWindow));
            }
        });
//        mLayoutChatWindow.addView(addSingleMsg());


    }

    private int measureViewHeight(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec( (1<<30) - 1, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec( (1<<30) - 1, View.MeasureSpec.AT_MOST);
        view.measure(widthMeasureSpec,heightMeasureSpec);
        return view.getMeasuredHeight();
    }

    private void scrollViewToBottom(int height) {
//        int oriHeight = mScrollview.getMeasuredHeight();
        mScrollview.scrollTo(0,height);

    }

    private View addSingleMsg() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        lp.bottomMargin = 10;
        LayoutInflater inflater = LayoutInflater.from(this);
        mIsLeft = !mIsLeft;
        if(mIsLeft) {
            final View view = inflater.inflate(R.layout.layout_singlemsg_left, null);
            view.setLayoutParams(lp);
//            view.post(new Runnable() {
//                @Override
//                public void run() {
//                    mAddViewHeight = view.getHeight();
//                    isMeasureFinished = true;
//                }
//            });

            //#2:测量view的高度
//            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec( (1<<30) - 1, View.MeasureSpec.AT_MOST);
//            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec( (1<<30) - 1, View.MeasureSpec.AT_MOST);
//            view.measure(widthMeasureSpec,heightMeasureSpec);
//            mAddViewHeight = view.getMeasuredHeight();

            return view;
        }else{
            final View view = inflater.inflate(R.layout.layout_singlemsg_right, null);
            lp.gravity = Gravity.END;
            view.setLayoutParams(lp);
            TextView tv = (TextView) view.findViewById(R.id.tvRightMsg);
            tv.setText("This is My Text.................Loooooooooooooooooooooooooooooooooong Text!!!!!!!!!!!!!!");

            //用线程post，在绘制完成之后测量view的宽高
            //但是利用线程会延迟
//            view.post(new Runnable() {
//                @Override
//                public void run() {
//                    mAddViewHeight = view.getHeight();
//                    isMeasureFinished = true;
//                }
//            });

            //#2:测量view的高度
//            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec( (1<<30) - 1, View.MeasureSpec.AT_MOST);
//            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec( (1<<30) - 1, View.MeasureSpec.AT_MOST);
//            view.measure(widthMeasureSpec,heightMeasureSpec);
//            mAddViewHeight = view.getMeasuredHeight();
            return view;
        }
    }

    private void initViews() {
        mLayoutChatWindow = (LinearLayout) findViewById(R.id.layout_content);
        mScrollview = (ScrollView) findViewById(R.id.scrollview_chat);
    }
}
