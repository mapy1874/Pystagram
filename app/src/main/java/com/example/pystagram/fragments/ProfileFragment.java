package com.example.pystagram.fragments;

import android.util.Log;

import com.example.pystagram.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {
    private final static String TAG = "ProfileFragment";

    @Override
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
