package activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aueb.towardsgreen.Connection;
import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.Request;
import com.aueb.towardsgreen.domain.Profile;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LeaderboardPageFragment extends Fragment {


    private LinearLayout container;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        container = view.findViewById(R.id.lblayout);

        LeaderboardAsyncTask leaderboardAsyncTask = new LeaderboardAsyncTask();
        leaderboardAsyncTask.execute();

    }

    private class LeaderboardAsyncTask extends AsyncTask<String, String, ArrayList<Profile>> {
        ProgressDialog pd = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("Παρακαλώ περιμένετε...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected ArrayList<Profile> doInBackground(String... strings) {
            Request request = new Request("GETSORTPROF", "");
            ArrayList<String> jsons = Connection.getInstance().requestGetData(request);
            return convertJsonToProfiles(jsons);
        }

        @Override
        protected void onPostExecute(ArrayList<Profile> profiles) {
            showleaderboard(profiles);
            //Toast.makeText(getActivity(), event.getMeetingDate().toString(), Toast.LENGTH_SHORT).show();
            pd.hide();
            pd.dismiss();
        }
    }

    private ArrayList<Profile> convertJsonToProfiles(ArrayList<String> jsons) {
        Gson gson = new Gson();
        ArrayList<Profile> profiles = new ArrayList<>();
        for (String json : jsons) {
            profiles.add(gson.fromJson(json, Profile.class));
        }
        return profiles;
    }

    private void showleaderboard(ArrayList<Profile> profiles) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        LeaderboardLineFragment lblineFragment;
        int count = 1;
        for (Profile profile : profiles) {
            lblineFragment = LeaderboardLineFragment.newInstance(String.valueOf(count),
                    profile.getFullName(), String.valueOf(profile.getPoints()));
            transaction.add(container.getId(), lblineFragment);
            count++;
        }
        transaction.commit();
    }
}