package activity;

import android.graphics.Color;
import android.os.Bundle;
import com.aueb.towardsgreen.Event;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.aueb.towardsgreen.R;

public class EventFragment extends Fragment {
    LinearLayout equipmentLayout;

    // Event TextViews
    TextView publisherUsername;
    TextView publishedTime;
    TextView publishedDate;
    TextView title;
    TextView status;
    TextView description;
    TextView meetingDate;
    TextView meetingTime;
    TextView location;
    TextView badge;

    ImageView eventMenu;
    ImageView eventImage;

    // Event Reactions
    TextView takePartReaction;
    TextView maybeReaction;
    TextView notInterestedReaction;

    private Event event;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = (Event) getArguments().getSerializable("event");
            //mParam2 = getArguments().getString(ARG_PARAM2);
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
        equipmentLayout = view.findViewById(R.id.event_equipmentLayout);

        publisherUsername = view.findViewById(R.id.event_publisher_username_txt);
        publishedTime = view.findViewById(R.id.event_published_time_txt);
        publishedDate = view.findViewById(R.id.event_published_date_txt);
        title = view.findViewById(R.id.event_title_txt);
        status = view.findViewById(R.id.event_status_txt);
        description = view.findViewById(R.id.event_description_txt);
        meetingDate = view.findViewById(R.id.event_meeting_date_txt);
        meetingTime = view.findViewById(R.id.event_meeting_time_txt);
        location = view.findViewById(R.id.event_location_txt);
        badge = view.findViewById(R.id.event_badge_txt);

        eventImage = (ImageView) view.findViewById(R.id.event_image);
        eventImage.setImageBitmap(event.getImageBitmap());

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

        Bundle args = new Bundle();
        args.putString("eq", "Trees");
        EventEquipmentFragment eventEquipmentFragment = new EventEquipmentFragment();
        eventEquipmentFragment.setArguments(args);
        getParentFragmentManager().beginTransaction().add(equipmentLayout.getId(), eventEquipmentFragment).commit();

        eventMenu = view.findViewById(R.id.ic_events_menu);
        takePartReaction = view.findViewById(R.id.event_reaction_takePart);
        maybeReaction = view.findViewById(R.id.event_reaction_maybe);
        notInterestedReaction = view.findViewById(R.id.event_reaction_notInterested);

        // Edit button (3 dots) on Click Listener
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

        // Reactions on Click Listeners
        takePartReaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "take part", Toast.LENGTH_SHORT).show();
                takePartReaction.setBackgroundColor(Color.parseColor("#76D38B"));
            }
        });

        maybeReaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Maybe", Toast.LENGTH_SHORT).show();
            }
        });

        notInterestedReaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Not interested", Toast.LENGTH_SHORT).show();
            }
        });
    }
}