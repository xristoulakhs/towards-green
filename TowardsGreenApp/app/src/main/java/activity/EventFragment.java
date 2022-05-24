package activity;

import android.graphics.Color;
import android.os.Bundle;
import com.aueb.towardsgreen.Event;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.aueb.towardsgreen.R;

import java.util.HashMap;
import java.util.Map;

public class EventFragment extends Fragment {
    // Requirement layout (LinearLayout) initialization
    private LinearLayout requirementLayout;

    // Event main fields (TextViews) initialization
    private TextView publishedTime;
    private TextView publishedDate;
    private TextView title;
    private TextView status;
    private TextView description;
    private TextView meetingDate;
    private TextView meetingTime;
    private TextView location;
    private TextView badge;

    // Event image(ImageView) initialization
    private ImageView eventMenu;

    // Event menu button (ImageView) initialization
    private ImageView eventImage;

    // Event number of Reactions (TextViews) initialization
    private TextView takePartReactionNumber;
    private TextView maybeReactionNumber;
    private TextView notInterestedReactionNumber;

    // Event Reactions buttons (TextViews-button use) initialization
    private TextView takePartReaction;
    private TextView maybeReaction;
    private TextView notInterestedReaction;

    private Event event;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = (Event) getArguments().getSerializable("event");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Requirement layout (LinearLayout) declaration
        requirementLayout = view.findViewById(R.id.event_requirementLayout);

        // Event main fields (TextViews) declaration
        TextView publisherUsername = view.findViewById(R.id.event_publisher_username_txt);
        publishedTime = view.findViewById(R.id.event_published_time_txt);
        publishedDate = view.findViewById(R.id.event_published_date_txt);
        title = view.findViewById(R.id.event_title_txt);
        status = view.findViewById(R.id.event_status_txt);
        description = view.findViewById(R.id.event_description_txt);
        meetingDate = view.findViewById(R.id.event_meeting_date_txt);
        meetingTime = view.findViewById(R.id.event_meeting_time_txt);
        location = view.findViewById(R.id.event_location_txt);
        badge = view.findViewById(R.id.event_badge_txt);

        // Event image (ImageView) declaration
        eventImage = view.findViewById(R.id.event_image);
        eventImage.setImageBitmap(event.getImageBitmap());

        // Event menu button (ImageView) declaration
        eventMenu = view.findViewById(R.id.ic_events_menu);

        // Event number of Reactions (TextViews) declaration
        takePartReactionNumber = view.findViewById(R.id.event_reaction_takePart_number);
        maybeReactionNumber = view.findViewById(R.id.event_reaction_maybe_number);
        notInterestedReactionNumber = view.findViewById(R.id.event_reaction_notInterested_number);

        // Event Reactions buttons (TextViews) declaration
        takePartReaction = view.findViewById(R.id.event_reaction_takePart);
        maybeReaction = view.findViewById(R.id.event_reaction_maybe);
        notInterestedReaction = view.findViewById(R.id.event_reaction_notInterested);

        // Setting Event main fields
        publisherUsername.setText(event.getCreator());
        publishedTime.setText(event.getPublishedTime().toString());
        publishedDate.setText(event.getPublishedDate().toString());
        title.setText(event.getTitle());
        status.setText(event.getStatusString());
        description.setText(event.getDescription());
        meetingDate.setText(event.getMeetingDate().toString());
        meetingTime.setText(event.getMeetingTime().toString());
        location.setText(event.getMeetingLocation());
        badge.setText(event.getBadge());
        takePartReactionNumber.setText(String.valueOf(event.getTakePartNumberOfReactions()));
        maybeReactionNumber.setText(String.valueOf(event.getMaybeNumberOfReactions()));
        notInterestedReactionNumber.setText(String.valueOf(event.getNotInterestedNumberOfReactions()));

        // Setting Event requirements in the appropriate fragment
        FragmentTransaction transaction =getChildFragmentManager().beginTransaction();
        EventRequirementFragment eventRequirementFragment;

        if (!event.getRequirements().isEmpty()) {
            requirementLayout.setVisibility(View.VISIBLE);
            for (Map.Entry<String, Boolean> requirement : event.getRequirements().entrySet()) {
                Bundle args = new Bundle();
                args.putString("requirementName", requirement.getKey());
                args.putString("requirementFulfillment", String.valueOf(requirement.getValue()));
                eventRequirementFragment = new EventRequirementFragment();
                eventRequirementFragment.setArguments(args);
                transaction.add(requirementLayout.getId(), eventRequirementFragment);
            }
        }
        transaction.commit();

        // Menu button (3 dots) on Click Listener
        eventMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                Toast.makeText(getActivity(), "Edit", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.event_menu);
                popupMenu.show();
            }
        });

        // TakePart reaction on Click Listener
        takePartReaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "take part", Toast.LENGTH_SHORT).show();
                takePartReaction.setBackgroundColor(Color.parseColor("#76D38B"));
            }
        });

        // Maybe reaction on Click Listener
        maybeReaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Maybe", Toast.LENGTH_SHORT).show();
            }
        });

        // NotInterested reaction on Click Listener
        notInterestedReaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Not interested", Toast.LENGTH_SHORT).show();
            }
        });
    }
}