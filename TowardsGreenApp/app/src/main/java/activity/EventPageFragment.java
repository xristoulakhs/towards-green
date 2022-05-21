package activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import com.aueb.towardsgreen.Event;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.Request;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;


public class EventPageFragment extends Fragment {
    private LinearLayout eventsLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getActivity(), "Event fragment", Toast.LENGTH_SHORT).show();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        eventsLayout = (LinearLayout) view.findViewById(R.id.eventsLayout);
        MyTask myTask = new MyTask();
        myTask.execute();

    }

    private class MyTask extends AsyncTask<String, String, Event> {
        Event event = null;
        ProgressDialog pd = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("Please wait...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Event doInBackground(String... strings) {

            try {
                Socket socket = new Socket(InetAddress.getByName("10.0.2.2"), 8080);
                ObjectInputStream objectIS = new ObjectInputStream(socket.getInputStream());
                String json = (String) objectIS.readObject();
                Gson gson = new Gson();
                event = gson.fromJson(json, Event.class);
                Log.i("System", json);
                Log.i("System", gson.toJson(event));

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            return event;
        }

        @Override
        protected void onPostExecute(Event e) {
            super.onPostExecute(e);
            EventFragment eventFragment = new EventFragment();
            Bundle args = new Bundle();
            args.putSerializable("event", event);
            eventFragment.setArguments(args);
            getParentFragmentManager().beginTransaction().add(eventsLayout.getId(), eventFragment).commit();
            Toast.makeText(getActivity(), event.getMeetingDate().toString(), Toast.LENGTH_SHORT).show();
            pd.hide();
        }
    }
}