package activity;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.domain.Badge;

public class BadgeProfileFragment extends Fragment {
    private Badge badge;

    private TextView name;
    private Button moreBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            badge = (Badge) getArguments().getSerializable("badge");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_badge_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.profile_badge_name_txt);
        moreBtn = view.findViewById(R.id.profile_badge_more_btn);

        name.setText(badge.getTitle());

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builderDialog;
        AlertDialog alertDialog;
        builderDialog = new AlertDialog.Builder(getActivity());
        View layoutView = getLayoutInflater().inflate(R.layout.badge_dialog, null);

        TextView badgeName = layoutView.findViewById(R.id.badgeTitle);
        TextView badgeDate = layoutView.findViewById(R.id.badgeDate);
        TextView badgePoints = layoutView.findViewById(R.id.badgePoints);
        Button dialogButton = layoutView.findViewById(R.id.backBtn);

        badgeName.setText(badge.getTitle());
        badgeDate.setText(badge.getPublishedDate());
        badgePoints.setText(String.valueOf(badge.getPointsEarned()));
        builderDialog.setView(layoutView);
        alertDialog = builderDialog.create();
        alertDialog.show();

        //Click on "PISW" button
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Dismiss Dialog
                alertDialog.dismiss();
            }
        });
    }
}