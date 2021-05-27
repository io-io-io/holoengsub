package umn.ac.id.holoengsub;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.google.android.youtube.player.YouTubeThumbnailView;
import umn.ac.id.holoengsub.R;

public class YoutubeViewHolder extends RecyclerView.ViewHolder{
    public YouTubeThumbnailView videoThumbnailImageView;
    public CardView youtubeCardView;

    public YoutubeViewHolder(View itemView) {
        super(itemView);
        videoThumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image_view);
        youtubeCardView = itemView.findViewById(R.id.youtube_row_card_view);
    }
}
