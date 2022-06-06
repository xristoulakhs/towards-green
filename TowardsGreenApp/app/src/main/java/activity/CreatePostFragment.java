package activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aueb.towardsgreen.Connection;
import com.aueb.towardsgreen.Event;
import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.Request;
import com.aueb.towardsgreen.domain.Post;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Map;

public class CreatePostFragment extends Fragment {
    private final int PICK_GALLERY = 1;
    private final int TAKE_PICTURE = 2;

    private Post newPost;
    private boolean createEditMode = true;
    private Connection connection;
    EditText postTitle;
    EditText postDescription;

    TextView createEditBar;

    Spinner location;
    private String postLocation;

    Button btnSubmit;
    Button btnCancel;
    Button btnAddMedia;

    private Bitmap postImageResource;
    private ImageView image;

    ArrayAdapter<CharSequence> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            createEditMode = false;
            newPost = (Post) getArguments().getSerializable("post");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postTitle = view.findViewById(R.id.create_post_title_txt);
        postDescription = view.findViewById(R.id.create_post_description_txt);
        location = view.findViewById(R.id.create_post_location);
        image= view.findViewById(R.id.post_image);
        btnSubmit = view.findViewById(R.id.post_btn_submit);
        btnCancel = view.findViewById(R.id.post_btn_cancel);
        btnAddMedia = view.findViewById(R.id.post_media_btn);
        connection = Connection.getInstance();
        createEditBar = view.findViewById(R.id.createPost);

        adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.nomoi, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(adapter);

        if (createEditMode) {
            newPost = new Post();
        }
        else {
            enableEditMode();
        }

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
                showImageOptionsDialog();
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

                if(TextUtils.isEmpty(postTitle.getText())){
                    postTitle.setError("Παρακαλώ εισάγετε ενα έγκυρο όνομα!");
                }else if(TextUtils.isEmpty(postDescription.getText())){
                    postDescription.setError("Παρακαλώ εισάγετε ενα έγκυρο επώνυμο!");
                }else if(postLocation.isEmpty()){
                    Toast.makeText(getContext(),"Παρακαλώ επιλέξτε τοποθεσία", Toast.LENGTH_LONG).show();
                }else{
                    newPost.setTitle(postTitle.getText().toString());
                    newPost.setLocation(postLocation);
                    newPost.setDescription(postDescription.getText().toString());
                    newPost.setCreator(connection.getProfile().getFullName());
                    newPost.setCreatorID(connection.getProfile().getUserID());
                    setPublicationDateAndTime();
                    if (postImageResource != null) {
                        newPost.setImage(postImageResource);
                    }

                    //TODO: save to dao
                    CreatePostTask createPostTask = new CreatePostTask();
                    createPostTask.execute();

                }
            }
        });

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
            postImageResource = null;
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                postImageResource = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(postImageResource);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == TAKE_PICTURE) {
            postImageResource = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(postImageResource);
        }
    }
    public void setPostLocation(String postLocation) {
        this.postLocation = postLocation;
    }

    private void setPublicationDateAndTime() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        String publishedDate = day + "/" + month + "/" + year;
        String publishedTime = hour + ":" + minute;

        newPost.setPublishedDate(publishedDate);
        newPost.setPublishedTime(publishedTime);
    }

    private void enableEditMode() {
        createEditBar.setText("Επεξεργασία δημοσίευσης");
        btnSubmit.setText("Αποθήκευση αλλαγών");

        postTitle.setText(newPost.getTitle());
        postDescription.setText(newPost.getDescription());
        if (newPost.getImage() != null) {
            image.setImageBitmap(newPost.getImageBitmap());
        }

        location.setSelection(adapter.getPosition(newPost.getLocation()));

    }

    private class CreatePostTask extends AsyncTask<String, String, Boolean> {
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
            String json = gson.toJson(newPost);
            Request request = null;
            if (createEditMode) {
                request = new Request("INPOST", json);
            }
            else {
                json = gson.toJson(new String[]{newPost.getPostID(), json});
                request = new Request("UPPOST", json);
            }
            return Connection.getInstance().requestSendData(request);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            showAlertDialog(result);
        }
    }

    private void showAlertDialog(boolean result) {
        String successMessage = "Μόλις δημιούργησες επιτυχώς μία νέα δημοσίευση!";
        String failureMessage = "Η δημοσίευση δεν δημιουργήθηκε. Ξαναπροσπάθησε σε λίγα λεπτά!";

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
                getParentFragmentManager().beginTransaction().replace(R.id.container_content, new PostFragmentPage()).commit();
            }
        }, 5000);
    }
}
