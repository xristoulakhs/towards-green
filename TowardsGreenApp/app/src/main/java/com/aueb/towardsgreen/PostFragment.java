package com.aueb.towardsgreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PostFragment extends Fragment {

    private TextView post_description;
    private TextView post_title;
    private TextView publish_date;
    private TextView publish_time;
    private TextView publisher;

    private ImageView userImg;
    private ImageView postImg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        publisher = view.findViewById(R.id.post_publisher_username_txt);
        publish_date = view.findViewById(R.id.post_published_date_txt);
        publish_time =  view.findViewById(R.id.post_published_time_txt);
        post_description = view.findViewById(R.id.post_description);
        post_title = view.findViewById(R.id.post_title_txt);
        postImg = view.findViewById(R.id.post_image);
        userImg = view.findViewById(R.id.post_username_background);
    }

}
