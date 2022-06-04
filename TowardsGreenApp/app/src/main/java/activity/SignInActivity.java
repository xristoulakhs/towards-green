package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.aueb.towardsgreen.domain.Profile;
import com.google.gson.Gson;

import org.json.JSONException;

import activity.createProfile.CreateProfileActivity;

public class SignInActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private CheckBox rememberMe;
    private Button logInBtn;
    private Button createAccountBtn;
    private User rememberedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        setContentView(R.layout.activity_sign_in);

        showInputAddressDialog();
        UserDao userDao = UserDao.getInstance(this);
        rememberedUser = userDao.retrieveUser();

        email = findViewById(R.id.sign_in_email_edtxt);
        password = findViewById(R.id.sign_in_password_edtxt);
        rememberMe = findViewById(R.id.sign_in_remember_me_checkbox);
        logInBtn = findViewById(R.id.sign_in_log_in_btn);
        createAccountBtn = findViewById(R.id.sign_in_create_account_btn);

        if (!rememberedUser.getEmail().equals("null")) {
            email.setText(rememberedUser.getEmail());
            password.setText(rememberedUser.getPassword());
        }

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(email.getText().toString(),
                                     password.getText().toString());

                AuthenticationAsyncTask authenticationAsyncTask = new AuthenticationAsyncTask(user);
                authenticationAsyncTask.execute();
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, CreateProfileActivity.class);
                startActivity(intent);
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

    private  void showInputAddressDialog() {
        EditText editText = new EditText(this);
        editText.setText("10.0.2.2");

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask();
                        connectionAsyncTask.execute();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Πληκτρολογήστε παρακάτω το νέο όνομα:")
                .setView(editText)
                .setPositiveButton("Εντάξει", dialogClickListener)
                .setNegativeButton("Ακύρωση", dialogClickListener);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = editText.getText().toString();
                Connection.getInstance().setAddress(address);
                alertDialog.dismiss();
                ConnectionAsyncTask connectionAsyncTask = new ConnectionAsyncTask();
                connectionAsyncTask.execute();
            }
        });
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
            if (!rememberedUser.getEmail().equals("null")) {
                AuthenticationAsyncTask authenticationAsyncTask = new AuthenticationAsyncTask(rememberedUser);
                authenticationAsyncTask.execute();
            }
            //Toast.makeText(getActivity(), event.getMeetingDate().toString(), Toast.LENGTH_SHORT).show();
            pd.hide();
            pd.dismiss();
        }
    }

    private class AuthenticationAsyncTask extends AsyncTask<String, String, Boolean> {
        private User user;

        public AuthenticationAsyncTask(User user) {
            this.user = user;
        }

        ProgressDialog pd = new ProgressDialog(SignInActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("Παρακαλώ περιμένετε για αυθεντικοποίηση...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            return authenticateUser(user);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            //Toast.makeText(getActivity(), event.getMeetingDate().toString(), Toast.LENGTH_SHORT).show();

            if (result) {

                Toast.makeText(SignInActivity.this, "True", Toast.LENGTH_SHORT).show();
                if (rememberMe.isChecked()) {
                    try {
                        UserDao.getInstance(SignInActivity.this).saveUser(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Request request = new Request("GETPR", email.getText().toString());
                String json = Connection.getInstance().requestGetData(request).get(0);
                Profile profile = new Gson().fromJson(json, Profile.class);

                Connection.getInstance().setProfile(profile);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intentMainActivity();
                    }
                }, 5000);
            }
            else {
                Toast.makeText(SignInActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
            }
            pd.hide();
            pd.dismiss();
        }
    }
}