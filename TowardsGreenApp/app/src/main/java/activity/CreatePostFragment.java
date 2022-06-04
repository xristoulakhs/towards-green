package activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aueb.towardsgreen.Connection;
import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.domain.Post;

public class CreatePostFragment extends Fragment {


    private Connection connection;
    EditText postTitle;
    EditText postDescription;

    Spinner location;
    private String postLocation;

    Button btnSubmit;
    Button btnCancel;
    Button btnAddMedia;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postTitle = view.findViewById(R.id.post_title_txt);
        postDescription = view.findViewById(R.id.post_description);
        location = view.findViewById(R.id.post_location);
        btnSubmit = view.findViewById(R.id.post_btn_submit);
        btnCancel = view.findViewById(R.id.post_btn_cancel);
        connection = Connection.getInstance();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.nomoi, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(adapter);

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setPostLocation(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnAddMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: epilogi fwtografias
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDescription.getText().clear();
                postTitle.getText().clear();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Post newpost = new Post();
                if(TextUtils.isEmpty(postTitle.getText())){
                    postTitle.setError("Παρακαλώ εισάγετε ενα έγκυρο όνομα!");
                }else if(TextUtils.isEmpty(postDescription.getText())){
                    postDescription.setError("Παρακαλώ εισάγετε ενα έγκυρο επώνυμο!");
                }else if(postLocation.isEmpty()){
                    Toast.makeText(getContext(),"Παρακαλώ επιλέξτε τοποθεσία", Toast.LENGTH_LONG).show();
                }else{
                    newpost.setTitle(postTitle.getText().toString());
                    newpost.setLocation(postLocation);
                    newpost.setDescription(postDescription.getText().toString());
                    newpost.setCreator(connection.getProfile().getFullName());
                    newpost.setCreatorId(connection.getProfile().getUserID());
                    //TODO: save to dao

                }
            }
        });

    }

    //getters setters


    public String getPostLocation() {
        return postLocation;
    }

    public void setPostLocation(String postLocation) {
        this.postLocation = postLocation;
    }
}
