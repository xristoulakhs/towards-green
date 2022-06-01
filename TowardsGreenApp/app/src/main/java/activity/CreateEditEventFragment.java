package activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import com.aueb.towardsgreen.Event;
import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.Request;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This class is used either for editing or creating a event.
 * These two functions (edit, create) share the exact same functionality.
 */
public class CreateEditEventFragment extends Fragment {
    private final int PICK_GALLERY = 1;
    private final int TAKE_PICTURE = 2;

    private Event event;

    private Calendar calendar;
    private int calendarDateYear, calendarDateMonth, calendarDateDay,
                calendarTimeHour, calendarTimeMinute;

    /**
     * This variable helps us understand whether we are in Edit mode
     * or in Edit Mode. True stands for Create mode and False for Edit mode.
     */
    private boolean createEditMode = true;
    private TextView createEditBar;

    private LinearLayout statusLayout;
    private Button changeStatusBtn;

    private EditText title;
    private EditText description;

    private Bitmap img;
    private ImageView image;
    private Button chooseImageBtn;

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
            createEditMode = false;
            event = (Event) getArguments().getSerializable("event");
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

        calendarDateYear = calendar.get(Calendar.YEAR);
        calendarDateMonth = calendar.get(Calendar.MONTH) + 1;
        calendarDateDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendarTimeHour = calendar.get(Calendar.HOUR);
        calendarTimeMinute = calendar.get(Calendar.MINUTE);

        createEditBar = view.findViewById(R.id.event_create_edit_mode_txt);

        statusLayout = view.findViewById(R.id.event_create_edit_statusLayout);
        changeStatusBtn = view.findViewById(R.id.event_create_edit_change_status_btn);

        title = view.findViewById(R.id.event_create_edit_title_edtxt);
        description = view.findViewById(R.id.event_create_edit_description_edtxt);

        image = view.findViewById(R.id.event_create_edit_image);
        chooseImageBtn = view.findViewById(R.id.event_create_edit_choose_image_btn);

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

        if (createEditMode) {
            enableCreateMode();
        }
        else {
            enableEditMode();
        }

        FragmentManager fragmentManager = getChildFragmentManager();

        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageOptionsDialog();
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

            }
        });

        chooseBadgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Fragment> fragments = fragmentManager.getFragments();
                CreateEventRequirementFragment createEventRequirementFragment = (CreateEventRequirementFragment) fragments.get(0);
                Toast.makeText(getActivity(), createEventRequirementFragment.getRequirementName(), Toast.LENGTH_SHORT).show();
            }
        });

        insertRequirementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                String requirementNameTxt = requirementName.getText().toString();
                Bundle args = new Bundle();
                args.putString("requirementName", requirementNameTxt);
                args.putBoolean("requirementFulfillment", requirementFulfillmentSwitch.isChecked());
                CreateEventRequirementFragment createEventRequirementFragment = new CreateEventRequirementFragment();
                createEventRequirementFragment.setArguments(args);
                transaction.add(requirementListLayout.getId(), createEventRequirementFragment);
                transaction.commit();

            }
        });
    }

    private void enableCreateMode() {
        event = new Event();

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

        location.setText(event.getMeetingLocation());

        if (event.getBadge() == null) {
            badge.setVisibility(View.GONE);
        }
        else {
            badge.setText(event.getBadge());
        }
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
            img = null;
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                img = BitmapFactory.decodeStream(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            image.setImageBitmap(img);
        }
        else if (requestCode == TAKE_PICTURE) {
            Bitmap img = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(img);
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
            }
        }, calendarTimeHour, calendarTimeMinute, true);
        timePickerDialog.show();
    }

    public void getEditTextsContent() {
        event.setTitle(title.getText().toString());
        event.setDescription(description.getText().toString());

    }

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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... strings) {
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }
}