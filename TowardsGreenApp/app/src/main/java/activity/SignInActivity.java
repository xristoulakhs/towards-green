package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.aueb.towardsgreen.Connection;
import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.Request;
import com.aueb.towardsgreen.User;
import com.aueb.towardsgreen.UserDao;
import com.google.gson.Gson;

import org.json.JSONException;

public class SignInActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private CheckBox rememberMe;
    private Button logInBtn;
    private Button createAccountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        setContentView(R.layout.activity_sign_in);

        ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask();
        connectionAsyncTask.execute();
        UserDao userDao = UserDao.getInstance(this);
        User rememberedUser = userDao.retrieveUser();

        if (!rememberedUser.getEmail().equals("null")) {

            if (authenticateUser(rememberedUser)) {
                intentMainActivity();
            }
        }

        email = findViewById(R.id.sign_in_email_edtxt);
        password = findViewById(R.id.sign_in_password_edtxt);
        rememberMe = findViewById(R.id.sign_in_remember_me_checkbox);
        logInBtn = findViewById(R.id.sign_in_log_in_btn);
        createAccountBtn = findViewById(R.id.sign_in_create_account_btn);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(email.getText().toString(),
                                     password.getText().toString());
                boolean result = authenticateUser(user);
                if (result) {

                    if (rememberMe.isChecked()) {
                        try {
                            userDao.saveUser(user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Toast.makeText(SignInActivity.this, "Σωστά", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            intentMainActivity();
                        }
                    }, 5000);
                }
                else {
                    Toast.makeText(SignInActivity.this, "Λαθος", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void intentMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean authenticateUser(User user) {
        Request request = new Request("USERCON", new Gson().toJson(user));
        return Connection.getInstance().requestSendData(request);
    }

    private void showAlertDialog(int layout) {
        AlertDialog alertDialog;
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(SignInActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        builderDialog.setView(layoutView);

        alertDialog = builderDialog.create();
        alertDialog.show();
    }

    private class ConnectionAsyncTask extends AsyncTask<String, String, Integer> {

        ProgressDialog pd = new ProgressDialog(SignInActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("Παρακαλώ περιμένετε για σύνδεση...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            Connection.getInstance().connect();
            return null;
        }

        @Override
        protected void onPostExecute(Integer i) {
            //Toast.makeText(getActivity(), event.getMeetingDate().toString(), Toast.LENGTH_SHORT).show();
            pd.hide();
            pd.dismiss();
        }
    }
}