package com.giant.knowledgebase.mvp.ui.main.activity;

/**
 * Created by Jorble on 2017/7/6.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.L;

import org.simple.eventbus.EventBus;

import static com.giant.knowledgebase.app.constant.EventBusTags.HIDE_ERROR;
import static com.giant.knowledgebase.app.constant.EventBusTags.NETWORK_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHARE_WX_ACTION;
import static com.giant.knowledgebase.app.constant.EventBusTags.SHOW_ERROR;

public class NetworkConnectChangedReceiver extends BroadcastReceiver {
    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "4G网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            L.e("wifiState:" + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
            }
        }
        // 监听wifi的连接状态即是否连上了一个有效无线路由
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                // 获取联网状态的NetWorkInfo对象
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                //获取的State对象则代表着连接成功与否等状态
                NetworkInfo.State state = networkInfo.getState();
                //判断网络是否已经连接
                boolean isConnected = state == NetworkInfo.State.CONNECTED;
                L.e("isConnected:" + isConnected);
                showNetworkState(!isConnected);
            }
        }
        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent
                    .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI
                            || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        L.i(getConnectionType(info.getType()) + "连上");
                        showNetworkState(false);
                    }
                } else {
                    L.i(getConnectionType(info.getType()) + "断开");
                    showNetworkState(true);
                }
            }
        }
    }

    /**
     * 是否显示网络状态
     * @param isShow
     */
    private void showNetworkState(boolean isShow){
        if(isShow){
            Message message = new Message();
            message.what = SHOW_ERROR;
            EventBus.getDefault().post(message, NETWORK_ACTION);
        }else {
            Message message = new Message();
            message.what = HIDE_ERROR;
            EventBus.getDefault().post(message, NETWORK_ACTION);
        }
    }
}
