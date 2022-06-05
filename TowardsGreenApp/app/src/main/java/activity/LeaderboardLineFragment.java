package activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aueb.towardsgreen.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderboardLineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderboardLineFragment extends Fragment {

    private static final String PLACEMENT = "placement";
    private static final String NAME = "name";
    private static final String POINTS = "points";

    // TODO: Rename and change types of parameters
    private String placement;
    private String name;
    private String points;

    public LeaderboardLineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param3 Parameter 3.
     * @return A new instance of fragment LeaderboardLineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaderboardLineFragment newInstance(String param1, String param2, String param3) {
        LeaderboardLineFragment fragment = new LeaderboardLineFragment();
        Bundle args = new Bundle();
        args.putString(PLACEMENT, param1);
        args.putString(NAME, param2);
        args.putString(POINTS, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            placement = getArguments().getString(PLACEMENT);
            name = getArguments().getString(NAME);
            points = getArguments().getString(POINTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard_line, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView placementtv = view.findViewById(R.id.lbplacement);
        TextView nametv = view.findViewById(R.id.lbname);
        TextView pointstv = view.findViewById(R.id.lbpoints);

        placementtv.setText(placement);
        nametv.setText(name);
        pointstv.setText(points);
    }


}