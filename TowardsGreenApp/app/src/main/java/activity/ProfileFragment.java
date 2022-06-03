package activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aueb.towardsgreen.Connection;
import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.domain.Profile;

public class ProfileFragment extends Fragment {
    private ImageView profileImg;
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
        Profile profile = Connection.getInstance().getProfile();

        profileImg = view.findViewById(R.id.profile_image);
        qrImg = view.findViewById(R.id.profile_qrCodeImage);
        userId = view.findViewById(R.id.profile_userId);
        role = view.findViewById(R.id.profile_userRole);
        points = view.findViewById(R.id.profile_points);
        badgeNum = view.findViewById(R.id.profile_number_of_badges);
        badgeLayout = view.findViewById(R.id.profile_badgeLayout);

        qrImg.setImageBitmap(profile.getImageBitmap());
        userId.setText(profile.getUserID());
        role.setText(profile.getRole().toString());
        points.setText(String.valueOf(profile.getPoints()));
        badgeNum.setText(String.valueOf(profile.getBadges().size()));
    }
}
