package com.example.testimsi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {
    //	private static final String TAG = MainActivity.class.getName();
    private static final String TAG = "MainActivityttt";
    private String mImsi1, mImsi2;

    private TextView tvSimInfo;
    private Button bnGetImsi;
    private List<String> list;

    private ProgressDialog progressDialog;
    private int num =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvSimInfo = (TextView) findViewById(R.id.tvSimInfo);
        bnGetImsi = (Button) findViewById(R.id.bnGetImsi);

        bnGetImsi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new Thread(new Runnable() {
                    public void run() {
                        Log.e(TAG, "TEST start.");
                        list = new ArrayList<>();
                        String cmd = "LIST";
                        boolean success = sendViewCmd(cmd, list);
                        if (!success) {
                            Log.e(TAG, "sendViewCmd failed. " + cmd);
                            return;
                        }
                        String tag = null;
                        //com.qf.administrator.wallpaper
                        //com.cmcc.cmvideo
                        //com.eshore.ezone
                        //com.andreader.prein
                        //com.creditwealth.client
                        for (String line : list) {
                            int find = line.indexOf("com.tencent.mm");
                            if (find > 0) {
                                tag = line.substring(0, find).trim();
                                break;
                            }
                        }
                        if (tag == null) {
                            Log.e(TAG, "tag not found");
                            return;
                        }
                        cmd = "DUMP " + tag;
                        list.clear();
                        success = sendViewCmd(cmd, list);
                        parse();

                        if (!success) {
                            Log.e(TAG, "sendViewCmd failed. " + cmd);
                            return;
                        }
                        Log.e(TAG, "TEST end.");

                    }
                }).start();

            }
        });

        showVersion("");

//		showProgrealog(false);


    }
    public void parse(){
        Map<String,String> map = new HashMap<>();
        for (String str : list) {
            String[] entries = str.trim().split(" ");
            String[] className = entries[0].trim().split("@");
            for (int i = 1; i < entries.length; i++) {
                String[] entry = entries[i].split("=");
                if(entry.length==2){
                    map.put(entry[0].trim(),entry[1].trim());
                }
            }
            Log.i(TAG, "parse: "+className[0].trim());
            switch(className[0].trim()){
                case "android.widget.TextView":
                case "android.support.v7.widget.AppCompatTextView":
                    if(map.get("getVisibility()").contains("GONE")){

                    }else {
                        Log.i(TAG, "parse:&&&"+className[0]
                                +"  "+map.get("measurement:mMeasuredWidth")+"   "+map.get("measurement:mMeasuredHeight")
                                +"  "+map.get("layout:getLocationOnScreen_x()")+"  "+map.get("layout:getLocationOnScreen_y()"));

                    }
                    break;
                case "com.tencent.mm.ui.base.CustomViewPager":
                    Log.i(TAG, "parse:??? "+map.get("measurement:mMeasuredWidth")+"  "+map.get("scrolling:mScrollX"));
                    break;
                case "com.tencent.mm.ui.base.NoMeasuredTextView":
                    ++num;
                    FileUtil.saveFile(map.toString(),"/sdcard/testimsi",num+"NoMeasuredTextView.txt",true);
                    FileUtil.readFile("/sdcard/testimsi/NoMeasuredTextView.txt");
                    Log.i(TAG, "parse:***"+className[0]+"  "+map.get("text:mText")+"   "+map.toString());
                    break;
                case "com.tencent.mm.ui.AddressView":
                    Log.i(TAG, "parse:^^^"+className[0]+"  "+map.get("accessibility:getContentDescription()")+"   "+map.toString());
                    break;
            }
        }
    }
    public TextBean textState(int x,int y){
        Map<String,String> map = new HashMap<>();
        for (String str : list) {
            String[] entries = str.trim().split(" ");
            String[] className = entries[0].trim().split("@");
            for (int i = 1; i < entries.length; i++) {
                String[] entry = entries[i].split("=");
                if(entry.length==2){
                    map.put(entry[0].trim(),entry[1].trim());
                }
            }
            Log.i(TAG, "parse: "+className[0].trim());
            switch(className[0].trim()){
                case "android.widget.TextView":
                case "android.support.v7.widget.AppCompatTextView":
                    if(map.get("getVisibility()").contains("GONE")){

                    }else {
                        Log.i(TAG, "parse:&&&"+className[0]
                                +"  "+map.get("measurement:mMeasuredWidth")+"   "+map.get("measurement:mMeasuredHeight")
                                +"  "+map.get("layout:getLocationOnScreen_x()")+"  "+map.get("layout:getLocationOnScreen_y()"));

                    }
                    if(getPointText(x,y,map)){
                        TextBean tb = new TextBean();
                        tb.setText(map.get("text:mText").trim().split(",")[1]);
                        tb.setmCurTextColor(map.get("text:mCurTextColor"));
                        return tb;
                    }
                    break;
                case "com.tencent.mm.ui.base.CustomViewPager":
                    Log.i(TAG, "parse:??? "+map.get("measurement:mMeasuredWidth")+"  "+map.get("scrolling:mScrollX"));
                    break;
                case "com.tencent.mm.ui.base.NoMeasuredTextView":
                    ++num;
                    FileUtil.saveFile(map.toString(),"/sdcard/testimsi",num+"NoMeasuredTextView.txt",true);
                    FileUtil.readFile("/sdcard/testimsi/NoMeasuredTextView.txt");
                    Log.i(TAG, "parse:***"+className[0]+"  "+map.get("text:mText")+"   "+map.toString());
                    break;
                case "com.tencent.mm.ui.AddressView":
                    Log.i(TAG, "parse:^^^"+className[0]+"  "+map.get("accessibility:getContentDescription()")+"   "+map.toString());
                    break;
            }
        }
       return null;
    }
    private boolean getPointText(int x,int y,Map<String,String> map){
        int width = Integer.parseInt(map.get("measurement:mMeasuredWidth").trim().split(",")[1]);
        int height = Integer.parseInt(map.get("measurement:mMeasuredHeight").trim().split(",")[1]);
        String sx = map.get("layout:getLocationOnScreen_x()");
        String sy = map.get("layout:getLocationOnScreen_y()");


        return false;
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            //super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(MainActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();

                    break;
            }
        }

    };

    public void showProgressDialog(final boolean cancelable) {
        if (isFinishing()) {
            return;
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("请稍后");
            progressDialog.setTitle("提示");
            progressDialog.setCancelable(cancelable);
            progressDialog.setCanceledOnTouchOutside(cancelable);
            progressDialog.show();
        }
    }


    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private boolean sendViewCmd(String cmd, List<String> list) {
        Log.e(TAG, "sendViewCmd: " + cmd);
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 4939), 40000);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
            out.write(cmd);
            out.newLine();
            out.flush();

            //receive response from viewserver
            //Output: 21d12790 com.example.testimsi/com.example.testimsi.MainActivity

            String line;
            while ((line = in.readLine()) != null) {
                if ("DONE.".equalsIgnoreCase(line)) { //$NON-NLS-1$
                    break;
                }
                Log.e(TAG, "Output: " + line);
                list.add(line);
            }

            out.close();
            in.close();
            socket.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "zzz Exceptioin: " + e);
        }
        return false;
    }

    private void showVersion(String serverOrClient) {
        // 在Activity中可以直接调用getPackageManager()，获取PackageManager实例。
        PackageManager packageManager = getPackageManager();
        // 在Activity中可以直接调用getPackageName()，获取安装包全名。
        String packageName = getPackageName();
        // flags提供了10种选项，及其组合，如果只是获取版本号，flags=0即可
        int flags = 0;
        PackageInfo packageInfo = null;
        try {
            // 通过packageInfo即可获取AndroidManifest.xml中的信息。
            packageInfo = packageManager.getPackageInfo(packageName, flags);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        if (packageInfo != null) {
            // 这里就拿到版本信息了。
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            setTitle(getString(R.string.app_name) + " [" + versionName + " - " + versionCode + "] " + serverOrClient);
        }
    }


}
