package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aueb.towardsgreen.Event;
import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.domain.Post;

public class PostFragment extends Fragment {
    private Post post;

    private ImageView postMenu;

    private TextView post_description;
    private TextView post_title;
    private TextView publish_date;
    private TextView publish_time;
    private TextView publisher;

    private TextView agree;
    private TextView disagree;

    private ImageView userImg;
    private ImageView postImg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (Post) getArguments().getSerializable("post");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postMenu = view.findViewById(R.id.ic_posts_menu);

        publisher = view.findViewById(R.id.post_publisher_username_txt);
        publish_date = view.findViewById(R.id.post_published_date_txt);
        publish_time =  view.findViewById(R.id.post_published_time_txt);
        post_description = view.findViewById(R.id.post_description);
        post_title = view.findViewById(R.id.post_title_txt);
        postImg = view.findViewById(R.id.post_image);
        //userImg = view.findViewById(R.id.post_username_background);
        agree = view.findViewById(R.id.post_reaction_agree);
        disagree = view.findViewById(R.id.post_reaction_disagree);

        if (post.getImage() == null) {
            postImg.setVisibility(View.GONE);
        }
        else {
            postImg.setImageBitmap(post.getImageBitmap());
        }

        publisher.setText(post.getCreator());
        publish_date.setText(post.getPublishedDate());
        publish_time.setText(post.getPublishedTime());
        post_description.setText(post.getDescription());
        post_title.setText(post.getTitle());

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //prosthetei 1 sta atoma pou symfwnoun sti basi
            }
        });

        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //prosthetei 1 sta atoma pou den sumfwnoun sti basi
            }
        });

        postMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.post_menu_edit:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.post_menu);
                popupMenu.show();
            }
        });
    }



}
