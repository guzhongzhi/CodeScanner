package com.gulusoft.codescaner;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gulusoft.com.gulusoft.com.codescanner.model.ServerManager;

import io.github.xudaojie.qrcodelib.CaptureActivity;

public class MainActivity extends Activity{
    final static ServerManager serverManager = new ServerManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView ip = (TextView)findViewById(R.id.ipTextView);
        ip.setText("WebSocket 服务端地址: ws://" + getLocalIpAddress() + ":1633");
        if(serverManager.IsRunning()) {
            serverManager.Stop();
        } else {
            if(!serverManager.Start(1633)) {
                ip.setText(serverManager.getLastErrorMessage());
            }
        }

        Button btnQRCode = (Button)findViewById(R.id.btnQRCode);
        btnQRCode.setText("开始扫描二维码");

        btnQRCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(i, 1);
            }
        });

        Button btn = (Button)findViewById(R.id.btnBarCode);
        btn.setText("开始扫描条形码");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ScanBarCodeActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK
                && data != null) {
            String result = data.getStringExtra("result");
            serverManager.SendMessageToAll(result);
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }

    private String getLocalIpAddress() {
        int paramInt = ((WifiManager) getSystemService("wifi")).getConnectionInfo().getIpAddress();
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }

    public static void SendMessage(String msg) {
        serverManager.SendMessageToAll(msg);
    }
}
