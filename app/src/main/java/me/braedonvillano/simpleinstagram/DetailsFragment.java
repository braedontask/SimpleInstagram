package me.braedonvillano.simpleinstagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import me.braedonvillano.simpleinstagram.models.Post;

public class DetailsFragment extends Fragment {
    ImageView imagePost;
    TextView description;
    Post main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        imagePost = view.findViewById(R.id.ivDetailsImage);
        description = view.findViewById(R.id.tvDetailsDescription);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        description.setText(main.getDescription());
        Glide.with(getActivity())
                .load(main.getImage().getUrl())
                .into(imagePost);
    }

    public void setMain(Post post) {
        main = post;
    }
}
