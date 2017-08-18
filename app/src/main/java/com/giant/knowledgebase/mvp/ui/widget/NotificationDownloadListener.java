package com.giant.knowledgebase.mvp.ui.widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.giant.knowledgebase.R;
import com.giant.knowledgebase.app.utils.L;
import com.giant.knowledgebase.mvp.ui.main.activity.MainActivity;

import ezy.boost.update.OnDownloadListener;

/**
 * Created by Jorble on 2017/6/27.
 */

public class NotificationDownloadListener implements OnDownloadListener {
    private Context mContext;
    private int mNotifyId;
    private NotificationCompat.Builder mBuilder;

    RemoteViews contentView;

    public NotificationDownloadListener(Context context, int notifyId) {
        mContext = context;
        mNotifyId = notifyId;
    }

    @Override
    public void onStart() {

        /***
         * 在这里我们用自定的view来显示Notification
         */
        contentView = new RemoteViews(mContext.getPackageName(), R.layout.item_notification_download);
        contentView.setTextViewText(R.id.notificationTitle, "正在下载");
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

        if (mBuilder == null) {
            String title = "下载中 - " + mContext.getString(mContext.getApplicationInfo().labelRes);
            mBuilder = new NotificationCompat.Builder(mContext);
            mBuilder.setOngoing(true)
                    .setAutoCancel(false)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(mContext.getApplicationInfo().icon)
                    .setTicker(title)
                    .setCustomContentView(contentView);
        }
        onProgress(0);
    }

    @Override
    public void onProgress(int progress) {
        if (mBuilder != null) {
            if (progress > 0) {
                mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
                mBuilder.setDefaults(0);
            }

            contentView.setTextViewText(R.id.notificationPercent, progress + "%");
            contentView.setProgressBar(R.id.notificationProgress, 100, progress, false);

            NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(mNotifyId, mBuilder.build());
        }
    }

    @Override
    public void onFinish() {
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(mNotifyId);
    }
}