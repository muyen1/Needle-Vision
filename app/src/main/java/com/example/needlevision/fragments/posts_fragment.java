package com.example.needlevision.fragments;

        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;

        import com.example.needlevision.R;
        import com.google.android.material.snackbar.Snackbar;

public class posts_fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.posts_page,container,false);
        setAccountBtn(rootView);

        return rootView;
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
