package activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aueb.towardsgreen.R;

public class EventRequirementFragment extends Fragment {
    private TextView requirement;
    private ImageView imageFulfillment;

    // TODO: Rename and change types of parameters
    private String requirementName;
    private String requirementFulfillment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requirementName = getArguments().getString("requirementName");
            requirementFulfillment = getArguments().getString("requirementFulfillment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_requirement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requirement = (TextView) view.findViewById(R.id.requirement_txt);
        requirement.setText(requirementName);
        imageFulfillment = (ImageView) view.findViewById(R.id.requirement_fulfillment_iv);

        if (!Boolean.parseBoolean(requirementFulfillment)) {
            imageFulfillment.setImageResource(R.drawable.ic_not_done);
        }
    }
}