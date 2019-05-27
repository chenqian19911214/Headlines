package com.marksixinfo.widgets.ninegridimg;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marksixinfo.R;
import com.marksixinfo.widgets.glide.GlideUtil;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Jaeger on 16/2/24.
 * <p>
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private LayoutInflater mInflater;
    private List<Post> mPostList;
    private int mShowStyle;

    public PostAdapter(Context context, List<Post> postList, int showStyle) {
        super();
        mPostList = postList;
        mInflater = LayoutInflater.from(context);
        mShowStyle = showStyle;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bind(mPostList.get(position));
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mShowStyle == NineGridImageView.STYLE_FILL) {
            return new PostViewHolder(mInflater.inflate(R.layout.item_post_fill_style, parent, false));
        } else {
            return new PostViewHolder(mInflater.inflate(R.layout.item_post_grid_style, parent, false));
        }
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private NineGridImageView<String> mNglContent;
        private TextView mTvContent;

        private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String s) {
//                Picasso.with(context).load(s).placeholder(R.drawable.ic_default_image).into(imageView);
                GlideUtil.loadImage(s, imageView);
            }

            @Override
            protected ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }

            @Override
            protected void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
                Toast.makeText(context, "image position is " + index, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<String> list) {
                Toast.makeText(context, "image long click position is " + index, Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        public PostViewHolder(View itemView) {
            super(itemView);
            mTvContent = (TextView) itemView.findViewById(R.id.tv_content);
            mNglContent = (NineGridImageView<String>) itemView.findViewById(R.id.ngl_images);
            mNglContent.setAdapter(mAdapter);
            mNglContent.setItemImageClickListener(new ItemImageClickListener<String>() {
                @Override
                public void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
                    Log.d("onItemImageClick", list.get(index));
                }
            });
            mNglContent.setItemImageLongClickListener(new ItemImageLongClickListener<String>() {
                @Override
                public boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<String> list) {
                    Log.d("onItemImageLongClick", list.get(index));
                    return true;
                }
            });
        }

        public void bind(Post post) {
            mNglContent.setImagesData(post.getImgUrlList(), post.getmSpanType());
            mTvContent.setText(post.getContent());

            Log.d("jaeger", "九宫格高度: " + mNglContent.getMeasuredHeight());
            Log.d("jaeger", "item 高度: " + itemView.getMeasuredHeight());
        }
    }
}
