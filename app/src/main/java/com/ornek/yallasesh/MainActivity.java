package com.ornek.yallasesh;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final int MIC_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://meet.jit.si"))
                    .setAudioOnly(true)
                    .setVideoMuted(true)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(options);
        } catch (Exception ignored) { }

        findViewById(R.id.joinBtn).setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, MIC_REQUEST);
                return;
            }
            join();
        });
    }

    private void join() {
        String room = ((EditText) findViewById(R.id.roomInput))
                .getText().toString().trim();
        if (room.isEmpty()) {
            Toast.makeText(this, "Oda adı gir", Toast.LENGTH_SHORT).show();
            return;
        }
        JitsiMeetActivity.launch(this,
                new JitsiMeetConferenceOptions.Builder().setRoom(room).build());
    }

    @Override
    public void onRequestPermissionsResult(int code, @NonNull String[] p, @NonNull int[] r) {
        super.onRequestPermissionsResult(code, p, r);
        if (code == MIC_REQUEST) {
            if (r.length > 0 && r[0] == PackageManager.PERMISSION_GRANTED) join();
            else Toast.makeText(this, "Mikrofon izni gerekli", Toast.LENGTH_SHORT).show();
        }
    }
}
