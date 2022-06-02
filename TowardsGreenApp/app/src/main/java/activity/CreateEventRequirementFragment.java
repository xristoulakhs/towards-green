package activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.aueb.towardsgreen.R;

public class CreateEventRequirementFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String REQUIREMENT_NAME = "requirementName";
    private static final String REQUIREMENT_FULFILLEMENT = "requirementFulfillment";

    // TODO: Rename and change types of parameters
    private String requirementName;
    private boolean requirementFulfillment;

    private TextView name;
    private Switch fulfillmentSwitch;
    private ImageButton editBtn;
    private ImageButton deleteBtn;


    public String getRequirementName() {
        return requirementName;
    }

    public boolean getRequirementFulfillment() {
        return requirementFulfillment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requirementName = getArguments().getString(REQUIREMENT_NAME);
            requirementFulfillment = getArguments().getBoolean(REQUIREMENT_FULFILLEMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_event_requirement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.create_event_requirement_name_txt);
        fulfillmentSwitch = view.findViewById(R.id.create_event_requirement_fulfillment_switch);
        editBtn = view.findViewById(R.id.create_event_requirement_edit_btn);
        deleteBtn = view.findViewById(R.id.create_event_requirement_delete_btn);

        name.setText(requirementName);
        fulfillmentSwitch.setChecked(requirementFulfillment);

        fulfillmentSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requirementFulfillment = fulfillmentSwitch.isChecked();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRenameDialog();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(CreateEventRequirementFragment.this);
                transaction.commit();
            }
        });
    }

    private void showRenameDialog() {
        EditText editText = new EditText(getActivity());
        editText.setText(requirementName);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Πληκτρολογήστε παρακάτω το νέο όνομα:")
                .setView(editText)
                .setPositiveButton("Αποθήκευση", dialogClickListener)
                .setNegativeButton("Ακύρωση", dialogClickListener);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String renamedRequirement = editText.getText().toString();
                requirementName = renamedRequirement;
                name.setText(renamedRequirement);
                alertDialog.dismiss();
            }
        });
    }
}