package activity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aueb.towardsgreen.Connection;
import com.aueb.towardsgreen.Event;
import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.Request;
import com.aueb.towardsgreen.domain.Post;
import com.aueb.towardsgreen.domain.Profile;
import com.google.gson.Gson;

public class PostFragment extends Fragment {
    private Post post;
    private Profile profile = Connection.getInstance().getProfile();

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

    private boolean[] userReactions;
    private String[] reactionNames = {"Agree", "Disagree"};
    private TextView[] reactionsNumber;
    private LinearLayout[] reactionsLayout;
    private TextView[] reactions;

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

        userReactions = new boolean[]{post.hasReacted("Agree", profile.getUserID()),
                post.hasReacted("Disagree", profile.getUserID())};

        reactionsNumber = new TextView[]{view.findViewById(R.id.post_reaction_agree_number),
                view.findViewById(R.id.post_reaction_disagree_number)};

        reactionsLayout = new LinearLayout[]{view.findViewById(R.id.post_agree_layout),
                view.findViewById(R.id.post_disagree_layout)};

        reactions = new TextView[]{view.findViewById(R.id.post_reaction_agree),
                view.findViewById(R.id.post_reaction_disagree)};

        for (int i = 0; i < 2; i++) {
            int finalI = i;
            reactions[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!hasClickedOtherReaction(finalI)) {
                        if (userReactions[finalI]) {
                            userReactions[finalI] = false;
                            changeReactionColor(reactionsLayout[finalI], reactionsNumber[finalI], true);
                            post.removeReaction(reactionNames[finalI], profile.getUserID());
                            if (finalI == 0) {
                                post.getUsersAndReactions().remove(profile.getUserID());
                            }
                        }
                        else {
                            userReactions[finalI] = true;
                            changeReactionColor(reactionsLayout[finalI], reactionsNumber[finalI], false);
                            post.addReaction(reactionNames[finalI], profile.getUserID());
                            if (finalI == 0) {
                                post.getUsersAndReactions().put(profile.getUserID(),reactionNames[finalI]);
                            }
                        }
                        showReactionNumbers();
                        if (finalI != 0) {
                            updatePostReaction(false);
                        }
                        else {
                            updatePostReaction(true);
                        }

                    }
                }
            });
        }

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

    private boolean hasClickedOtherReaction(int reaction) {
        for (int i = 0; i < 3; i++) {
            if (userReactions[i] && (i != reaction)) {
                return true;
            }
        }
        return  false;
    }

    private void changeReactionColor(LinearLayout linearLayout, TextView textView, boolean back) {
        int colorFrom = getResources().getColor(R.color.grey_200);
        int colorTo = getResources().getColor(R.color.green_200);
        if (back) {
            colorFrom = getResources().getColor(R.color.green_200);
            colorTo = getResources().getColor(R.color.grey_200);
        }
        ValueAnimator colorAnimatorLayout = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimatorLayout.setDuration(250);
        colorAnimatorLayout.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                textView.setBackgroundColor(Color.parseColor("#D6D6D6"));
                linearLayout.setBackgroundColor((int) colorAnimatorLayout.getAnimatedValue());
            }
        });
        colorAnimatorLayout.start();

        ValueAnimator colorAnimatorText = ValueAnimator.ofObject(new ArgbEvaluator(), colorTo, colorFrom);
        colorAnimatorText.setDuration(250);
        colorAnimatorText.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                textView.setBackgroundColor((int) colorAnimatorText.getAnimatedValue());
            }
        });
        colorAnimatorText.start();
    }

    private void showReactionNumbers() {
        reactionsNumber[0].setText(String.valueOf(post.getAgreeNumberOfReactions()));
        reactionsNumber[1].setText(String.valueOf(post.getDisagreeNumberOfReactions()));
    }


    private void updatePostReaction(boolean attendees) {
        Gson gson = new Gson();
        String updatedPost;
        if (attendees) {
            updatedPost = gson.toJson(new Post(post.getReactions(),post.getUsersAndReactions()));
        }
        else {
            updatedPost = gson.toJson(new Post(post.getReactions(), null));
        }
        String json = gson.toJson(new String[]{post.getPostID(),updatedPost});
        Connection.getInstance().requestSendDataWithoutResponse(new Request("UP", json));
    }
}
