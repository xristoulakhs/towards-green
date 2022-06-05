package activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aueb.towardsgreen.Connection;
import com.aueb.towardsgreen.Event;
import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.Request;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.gson.Gson;
import com.google.zxing.Result;

public class ScanQRCodeEventActivity extends AppCompatActivity {
    private Event event;

    private CodeScanner codeScanner;
    private CodeScannerView scannerView;

    private ImageView icon;
    private TextView scanResult;
    private TextView userName;
    private Button returnBtn;

    private final int CAMERA_PERM = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode_event);

        scannerView = findViewById(R.id.event_scan_qrView);
        icon = findViewById(R.id.event_scan_qr_ic);
        scanResult = findViewById(R.id.event_scan_qr_status_txt);
        userName = findViewById(R.id.event_scan_qr_user_txt);
        returnBtn = findViewById(R.id.event_scan_qr_return_btn);

        if (getIntent().getExtras() != null) {
            event = (Event) getIntent().getSerializableExtra("event");
        }

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEventAttendees();
                finish();
            }
        });

        askForCameraPermission();
    }

    private void codeScanner() {
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        String userID = result.getText();
                        userName.setText(userID);

                        if (event.getAttendees().containsKey(userID)) {
                            String userNameProfile = event.getAttendeesNames().get(userID);
                            showSuccessLayout(userNameProfile);
                            event.setAttendeePresent(userID);
                        }
                        else {
                            showFailureLayout();
                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKeepScanningLayout();
                codeScanner.startPreview();
            }
        });
    }

    private void showSuccessLayout(String name) {
        icon.setImageResource(R.drawable.ic_done);
        scanResult.setText("Επιτυχές σκανάρισμα!");
        userName.setText(name);
    }

    private void showKeepScanningLayout() {
        icon.setImageResource(R.drawable.ic_qr_scanner);
        scanResult.setText("Σκανάρισμα χρήστη...");
        userName.setText("");
    }

    private void showFailureLayout() {
        icon.setImageResource(R.drawable.ic_not_done);
        scanResult.setText("Ο χρήστης δεν συμμετέχει.");
        userName.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    private void askForCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM);
        }
        else {
            codeScanner();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Δόθηκε άδεια για χρήση κάμερας", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Δεν διαθέτετε άδεια για χρήση κάμερας", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void updateEventAttendees() {
        Gson gson = new Gson();
        String updatedEvent = gson.toJson(new Event(event.getAttendees()));
        String json = gson.toJson(new String[]{event.getEventID(), updatedEvent});
        Request request = new Request("UPEVWR", json);
        Connection.getInstance().requestSendDataWithoutResponse(request);
    }
}