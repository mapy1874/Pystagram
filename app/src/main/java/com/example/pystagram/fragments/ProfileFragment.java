package com.example.pystagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.pystagram.LoginActivity;
import com.example.pystagram.Post;
import com.example.pystagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private final static String TAG = "ProfileFragment";
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView rvPosts;
    protected ProfileAdapter adapter;
    protected List<Post> mPosts;
    private Button btnLogout;
    private TextView tvHandle;
    private ImageView ivProfileImage;
    private ParseUser currentUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvPosts = view.findViewById(R.id.rvPosts);
        // Create the data source
        mPosts= new ArrayList<Post>();
        // Create the adapter
        adapter = new ProfileAdapter(getContext(), mPosts);
        // set the adapter on the RV
        rvPosts.setAdapter(adapter);
        // set the layout manager on the RV
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        rvPosts.setLayoutManager(gridLayoutManager);

        currentUser = ParseUser.getCurrentUser();
        ivProfileImage =view.findViewById(R.id.ivProfileImage);
        tvHandle = view.findViewById(R.id.tvHandle);
        btnLogout = view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                // When working with fragments, instead of using this or refering
                // to the context, always use getActivity(). You should call
                getActivity().finish();
            }
        });

        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPosts();

            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                                               android.R.color.holo_green_light,
                                               android.R.color.holo_orange_light,
                                               android.R.color.holo_red_light);
        queryPosts();


        tvHandle.setText(currentUser.getUsername());

        ParseFile profileImage = currentUser.getParseFile("profileImage");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(100));
        Glide.with(getContext()).
                load(profileImage.getUrl()).
                apply(requestOptions).
                into(ivProfileImage);

    }

    protected void queryPosts() {
        final ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        // user information would not be post automatically
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }
                mPosts.addAll(posts);
                adapter.notifyDataSetChanged();
                Log.i(TAG, "Posts appear!");
                for (int i = 0; i < posts.size(); i++){
                    Post post= posts.get(i);
                    Log.d(TAG, "Post: "+post.getDescription()+"User: "+ post.getUser().getUsername());
                }
            }
        });
    }
}
