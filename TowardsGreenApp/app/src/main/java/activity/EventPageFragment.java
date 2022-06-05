package activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.aueb.towardsgreen.Connection;
import com.aueb.towardsgreen.Event;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.Request;
import com.aueb.towardsgreen.domain.Profile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class EventPageFragment extends Fragment {
    private Profile profile;
    private Connection connection;

    private boolean refreshing = false;
    private int numberOfEventsFetched = 0;
    private boolean noMoreEvents = false;

    private LinearLayout eventsLayout;
    private ScrollView eventScrollView;
    private SwipeRefreshLayout eventSwipeRefreshLayout;

    private FloatingActionButton floatingCreateEventBtn;

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

        //events = new ArrayList<>();
        connection = Connection.getInstance();
        profile = connection.getProfile();


        eventsLayout = view.findViewById(R.id.eventsLayout);
        eventScrollView = view.findViewById(R.id.event_page_scrollView);
        eventSwipeRefreshLayout = view.findViewById(R.id.event_page_refreshLayout);

        floatingCreateEventBtn = view.findViewById(R.id.event_create_floating_btn);

        if (profile.getRole() == Profile.ROLE.USER) {
            floatingCreateEventBtn.setVisibility(View.GONE);
        }

        floatingCreateEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.container_content, new CreateEditEventFragment()).commit();
            }
        });


        eventSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshing = true;
                RefreshEventAsyncTask myTask = new RefreshEventAsyncTask();
                myTask.execute();
            }
        });

        eventScrollView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        eventScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View sView = eventScrollView.getChildAt(eventScrollView.getChildCount() - 1);
                int scrollBottom = sView.getBottom() - (eventScrollView.getHeight() + eventScrollView.getScrollY());

                if (scrollBottom == 0 && !noMoreEvents) {
                    EventAsyncTask myTask = new EventAsyncTask("Φόρτωση περισσοτέρων εκδηλώσεων. Παρακαλώ περιμένετε...");
                    myTask.execute();
                }
            }
        });
        EventAsyncTask myTask = new EventAsyncTask("Παρακαλώ περιμένετε...");
        myTask.execute();

    }

    private class EventAsyncTask extends AsyncTask<String, String, ArrayList<Event>> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        String message;

        public EventAsyncTask(String message) {
            this.message = message;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage(message);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected ArrayList<Event> doInBackground(String... strings) {
            return convertJsonToEvents(connection.requestGetData(new Request("GETMOREEV", String.valueOf(numberOfEventsFetched))));
        }

        @Override
        protected void onPostExecute(ArrayList<Event> requestedEvents) {
            super.onPostExecute(requestedEvents);
            //events.addAll(requestedEvents);
            numberOfEventsFetched += requestedEvents.size();
            if (requestedEvents.size() < 2) {
                noMoreEvents = true;
            }
            showEvents(requestedEvents);
            progressDialog.hide();
            progressDialog.dismiss();
        }
    }

    private class RefreshEventAsyncTask extends AsyncTask<String, String, ArrayList<Event>> {

        @Override
        protected ArrayList<Event> doInBackground(String... strings) {
            return convertJsonToEvents(connection.requestGetData(new Request("GETEV", String.valueOf(numberOfEventsFetched))));
        }

        @Override
        protected void onPostExecute(ArrayList<Event> requestedEvents) {
            super.onPostExecute(requestedEvents);
            showEvents(requestedEvents);
            refreshing = false;
            eventSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void showEvents(ArrayList<Event> events) {
        boolean flag = true;
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        EventFragment eventFragment;
        for (Event event : events) {
            Bundle args = new Bundle();
            args.putSerializable("event", event);
            eventFragment = new EventFragment();
            eventFragment.setArguments(args);
            if (flag && refreshing) {
                transaction.replace(eventsLayout.getId(), eventFragment);
                transaction.addToBackStack(null);
                flag = false;
            }
            else{
                transaction.add(eventsLayout.getId(), eventFragment);
            }
        }
        transaction.commit();
    }

    private ArrayList<Event> convertJsonToEvents(ArrayList<String> jsons) {
        Gson gson = new Gson();
        ArrayList<Event> events = new ArrayList<>();
        for (String json : jsons) {
            events.add(gson.fromJson(json, Event.class));
        }
        return events;
    }
}