package activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.Toast;

import com.aueb.towardsgreen.Connection;
import com.aueb.towardsgreen.Event;
import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.UserDao;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        setContentView(R.layout.activity_main);


        //ConnectionAsyncTask myTask = new ConnectionAsyncTask();
        //myTask.execute();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.posts_page:
                        Toast.makeText(MainActivity.this, "Post page", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.events_page:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_content, new EventPageFragment()).commit();
//                        Intent intent = new Intent(getApplicationContext(), EventActivity.class);
//                        startActivity(intent);
                        break;
                    case R.id.profile_page:
                        break;
                    case R.id.leaderboard_page:
                        break;
                    case R.id.sign_out:
                        try {
                            UserDao.getInstance(MainActivity.this).deleteUser();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.sidebar_menu_open, R.string.sidebar_menu_close);


        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.posts_page);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ConnectionAsyncTask extends AsyncTask<String, String, Integer> {

        ProgressDialog pd = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("Please wait...");
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
        }
    }
}