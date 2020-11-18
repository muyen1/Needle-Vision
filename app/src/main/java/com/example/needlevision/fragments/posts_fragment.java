package com.example.needlevision.fragments;

        import android.content.Context;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;
        import android.widget.ListView;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;

        import com.example.needlevision.Post;
        import com.example.needlevision.PostActivity;
        import com.example.needlevision.R;
        import com.example.needlevision.adapters.PostListAdapter;
        import com.google.android.material.snackbar.Snackbar;

        import java.util.ArrayList;

public class posts_fragment extends Fragment {

    ViewGroup context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.posts_page,container,false);

        context = rootView;

        // List View
        ListView postList = (ListView) rootView.findViewById(R.id.lvPosts);
        ArrayList<Post> postArrayList = dummy();

        PostListAdapter adapter = new PostListAdapter(getActivity(), R.layout.post_layout, postArrayList);
        postList.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.posts_menu, menu);
        ((PostActivity) getActivity()).setActionBarTitle("Posts");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.filter_btn){
            Snackbar.make(context, "Filter", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        } else if (id == R.id.logout_btn){
            Snackbar.make(context, "Logout", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Post> dummy(){
        ArrayList<Post> postsList = new ArrayList<>();

        postsList.add(new Post(false,"Nov 1 2020", "4:20 pm" ,"123 Vancouver St.",  null));
        postsList.add(new Post(true,"Nov 5 2020", "1:50 am" ,"345 Burnaby St.",  "somewhere"));
        postsList.add(new Post(true,"Nov 10 2020", "6:40 pm" ,"245 Richmond St.",  "Under the tree"));
        postsList.add(new Post(false,"Nov 15 2020", "12:02 pm" ,"777 Port Moody St.", "on the Bench"));
        postsList.add(new Post(false,"Nov 10 2020", "3:30 pm" ,"245 Whistler St.",  "on the tree"));
        postsList.add(new Post(false,"Nov 20 2020", "11:12 pm" ,"777 Coquitlam St.", "beach"));

        return postsList;
    }

}
