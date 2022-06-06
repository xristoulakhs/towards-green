package activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aueb.towardsgreen.Connection;
import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.Request;
import com.aueb.towardsgreen.domain.Profile;
import com.google.gson.Gson;

import activity.SignInActivity;

public class CreateProfileActivity extends AppCompatActivity {

    private Profile createdProfile;
    EditText firstName,lastName,email,passwordInput,passwordConf;
    Button btnSubmit, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);


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
                
                CreateProfileAsyncTask createProfileAsyncTask = new CreateProfileAsyncTask();
                createProfileAsyncTask.execute();
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

    private class CreateProfileAsyncTask extends AsyncTask<String, String, Boolean> {
        ProgressDialog progressDialog = new ProgressDialog(CreateProfileActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Παρακαλώ περιμένετε...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Boolean doInBackground(String... strings) {
            Request request = new Request("INPR", new Gson().toJson(createdProfile));
            return Connection.getInstance().requestSendData(request);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            String successMessage = "Ο λογαριασμός δημιουργήθηκε με επιτυχία";
            String failureMessage = "Ο λογαριασμός δεν δημιουργήθηκε!";
            showAlertDialog(result, successMessage, failureMessage);
        }
    }

    private void showAlertDialog(boolean result, String successMessage, String failureMessage) {
        AlertDialog alertDialog;
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(CreateProfileActivity.this);
        View layoutView = null;

        if (result) {
            layoutView = getLayoutInflater().inflate(R.layout.success_dialog, null);
            TextView successMsg = layoutView.findViewById(R.id.success_dialog_txt);
            successMsg.setText(successMessage);
        }
        else {
            layoutView = getLayoutInflater().inflate(R.layout.failure_dialog, null);
            TextView failureMsg = layoutView.findViewById(R.id.failure_dialog_txt);
            failureMsg.setText(failureMessage);
        }

        builderDialog.setView(layoutView);

        alertDialog = builderDialog.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                finish();
            }
        }, 3000);
    }
}