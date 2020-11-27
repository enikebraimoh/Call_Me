package com.enike.callme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.media.Ringtone;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoCallSession extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener {

    private static final String API_KEY = "47004844";
    private static final String TOKEN = "T1==cGFydG5lcl9pZD00NzAwNDg0NCZzaWc9ZTk2N2U2YjgwMjQ4MjY5MTNkMGRmMGEzNzRiZmExOGE5ZDZjYmE0MTpzZXNzaW9uX2lkPTJfTVg0ME56QXdORGcwTkg1LU1UWXdOak0yTURZek5qZzNNSDVCVjFOelUwRTJka3RwTDFrMmVFUjBiVnBuUmxCWVpUaC1mZyZjcmVhdGVfdGltZT0xNjA2MzYwNjkyJm5vbmNlPTAuNjMyNzc3NDYzMDU3ODAzNCZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNjA4OTUyNjk0JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static final String SESSION_ID = "2_MX40NzAwNDg0NH5-MTYwNjM2MDYzNjg3MH5BV1NzU0E2dktpL1k2eER0bVpnRlBYZTh-fg";

    private static final int REQUEST_CODE = 1234;
    private static final String LOG_NAME = "naname";
    ImageView CLose_Session;
    FrameLayout Publisher_Layout, Subscriber_Layout;
    DatabaseReference UserRef;
    String CurrentUser;
    Session mSession;
    Publisher mPublisher;
    Subscriber mSubscriber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_session);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        CurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CLose_Session = findViewById(R.id.endvideosession);


        CLose_Session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRef.child(CurrentUser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("Ringing")){
                            UserRef.child(CurrentUser).child("Ringing").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mPublisher.destroy();
                                    mSubscriber.destroy();
                                    Intent intent = new Intent(VideoCallSession.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                       else if(snapshot.hasChild("Calling")){
                            UserRef.child(CurrentUser).child("Calling").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mPublisher.destroy();
                                    mSubscriber.destroy();
                                    Intent intent = new Intent(VideoCallSession.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                        else{
                            mPublisher.destroy();
                            mSubscriber.destroy();
                            Intent intent = new Intent(VideoCallSession.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        AfterPermissionsGranted();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,VideoCallSession.this);
    }


    @AfterPermissionGranted(REQUEST_CODE)
    private void AfterPermissionsGranted(){
        String [] params = {Manifest.permission.INTERNET,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};

        if(EasyPermissions.hasPermissions(VideoCallSession.this,params)){

            Publisher_Layout = findViewById(R.id.publisher_frame);
            Subscriber_Layout = findViewById(R.id.subscriber_frame);

            mSession = new com.opentok.android.Session.Builder(VideoCallSession.this,API_KEY,SESSION_ID).build();
            mSession.setSessionListener(VideoCallSession.this);
            mSession.connect(TOKEN);
        }else{
            EasyPermissions.requestPermissions(this,"please this app needs permission",REQUEST_CODE,params);
        }

    }

    @Override
    public void onConnected(Session session) {
        Log.i(LOG_NAME,"on Stream Connected");
        // publishing our stream to the session
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(VideoCallSession.this);

        Publisher_Layout.addView(mPublisher.getView());

        if(mPublisher.getView() instanceof GLSurfaceView){
            ((GLSurfaceView)  mPublisher.getView()).setZOrderOnTop(true);
        }
        mSession.publish(mPublisher);

    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_NAME,"on Disconnected");

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_NAME,"on Stream Received");


        if(mSubscriber == null){
            mSubscriber = new Subscriber.Builder(this,stream).build();
            mSession.subscribe(mSubscriber);
            Subscriber_Layout.addView(mSubscriber.getView());

        }


    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_NAME, "on Stream Dropped");
        if (mSubscriber != null) {
            mSubscriber = null;
            Subscriber_Layout.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.i(LOG_NAME,"on Error");

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        Log.i(LOG_NAME,"onPointerCaptureChanged");

    }



    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_NAME,"on Stream Created");

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_NAME,"on Stream Destroyed");

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.i(LOG_NAME,"on Stream error");
    }
}