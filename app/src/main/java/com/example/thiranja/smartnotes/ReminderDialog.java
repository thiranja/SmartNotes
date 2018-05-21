package com.example.thiranja.smartnotes;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ReminderDialog extends DialogFragment {

    private DatePicker date;
    private TimePicker time;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static ReminderDialog newInstanse(String title){
        ReminderDialog frag = new ReminderDialog();
        Bundle args = new Bundle();
        args.putString("id", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String id = getArguments().getString("id");
        DBHelper helper = new DBHelper(getActivity());
        final String noteName = helper.getName(id);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Here comes the code for adding Time date ,time picker and
        // Making the notification

        builder.setTitle("Set Reminder");

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.time_date_pickers,null);
        builder.setView(v);
        // Setting date picker and the time picker

        date = (DatePicker) v.findViewById(R.id.date_picker);
        time = (TimePicker) v.findViewById(R.id.time_picker);

        builder.setPositiveButton("Remind Me!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // code for reading time,date and setting the notification

                // This conditional prase of code is nneded for creating the channelId needed for devices
                // Which are above android 8
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    NotificationManager notificationManager =
                            (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationChannel channel = new NotificationChannel("default",
                            "Channel name",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("Channel description");
                    notificationManager.createNotificationChannel(channel);
                }

                Intent viewUpdate = new Intent("android.intent.action.UPDATE");
                viewUpdate.putExtra("id","Thiranja");
                Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
                //viewUpdate.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);

                PendingIntent intent = PendingIntent.getActivity(getActivity(), 0, viewUpdate, 0);

                // Making calander Object for get time as miliseconds
                Calendar calendar = Calendar.getInstance();
                int hour,minute;
                if (Build.VERSION.SDK_INT >= 23){
                    hour = time.getHour();
                    minute = time.getMinute();
                }else{
                    hour = time.getCurrentHour();
                    minute = time.getCurrentMinute();
                }
                calendar.set(date.getYear(), date.getMonth(), date.getDayOfMonth(), hour, minute);

                NotificationCompat.Builder notify = new NotificationCompat.Builder(getActivity(),"default")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("This Note Needs your Attention")
                        .setContentText(noteName)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(intent)
                        .setWhen(calendar.getTimeInMillis())
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setAutoCancel(true)
                        .setVibrate(new long[]{1000,1000,1000,1000,1000});

                Notification notification = notify.build();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
                notificationManager.notify(1,notify.build());
                int notificationId = 1;

                Intent notificationIntent = new Intent(getActivity(),AlarmReceiver.class);
                notificationIntent.putExtra("id",id);
                notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, notificationId);
                notificationIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
                //notificationIntent.setFlags(Integer.parseInt(Intent.ACTION_BOOT_COMPLETED));
                //notificationIntent.setAction(Intent.ACTION_BOOT_COMPLETED);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                long delay = calendar.getTimeInMillis() - SystemClock.elapsedRealtime();
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                StringBuilder sb = new StringBuilder();
                sb.append(delay);

                //Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "Reminder does not set", Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }
}
