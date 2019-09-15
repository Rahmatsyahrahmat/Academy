package com.rahmatsyah.academy.ui.bookmark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rahmatsyah.academy.R;
import com.rahmatsyah.academy.data.source.local.entity.CourseEntity;
import com.rahmatsyah.academy.ui.detail.DetailCourseActivity;

public class BookmarkPagedAdapter extends PagedListAdapter<CourseEntity, BookmarkPagedAdapter.ViewHolder> {

    private static DiffUtil.ItemCallback<CourseEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<CourseEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull CourseEntity oldItem, @NonNull CourseEntity newItem) {
            return oldItem.getCourseId().equals(newItem.getCourseId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull CourseEntity oldItem, @NonNull CourseEntity newItem) {
            return oldItem.equals(newItem);
        }
    };

    private BookmarkFragmentCallback bookmarkFragmentCallback;

    protected BookmarkPagedAdapter(BookmarkFragmentCallback bookmarkFragmentCallback) {
        super(DIFF_CALLBACK);
        this.bookmarkFragmentCallback = bookmarkFragmentCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items_bookmark,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CourseEntity bookmark = getItem(position);
        if (bookmark!=null){
            holder.tvTitle.setText(bookmark.getTitle());
            holder.tvDate.setText(String.format("Deadline %s", bookmark.getDeadline()));
            holder.tvDescription.setText(bookmark.getDescription());
            holder.itemView.setOnClickListener(v->{
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, DetailCourseActivity.class);
                String courseId = bookmark.getCourseId();
                intent.putExtra(DetailCourseActivity.EXTRA_COURSE,courseId);
                context.startActivity(intent);
            });

            holder.imgShare.setOnClickListener(v->{
                CourseEntity courseEntity = new CourseEntity(
                        bookmark.getCourseId(),
                        bookmark.getTitle(),
                        bookmark.getDescription(),
                        bookmark.getDeadline(),
                        false,
                        bookmark.getImagePath()
                );
                bookmarkFragmentCallback.onShareClick(courseEntity);
            });
        }
    }

    CourseEntity getItemById(int swipedPosition) {
        return getItem(swipedPosition);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle;
        final TextView tvDescription;
        final TextView tvDate;
        final ImageButton imgShare;
        final ImageView imgPoster;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDescription = itemView.findViewById(R.id.tv_item_description);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            imgShare = itemView.findViewById(R.id.img_share);
            imgPoster = itemView.findViewById(R.id.img_poster);
        }
    }
}
