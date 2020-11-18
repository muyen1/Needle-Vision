package com.example.needlevision.fragments;

        import android.content.Context;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageButton;
        import android.widget.ListView;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;

        import com.example.needlevision.Post;
        import com.example.needlevision.R;
        import com.example.needlevision.adapters.PostListAdapter;
        import com.google.android.material.snackbar.Snackbar;

        import java.util.ArrayList;

public class posts_fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.posts_page,container,false);
        setAccountBtn(rootView);

        ListView postList = (ListView) rootView.findViewById(R.id.lvPosts);
        ArrayList<Post> postArrayList = dummy();

        PostListAdapter adapter = new PostListAdapter(getActivity(), R.layout.post_layout, postArrayList);
        postList.setAdapter(adapter);

        return rootView;
    }

    private ArrayList<Post> dummy(){
        ArrayList<Post> postsList = new ArrayList<>();

        postsList.add(new Post(false,"Nov 1 2020", "4:20 pm" ,"123 Vancouver St.",  null));
        postsList.add(new Post(true,"Nov 5 2020", "1:50 am" ,"345 Burnaby St.",  "somewhere"));
        postsList.add(new Post(true,"Nov 10 2020", "6:40 pm" ,"245 Richmond St.",  "Under the tree"));
        postsList.add(new Post(false,"Nov 15 2020", "12:02 pm" ,"777 Port Moody St.", "on the Bench"));

        return postsList;
    }
    private void setAccountBtn(View view){
        ImageButton btn = view.findViewById(R.id.account_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Account", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }
}
