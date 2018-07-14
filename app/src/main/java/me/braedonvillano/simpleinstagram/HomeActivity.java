package me.braedonvillano.simpleinstagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.io.File;
import java.util.List;

import me.braedonvillano.simpleinstagram.models.Post;

public class HomeActivity extends AppCompatActivity implements CreateFragment.CreateFragmentInterface, HomeFragment.HomeFragmentInterface {

    final ProfileFragment fragProfile = new ProfileFragment();
    final HomeFragment fragHome = new HomeFragment();
    final CreateFragment fragCreate = new CreateFragment();
    final DetailsFragment fragDetails = new DetailsFragment();

    public FragmentManager fragmentManager;

    public final String APP_TAG = "SimpleInstagram";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "preview.jpg";
    File photoFile;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, fragHome).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    switch (item.getItemId()) {
                        case R.id.action_home:
                            fragmentTransaction.replace(R.id.flContainer, fragHome).commit();
                            return true;
                        case R.id.action_search:
                            // do nothing for now
                            return true;
                        case R.id.action_create:
                            fragmentTransaction.replace(R.id.flContainer, fragCreate).commit();
                            return true;
                        case R.id.action_favorites:
                            // do nothing for now
                            return true;
                        case R.id.action_profile:
                            fragmentTransaction.replace(R.id.flContainer, fragProfile).commit();
                            return true;
                        default:
                            fragmentTransaction.replace(R.id.flContainer, fragHome).commit();
                            return true;
                    }
                }
            });

        loadTopPosts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miProfile:
                loadTopPosts();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void launchPostDetails(Post post) {
        // use the fragment manager to load the new details fragment
        fragDetails.setMain(post);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, fragDetails).commit();
    }

    @Override
    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);
        Uri fileProvider = FileProvider.getUriForFile(HomeActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        // create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                fragCreate.setPreviewImage(takenImage, photoFile);
            } else {
                Log.d("*********", "this thing failed");
            }
        }
    }

    @Override
    public void changeTab(String tab) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (tab) {
            case "home":
                fragmentTransaction.replace(R.id.flContainer, fragHome).commit();
                return;
            case "search":

                return;
            case "create":
                fragmentTransaction.replace(R.id.flContainer, fragCreate).commit();
                return;
            case "favorites":

                return;
            case "profile":
                fragmentTransaction.replace(R.id.flContainer, fragProfile).commit();
                return;
            default:
                fragmentTransaction.replace(R.id.flContainer, fragHome).commit();
                return;
        }
    }

    public void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("HomeActivity", "Post[" + i + "] = "
                            + objects.get(i).getDescription()
                            + "\nusername = "
                            + objects.get(i).getUser().getUsername()
                        );
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
