package activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aueb.towardsgreen.Connection;
import com.aueb.towardsgreen.Event;
import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.Request;
import com.aueb.towardsgreen.domain.Badge;
import com.aueb.towardsgreen.domain.Post;
import com.aueb.towardsgreen.domain.Profile;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used either for editing or creating a event.
 * These two functions (edit, create) share the exact same functionality.
 */
public class CreateEditEventFragment extends Fragment {
    private final int PICK_GALLERY = 1;
    private final int TAKE_PICTURE = 2;

    private Event event;
    private Post post;
    private Badge createdBadge = null;
    private Badge choosenBadge = null;
    private ArrayList<Badge> badges;

    private Calendar calendar;
    private int calendarDateYear, calendarDateMonth, calendarDateDay,
                calendarTimeHour, calendarTimeMinute;

    /**
     * This variable helps us understand whether we are in Edit mode
     * or in Edit Mode. True stands for Create mode and False for Edit mode.
     */
    private boolean createEditMode = true;
    private boolean createFromPostMode = false;
    private TextView createEditBar;

    private TextView status;
    private LinearLayout statusLayout;
    private Button changeStatusBtn;

    private EditText title;
    private EditText description;

    private LinearLayout rewardLayout;
    private EditText rewardPoints;

    private Bitmap eventImageResource;
    private ImageView image;
    private Button chooseImageBtn;
    private ImageButton deleteImageBtn;

    private TextView dateDay;
    private TextView dateMonth;
    private TextView dateYear;
    private Button chooseDateBtn;

    private TextView timeHour;
    private TextView timeMinute;
    private Button chooseTimeBtn;

    private EditText location;

    private TextView badge;
    private Button createBadgeBtn;
    private Button chooseBadgeBtn;

    private TextView requirementTitle;
    private EditText requirementName;
    private Switch requirementFulfillmentSwitch;
    private Button insertRequirementBtn;
    private LinearLayout requirementLayout;
    private LinearLayout requirementListLayout;

    // This button acts according to the circumstances.
    // If the user wants to create an event, "Create event" will be shown.
    // Correspondingly, "Save changes" will be shown in edit event.
    private Button createSaveBtn;

    private boolean hasDateChanged, hasTimeChanged = false;

    // Event variables
    String eventTitle, eventDescription, eventMeetingLocation, eventBadge;
    int[] eventMeetingDate, eventMeetingTime;

    /**
     * If an event object has been passed through bundle in this fragment,
     * that means that we are in Edit mode. Otherwise, we are in Create mode and
     * no prior event information is needed.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mode = getArguments().getString("mode");
            if (mode.equals("edit")) {
                createEditMode = false;
                event = (Event) getArguments().getSerializable("event");
            }
            else if (mode.equals("createFromPost")) {
                createFromPostMode = true;
                post = (Post) getArguments().getSerializable("post");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_edit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendar = Calendar.getInstance();

        FetchBadgesAsyncTask fetchBadgesAsyncTask = new FetchBadgesAsyncTask();
        fetchBadgesAsyncTask.execute();

        calendarDateYear = calendar.get(Calendar.YEAR);
        calendarDateMonth = calendar.get(Calendar.MONTH) + 1;
        calendarDateDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendarTimeHour = calendar.get(Calendar.HOUR);
        calendarTimeMinute = calendar.get(Calendar.MINUTE);

        createEditBar = view.findViewById(R.id.event_create_edit_mode_txt);

        status = view.findViewById(R.id.event_create_edit_status_txt);
        statusLayout = view.findViewById(R.id.event_create_edit_statusLayout);
        changeStatusBtn = view.findViewById(R.id.event_create_edit_change_status_btn);

        title = view.findViewById(R.id.event_create_edit_title_edtxt);
        description = view.findViewById(R.id.event_create_edit_description_edtxt);

        rewardLayout = view.findViewById(R.id.event_create_edit_rewardLayout);
        rewardPoints = view.findViewById(R.id.event_create_points_reward_edtxt);

        image = view.findViewById(R.id.event_create_edit_image);
        chooseImageBtn = view.findViewById(R.id.event_create_edit_choose_image_btn);
        deleteImageBtn = view.findViewById(R.id.event_create_edit_delete_image_btn);

        dateDay = view.findViewById(R.id.event_create_edit_date_day_txt);
        dateMonth = view.findViewById(R.id.event_create_edit_date_month_txt);
        dateYear = view.findViewById(R.id.event_create_edit_date_year_txt);
        chooseDateBtn = view.findViewById(R.id.event_create_edit_choose_date_btn);

        timeHour = view.findViewById(R.id.event_create_edit_time_hour_txt);
        timeMinute = view.findViewById(R.id.event_create_edit_time_minute_txt);
        chooseTimeBtn = view.findViewById(R.id.event_create_edit_choose_time_btn);

        location = view.findViewById(R.id.event_create_edit_location_edtxt);

        badge = view.findViewById(R.id.event_create_edit_badge_txt);
        createBadgeBtn = view.findViewById(R.id.event_create_edit_create_badge_btn);
        chooseBadgeBtn = view.findViewById(R.id.event_create_edit_choose_badge_btn);

        requirementTitle = view.findViewById(R.id.event_create_edit_requirement_title);
        requirementLayout = view.findViewById(R.id.event_create_edit_requirementLayout);
        requirementName = view.findViewById(R.id.event_create_edit_requirement_name_edtxt);
        requirementFulfillmentSwitch = view.findViewById(R.id.event_create_edit_requirement_fulfillment_switch);
        requirementListLayout = view.findViewById(R.id.event_create_edit_requirementListLayout);
        insertRequirementBtn = view.findViewById(R.id.event_create_edit_insert_requirement_btn);

        createSaveBtn = view.findViewById(R.id.event_create_edit_save_create_btn);

        // TODO: Add Edit mode implementation
        // TODO: Add create an event from post (need post implementation first)
        if (createFromPostMode) {
            event = new Event();
            enableCreateFromPostMode();
        }
        else if (createEditMode) {
            event = new Event();
            enableCreateMode();
        }
        else {
            enableEditMode();
        }

        changeStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeStatusDialog();
            }
        });

        FragmentManager fragmentManager = getChildFragmentManager();

        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageOptionsDialog();
            }
        });

        deleteImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImageBtn.setVisibility(View.GONE);
                image.setImageResource(0);
                eventImageResource = null;
            }
        });

        chooseDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        chooseTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        createBadgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateBadgeDialog();
            }
        });

        chooseBadgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAvailableBadgesDialog(getBadgeNames());
            }
        });

        insertRequirementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String requirementNameTxt = requirementName.getText().toString();
                if (checkInputValidity(requirementNameTxt)) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    Bundle args = new Bundle();
                    args.putString("requirementName", requirementNameTxt);
                    args.putBoolean("requirementFulfillment", requirementFulfillmentSwitch.isChecked());
                    CreateEventRequirementFragment createEventRequirementFragment = new CreateEventRequirementFragment();
                    createEventRequirementFragment.setArguments(args);
                    transaction.add(requirementListLayout.getId(), createEventRequirementFragment);
                    transaction.commit();
                }
            }
        });

        createSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEventFieldsValidity()) {
                    CreateEditEventTask createEditEventTask = new CreateEditEventTask();
                    createEditEventTask.execute();
                }
            }
        });
    }

    private void enableCreateMode() {
        statusLayout.setVisibility(View.GONE);

        dateYear.setText(String.valueOf(calendarDateYear));
        dateMonth.setText(String.valueOf(calendarDateMonth));
        dateDay.setText(String.valueOf(calendarDateDay));

        timeHour.setText(String.valueOf(calendarTimeHour));
        timeMinute.setText(String.valueOf(calendarTimeMinute));
    }

    private void enableEditMode() {
        createEditBar.setText("Επεξεργασία εκδήλωσης");
        createSaveBtn.setText("Αποθήκευση αλλαγών");

        statusLayout.setBackgroundColor(Color.parseColor(event.getStatusColor()));
        title.setText(event.getTitle());
        description.setText(event.getDescription());

        dateYear.setText(String.valueOf(event.getMeetingDate()[0]));
        dateMonth.setText(String.valueOf(event.getMeetingDate()[1]));
        dateDay.setText(String.valueOf(event.getMeetingDate()[2]));

        timeHour.setText(String.valueOf(event.getMeetingTime()[0]));
        timeMinute.setText(String.valueOf(event.getMeetingTime()[1]));

        if (event.getImage() != null) {
            image.setImageBitmap(event.getImageBitmap());
        }

        location.setText(event.getMeetingLocation());

        if (event.getBadge() == null) {
            badge.setVisibility(View.GONE);
        }
        else {
            badge.setText(event.getBadge().getTitle());
        }

        setRequirementFragments();
    }

    // TODO: to be implemented , waiting for Post
    private void enableCreateFromPostMode() {
        enableCreateMode();

        rewardLayout.setVisibility(View.VISIBLE);

        title.setText(post.getTitle());
        description.setText(post.getDescription());
    }

    private void showChangeStatusDialog() {
        String[] choices = {"Ανοιχτό προς συμμετοχή",
                "Σε εξέλιξη",
                "Ολοκληρώθηκε"};

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int choice) {
                switch (choice) {
                    case 0:
                        setEventStatus(Event.Status.OPEN);
                        break;

                    case 1:
                        setEventStatus(Event.Status.IN_PROGRESS);
                        break;

                    case 2:
                        setEventStatus(Event.Status.CLOSED);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialogInterface.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Επίλεξε μία κατάσταση:").setItems(choices, dialogClickListener)
                .setNegativeButton("Ακύρωση", dialogClickListener).show();
    }

    private void setEventStatus(Event.Status eventStatus) {
        event.setStatus(eventStatus);
        status.setText(eventStatus.toString());
        statusLayout.setBackgroundColor(Color.parseColor(eventStatus.getColor()));
    }

    private void showImageOptionsDialog() {
        String[] choices = {"Εύρεση από βιβλιοθήκη φωτογραφιών",
        "Λήψη φωτογραφίας"};

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int choice) {
                switch (choice) {
                    case 0:
                        choosePicture();
                        break;

                    case 1:
                        takePicture();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialogInterface.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Επίλεξε πηγή εύρεσης φωτογραφίας:").setItems(choices, dialogClickListener)
                .setNegativeButton("Ακύρωση", dialogClickListener).show();
    }

    private void choosePicture() {
        Intent choosePictureIntent = new Intent();
        choosePictureIntent.setType("image/*");
        choosePictureIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(choosePictureIntent, PICK_GALLERY);
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_GALLERY) {
            eventImageResource = null;
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                eventImageResource = BitmapFactory.decodeStream(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            deleteImageBtn.setVisibility(View.VISIBLE);
            image.setImageBitmap(eventImageResource);
        }
        else if (requestCode == TAKE_PICTURE) {
            deleteImageBtn.setVisibility(View.VISIBLE);
            eventImageResource = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(eventImageResource);
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendarDateYear = calendar.get(Calendar.YEAR);
        calendarDateMonth = calendar.get(Calendar.MONTH) + 1;
        calendarDateDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //Calendar calendarPicker = Calendar.getInstance();
                //calendarPicker.set(year, month, day);
                dateYear.setText(String.valueOf(year));
                dateMonth.setText(String.valueOf(month + 1));
                dateDay.setText(String.valueOf(day));
                hasDateChanged = true;
            }
        }, calendarDateYear, calendarDateMonth, calendarDateDay);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendarTimeHour = calendar.get(Calendar.HOUR);
        calendarTimeMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                timeHour.setText(String.valueOf(hour));
                timeMinute.setText(String.valueOf(minute));
                hasTimeChanged = true;
            }
        }, calendarTimeHour, calendarTimeMinute, true);
        timePickerDialog.show();
    }

    private void showCreateBadgeDialog() {
        AlertDialog alertDialog;
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(getActivity());
        View layoutView = getLayoutInflater().inflate(R.layout.create_badge_dialog, null);

        builderDialog.setView(layoutView);

        EditText badgeName = layoutView.findViewById(R.id.create_badge_name_edtxt);
        EditText badgePoints = layoutView.findViewById(R.id.create_badge_points_edtxt);
        Button createBadgeBtn = layoutView.findViewById(R.id.create_badge_create_btn);

        alertDialog = builderDialog.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        createBadgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                String publishedBadgeDate = day + "/" + month + "/" + year;
                String badgeNameText = badgeName.getText().toString();
                int badgePointsText = Integer.parseInt(badgePoints.getText().toString());
                createdBadge = new Badge(badgeNameText, publishedBadgeDate, badgePointsText);
                badge.setText(badgeNameText);

                CreateBadgeAsyncTask createBadgeEventTask = new CreateBadgeAsyncTask();
                createBadgeEventTask.execute();
            }
        });
    }

    private void showAvailableBadgesDialog(ArrayList<String> badgeNames) {
        String[] choices = badgeNames.toArray(new String[0]);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int choice) {
                if (choice != DialogInterface.BUTTON_NEGATIVE) {
                    choosenBadge = badges.get(choice);
                    badge.setText(choosenBadge.getTitle());
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Επίλεξε ένα βραβείο:").setItems(choices, dialogClickListener)
                .setNegativeButton("Ακύρωση", dialogClickListener).show();
    }

    private boolean checkEventFieldsValidity() {
        eventTitle = title.getText().toString();
        eventDescription = description.getText().toString();
        eventMeetingLocation = location.getText().toString();

        if (checkInputValidity(eventTitle) &&
                checkInputValidity(eventDescription) &&
                checkInputValidity(eventMeetingLocation) &&
                checkDateAndTimeValidity()) {
            return true;
        }

        return false;
    }

    private boolean checkInputValidity(String input) {
        if (input.equals("")) {
            Toast.makeText(getActivity(), "Παρακαλώ συμπληρώστε όλα τα υποχρεωτικά πεδία.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkDateAndTimeValidity() {
        calendar = Calendar.getInstance();

        if (!createEditMode && !hasDateChanged && !hasTimeChanged) {
            return true;
        }

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int year = Integer.parseInt(dateYear.getText().toString());
        int month = Integer.parseInt(dateMonth.getText().toString());
        int day = Integer.parseInt(dateDay.getText().toString());

        eventMeetingDate = new int[]{year, month, day};

        int currentHour = calendar.get(Calendar.HOUR);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int hour = Integer.parseInt(timeHour.getText().toString());
        int minute = Integer.parseInt(timeMinute.getText().toString());

        eventMeetingTime = new int[]{hour, minute};

        if (year >= currentYear && month >= currentMonth && day >= currentDay) {
            if (day == currentDay && hour < currentHour && minute < currentMinute) {
                Toast.makeText(getActivity(), "Παρακαλώ εισάγετε μία έγκυρη ώρα.", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        Toast.makeText(getActivity(), "Παρακαλώ εισάγετε μία έγκυρη ημερομηνία.", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void setRequirementFragments() {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Map.Entry<String, Boolean> requirement : event.getRequirements().entrySet()) {
            Bundle args = new Bundle();
            args.putString("requirementName", requirement.getKey());
            args.putBoolean("requirementFulfillment", requirement.getValue());
            CreateEventRequirementFragment createEventRequirementFragment = new CreateEventRequirementFragment();
            createEventRequirementFragment.setArguments(args);
            transaction.add(requirementListLayout.getId(), createEventRequirementFragment);
        }
        transaction.commit();
    }

    private void setEventFields() {
        Profile profile = Connection.getInstance().getProfile();
        event.setCreatorID(profile.getUserID());
        event.setCreator(profile.getFirstName());
        event.setTitle(eventTitle);
        event.setDescription(eventDescription);
        if (eventImageResource != null) {
            event.setImage(eventImageResource);
        }
        event.setMeetingDate(eventMeetingDate);
        event.setMeetingTime(eventMeetingTime);
        event.setMeetingLocation(eventMeetingLocation);
        // TODO: add badge implementation
        event.setRequirements(getRequirementsFromFragments());

        if (createdBadge != null) {
            event.setBadge(createdBadge);
        }
        else if (choosenBadge != null) {
            event.setBadge(choosenBadge);
        }
    }

    private void setPublicationDateAndTime() {
        calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        event.setPublishedDate(year, month, day);
        event.setPublishedTime(hour, minute);
    }

    private HashMap<String, Boolean> getRequirementsFromFragments() {
        HashMap<String, Boolean> requirements = new HashMap<>();
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        CreateEventRequirementFragment createEventRequirementFragment;
        for (Fragment fragment : fragments) {
            createEventRequirementFragment = (CreateEventRequirementFragment) fragment;
            requirements.put(createEventRequirementFragment.getRequirementName(),
                             createEventRequirementFragment.getRequirementFulfillment());
        }
        return requirements;
    }

    // TODO: Create AsyncTask class that extends AsyncTask to save coding lines

    /**
     * This AsyncTask is destined to ensure that Event has created successfully.
     * By created successfully we mean that created event record should have been stored in
     * the database. So, what we do is send the record in the database and wait for a boolean
     * variable. Sending and parsing the Event may take seconds, so we want to show a progress dialog
     * during this process to keep user informed that this action is taking place (rather than leaving a white
     * screen and letting one think that app may have crashed).
     * True corresponds to success and False to failure. Additionally, we show our
     * custom alert Dialogs to inform user for the result of one's action.
     */
    private class CreateEditEventTask extends AsyncTask<String, String, Boolean> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Παρακαλώ περιμένετε...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Boolean doInBackground(String... strings) {
            setEventFields();
            setPublicationDateAndTime();
            Gson gson = new Gson();
            String json = gson.toJson(event);
            Request request = null;
            if (createEditMode) {
                request = new Request("INEV", json);

                if (createFromPostMode && !rewardPoints.getText().toString().equals("")) {
                    String jsonReward = gson.toJson(new String[]{post.getCreatorID(), rewardPoints.getText().toString()});
                    Request requestForReward = new Request("REWARDPR", jsonReward);
                    Connection.getInstance().requestSendDataWithoutResponse(requestForReward);
                }
            }
            else {
                json = gson.toJson(new String[]{event.getEventID(), json});

                if (event.getStatus() == Event.Status.CLOSED) {
                    request = new Request("UPCLEV", json);
                }
                else {
                    request = new Request("UPEV", json);
                }
            }

            return Connection.getInstance().requestSendData(request);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            String successMessage = "Μόλις δημιούργησες επιτυχώς μία νέα εκδήλωση!";
            String failureMessage = "Η εκδήλωση δεν δημιουργήθηκε. Ξαναπροσπάθησε σε λίγα λεπτά!";
            if (!createEditMode) {
                successMessage = "Η εκδήλωση τροποποιήθηκε επιτυχώς!";
                failureMessage = "Η εκδήλωση δεν τροποποιήθηκε λόγω σφάλματος. Ξαναπροσπάθησε σε λίγα λεπτά!";
            }
            showAlertDialog(result, successMessage, failureMessage, true);
        }
    }

    private class CreateBadgeAsyncTask extends AsyncTask<String, String, Boolean> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Παρακαλώ περιμένετε...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Boolean doInBackground(String... strings) {
            Gson gson = new Gson();
            String json = gson.toJson(createdBadge);
            Request request = new Request("INBDG", json);

            return Connection.getInstance().requestSendData(request);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            String successMessage = "Μόλις δημιούργησες επιτυχώς ένα νέο βραβείο!";
            String failureMessage = "Το βραβείο δεν δημιουργήθηκε. Ξαναπροσπάθησε σε λίγα λεπτά!";
            showAlertDialog(result, successMessage, failureMessage, false);
        }
    }

    private class FetchBadgesAsyncTask extends AsyncTask<String, String, ArrayList<String>> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            Request request = new Request("GETALLBDG", "");
            ArrayList<String> jsonBadges = Connection.getInstance().requestGetData(request);

            return jsonBadges;
        }

        @Override
        protected void onPostExecute(ArrayList<String> jsonBadges) {
            super.onPostExecute(jsonBadges);
            badges = convertJsonToBadges(jsonBadges);
            Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Badge> convertJsonToBadges(ArrayList<String> jsons) {
        ArrayList<Badge> badges = new ArrayList<>();
        Gson gson = new Gson();
        for (String json : jsons) {
            badges.add(gson.fromJson(json, Badge.class));
        }
        return badges;
    }

    private ArrayList<String> getBadgeNames() {
        ArrayList<String> badgeNames = new ArrayList<>();
        for (Badge badge : badges) {
            badgeNames.add(badge.getTitle());
        }
        return badgeNames;
    }

    private void showAlertDialog(boolean result, String successMessage, String failureMessage, boolean redirect) {
        AlertDialog alertDialog;
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(getActivity());
        View layoutView = null;

        if (result) {
            layoutView = getLayoutInflater().inflate(R.layout.success_dialog, null);
            TextView successMsg = layoutView.findViewById(R.id.success_dialog_txt);
            successMsg.setText(successMessage);
        }
        else {
            layoutView = getLayoutInflater().inflate(R.layout.failure_dialog, null);
            TextView failureMsg = layoutView.findViewById(R.id.failure_dialog_txt);
            failureMsg.setText(failureMessage);
        }

        builderDialog.setView(layoutView);

        alertDialog = builderDialog.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                if (redirect) {
                    getParentFragmentManager().beginTransaction().replace(R.id.container_content, new EventPageFragment()).commit();
                }
            }
        }, 5000);
    }
}