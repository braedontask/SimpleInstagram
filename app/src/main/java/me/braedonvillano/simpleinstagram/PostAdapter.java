package me.braedonvillano.simpleinstagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.braedonvillano.simpleinstagram.models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    static ArrayList<Post> mPosts;
    Context context;
    static Callback callback;

    public interface Callback {
        void onPostClicked(Post post);
    }

    public PostAdapter(ArrayList<Post> posts, @NonNull final Callback callback) {
        mPosts = posts;
        this.callback = callback;
    }

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.item_post, parent, false);

        return new ViewHolder(tweetView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mPosts.get(position);

        holder.tvPostDescription.setText(post.getDescription());

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPostImage);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivPostImage;
        public TextView tvPostDescription;

        public ViewHolder(View itemView) {
            super(itemView);

            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onPostClicked(mPosts.get(getAdapterPosition()));
                }
            });
        }
    }
}

//    public String getRelativeTimeAgo(String rawJsonDate) {
//        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
//        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
//        sf.setLenient(true);
//
//        String relativeDate = "";
//        try {
//            long dateMillis = sf.parse(rawJsonDate).getTime();
//            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
//                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return relativeDate;
//    }
