package com.aueb.towardsgreen.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aueb.towardsgreen.R;

public class CreatePostFragment extends Fragment {

    EditText postTitle;
    EditText postDescription;

    Spinner location;

    Button btnSubmit;
    Button btnCancel;

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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.nomoi, android.R.layout.simple_spinner_item);

    }
}
