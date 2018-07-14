package me.braedonvillano.simpleinstagram;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import me.braedonvillano.simpleinstagram.models.Post;

public class CreateFragment extends Fragment {

    private static final String imagePath = "/storage/emulated/0/DCIM/Camera/IMG_20180709_173025.jpg";
    private File imageFile;
    private EditText mtDescription;
    private ImageView ivImage;
    private Button btnPost;
    private Button btnCapture;
    private CreateFragmentInterface cfInterface;

    public CreateFragment() {
    }

    public interface CreateFragmentInterface {
        void takePicture();
        void changeTab(String tab);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        mtDescription = view.findViewById(R.id.mtDescription);
        ivImage = view.findViewById(R.id.ivPostImage);
        btnPost = view.findViewById(R.id.btnPost);
        btnCapture = view.findViewById(R.id.btnCapture);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            cfInterface = (CreateFragmentInterface) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CreateFragmentInterface cfInterface = (CreateFragmentInterface) getActivity();
                Log.d("********", "im about to call this interface method");
                cfInterface.takePicture();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = mtDescription.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();
                // final File file = new File(imagePath);
                final ParseFile parseFile = new ParseFile(imageFile);
                createPost(description, parseFile, user);
            }
        });
    }

    // put image in view and change global image for post listener
    public void setPreviewImage(Bitmap image, File photoFile) {
        ivImage.setImageBitmap(image);
        imageFile = photoFile;
    }

    private void createPost(String description, ParseFile image, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(image);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("CreateFragment", "Created Post");
                } else {
                    e.printStackTrace();
                }
            }
        });
        // cfInterface.changeTab("home");
        Toast.makeText(getContext(), "Posted", Toast.LENGTH_LONG).show();
    }
}
