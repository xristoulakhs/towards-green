package activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aueb.towardsgreen.Connection;
import com.aueb.towardsgreen.R;
import com.aueb.towardsgreen.Request;
import com.aueb.towardsgreen.domain.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PostFragmentPage extends Fragment {

    private boolean noMorePosts = false;
    private Connection connection;
    private boolean refreshing = false;
    private int numberOfPostsFetched = 0;
    private LinearLayout postsLayout;
    private ScrollView postScrollView;
    private SwipeRefreshLayout postSwipeRefreshLayout;

    private FloatingActionButton floatingCreatePostBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        connection = Connection.getInstance();
        postsLayout = view.findViewById(R.id.postsLayout);
        postScrollView =  view.findViewById(R.id.post_page_scrollView);
        postSwipeRefreshLayout = view.findViewById(R.id.post_page_refreshLayout);

        floatingCreatePostBtn = view.findViewById(R.id.post_create_floating_btn);

        floatingCreatePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.container_content, new CreatePostFragment()).commit();
            }
        });

        postSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshing = true;
                PostFragmentPage.RefreshPostAsync asyncTask = new RefreshPostAsync();
                asyncTask.execute();
            }
        });

        postScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View postView = postScrollView.getChildAt(postScrollView.getChildCount()-1);
                int scrollBottom = postView.getBottom() - (postScrollView.getHeight()+ postScrollView.getScrollY());

                if(scrollBottom == 0 && !noMorePosts){
                    PostAsync asyncTask = new PostAsync("Φόρτωση περισσοτέρων δημοσιεύσεων. Παρακαλώ περιμένετε...");
                    asyncTask.execute();
                }
            }
        });

        postScrollView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        PostAsync async = new PostAsync("Παρακαλώ περιμένετε...");
        async.execute();
    }


    public void showPosts(ArrayList<Post> posts){
        boolean flag = true;
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        PostFragment postFragment;
        for(Post post: posts){
            Bundle bundle = new Bundle();
            bundle.putSerializable("post", post);
            postFragment = new PostFragment();
            postFragment.setArguments(bundle);
            if (flag && refreshing) {
                transaction.replace(postsLayout.getId(), postFragment);
                transaction.addToBackStack(null);
                flag = false;
            }
            else{
                transaction.add(postsLayout.getId(), postFragment);
            }
        }
        transaction.commit();
    }


    private ArrayList<Post> convertJsonToPosts(ArrayList<String> jsons) {
        Gson gson = new Gson();
        ArrayList<Post> posts = new ArrayList<>();
        for (String json : jsons) {
            posts.add(gson.fromJson(json, Post.class));
        }
        return posts;
    }

    private class PostAsync extends AsyncTask<String,String, ArrayList<Post>>{

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        String message;

        public PostAsync(String message) {
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage(message);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected ArrayList<Post> doInBackground(String... strings) {
            return convertJsonToPosts(connection.requestGetData(new Request("GETMOREPOSTS", String.valueOf(numberOfPostsFetched))));
        }

        @Override
        protected void onPostExecute(ArrayList<Post> requestedPosts) {
            super.onPostExecute(requestedPosts);
            numberOfPostsFetched += requestedPosts.size();
            if (requestedPosts.size() < 2) {
                noMorePosts = true;
            }
            showPosts(requestedPosts);
            progressDialog.hide();
        }
    }

    private class RefreshPostAsync extends AsyncTask<String, String, ArrayList<Post>>{

        @Override
        protected ArrayList<Post> doInBackground(String... strings) {
            return convertJsonToPosts(connection.requestGetData(new Request("GETPOSTS", String.valueOf(numberOfPostsFetched))));
        }

        @Override
        protected void onPostExecute(ArrayList<Post> requestedPosts) {
            super.onPostExecute(requestedPosts);
            showPosts(requestedPosts);
            refreshing = false;
            Toast.makeText(getActivity(), String.valueOf(requestedPosts.size()), Toast.LENGTH_SHORT).show();
            postSwipeRefreshLayout.setRefreshing(false);
        }
    }

}
