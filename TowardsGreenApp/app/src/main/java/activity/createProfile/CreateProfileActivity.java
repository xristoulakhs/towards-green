package activity.createProfile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aueb.towardsgreen.Connection;
import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.Request;
import com.aueb.towardsgreen.domain.Profile;
import com.google.gson.Gson;

import activity.SignInActivity;

public class CreateProfileActivity extends AppCompatActivity implements CreateProfileView{

    private Profile createdProfile;
    EditText firstName,lastName,email,passwordInput,passwordConf;
    Button btnSubmit, btnCancel;

    //private CreateProfilePresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        //presenter = new CreateProfilePresenter(this);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        passwordConf = findViewById(R.id.confirmPas);

        btnCancel = findViewById(R.id.cancel);
        btnSubmit = findViewById(R.id.submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createdProfile = new Profile();
                if(TextUtils.isEmpty(firstName.getText())){
                    firstName.setError("Παρακαλώ εισάγετε ενα έγκυρο όνομα!");
                }else if(TextUtils.isEmpty(lastName.getText())){
                    lastName.setError("Παρακαλώ εισάγετε ενα έγκυρο επώνυμο!");
                }else if(TextUtils.isEmpty(email.getText())){
                    lastName.setError("Παρακαλώ εισάγετε ενα έγκυρο email!");
                }else if(TextUtils.isEmpty(passwordInput.getText())){
                    lastName.setError("Παρακαλώ εισάγετε εναν έγκυρο κωδικό!");
                }else if(TextUtils.isEmpty(lastName.getText())){
                    lastName.setError("Παρακαλώ εισάγετε εναν έγκυρο κωδικό!");
                }else if(!passwordInput.getText().toString().equals(passwordConf.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Οι κωδικοί δεν ταιριάζουν",Toast.LENGTH_LONG).show();
                }else{
                createdProfile.setFirstName(firstName.getText().toString());
                createdProfile.setLastName(lastName.getText().toString());
                createdProfile.setEmail(email.getText().toString());
                createdProfile.setPassword(passwordInput.getText().toString());
                createdProfile.generateQR();
                sendNewProfile(createdProfile);

                //TODO: save to dao and continue to main screen

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean sendNewProfile(Profile profile) {
        Request request = new Request("INPR", new Gson().toJson(profile));
        Log.i("tag", new Gson().toJson(profile));
        return Connection.getInstance().requestSendData(request);
    }
}