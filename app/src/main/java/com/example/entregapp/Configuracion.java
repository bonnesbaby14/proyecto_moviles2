package com.example.entregapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.core.motion.utils.Utils;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.util.Calendar;

public class Configuracion extends AppCompatActivity {

    Button crear;
    Button set;
    String finalHour, finalMinute;
    Calendar today;
    public static String NOTIFICATION_CHANNEL_ID = "1001";
    public static String default_notification_id = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);


        crear=findViewById(R.id.btnSetAlarm);
        set=findViewById(R.id.btnDate);
        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);


        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notificationIntent = new Intent(Configuracion.this,MyReciver.class);
                notificationIntent.putExtra(MyReciver.NOTIFICATIONID,1);
                notificationIntent.putExtra(MyReciver.NOTIFICATION,getNotification("Hoora de entregar paquete"));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(Configuracion.this,0, notificationIntent,PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                assert alarmManager !=null;
                Log.d("gaboF", "se setio ");
                alarmManager.set(AlarmManager.RTC_WAKEUP, today.getTimeInMillis(), pendingIntent);

                Toast.makeText(Configuracion.this, getString(R.string.changed_to, finalHour + ":" + finalMinute), Toast.LENGTH_LONG).show();


            }
        });



        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Configuracion.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String finalHour, finalMinute;

                        finalHour = "" + selectedHour;
                        finalMinute = "" + selectedMinute;
                        if (selectedHour < 10) finalHour = "0" + selectedHour;
                        if (selectedMinute < 10) finalMinute = "0" + selectedMinute;

                        today = Calendar.getInstance();

                        today.set(Calendar.HOUR_OF_DAY, selectedHour);
                        today.set(Calendar.MINUTE, selectedMinute);
                        today.set(Calendar.SECOND, 0);






                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle(getString(R.string.select_time));
                mTimePicker.show();
            }
        });




    }

    private Notification getNotification(String content){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_id);

        builder.setContentTitle("Schedule Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);

        return builder.build();
    }
}