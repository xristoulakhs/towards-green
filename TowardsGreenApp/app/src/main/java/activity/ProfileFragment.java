package activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.aueb.towardsgreen.Connection;
import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.Request;
import com.aueb.towardsgreen.domain.Badge;
import com.aueb.towardsgreen.domain.Profile;
import com.google.gson.Gson;

public class ProfileFragment extends Fragment {
    private Profile profile;

    private TextView name;
    private ImageView qrImg;

    private TextView userId;
    private TextView role;
    private TextView points;
    private TextView badgeNum;

    private LinearLayout badgeLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profile = Connection.getInstance().getProfile();

        name = view.findViewById(R.id.profile_name);
        qrImg = view.findViewById(R.id.profile_qrCodeImage);
        userId = view.findViewById(R.id.profile_userId);
        role = view.findViewById(R.id.profile_userRole);
        points = view.findViewById(R.id.profile_points);
        badgeNum = view.findViewById(R.id.profile_number_of_badges);
        badgeLayout = view.findViewById(R.id.profile_badgeLayout);

        ProfileAsyncTask profileAsyncTask = new ProfileAsyncTask();
        profileAsyncTask.execute();

    }

    private void setFields() {
        name.setText(profile.getFullName());
        qrImg.setImageBitmap(profile.getImageBitmap());
        userId.setText(profile.getUserID());
        role.setText(profile.getRole().toString());
        points.setText(String.valueOf(profile.getPoints()));
        badgeNum.setText(String.valueOf(profile.getBadges().size()));

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        for (Badge badge : profile.getBadges()) {
            Bundle args = new Bundle();
            args.putSerializable("badge", badge);
            BadgeProfileFragment badgeProfileFragment = new BadgeProfileFragment();
            badgeProfileFragment.setArguments(args);
            transaction.add(badgeLayout.getId(), badgeProfileFragment);
        }
        transaction.commit();
    }

    private class ProfileAsyncTask extends AsyncTask<String, String, Boolean> {
        ProgressDialog pd = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("Παρακαλώ περιμένετε ανανέωση προφίλ...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Request request = new Request("GETPR", profile.getEmail());
            String json = Connection.getInstance().requestGetData(request).get(0);
            Profile profile = new Gson().fromJson(json, Profile.class);

            Connection.getInstance().setProfile(profile);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
            setFields();
        }
    }
}
