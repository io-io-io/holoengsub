package umn.ac.id.holoengsub;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import umn.ac.id.holoengsub.YouTubeConfig;
import umn.ac.id.holoengsub.R;
import umn.ac.id.holoengsub.YoutubeVideoAdapter;
import umn.ac.id.holoengsub.RecyclerViewOnClickListener;

import java.util.ArrayList;
import java.util.Collections;

public class Player extends AppCompatActivity {

    private static final String TAG = Player.class.getSimpleName();
    private RecyclerView recyclerView;
    //youtube player fragment
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private ArrayList<String> youtubeVideoArrayList;

    //youtube player to play video when new video selected
    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        generateDummyVideoList();
        initializeYoutubePlayer();
        setUpRecyclerView();
        populateRecyclerView();
    }

    private void initializeYoutubePlayer() {

        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_player_fragment);

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize(YouTubeConfig.getApiKey(), new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;

                    //set the player style default
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    //cue the 1st video by default
                    youTubePlayer.cueVideo(youtubeVideoArrayList.get(0));
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                //print or show error if initialization failed
                Log.e(TAG, "Youtube Player View initialization failed");
            }
        });

    }

    /**
     * setup for the recycler view
     */
    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        //Horizontal direction recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * populate the recycler view and implement the click event here
     */
    private void populateRecyclerView() {
        final YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(this, youtubeVideoArrayList);
        recyclerView.setAdapter(adapter);

        //set click event
        recyclerView.addOnItemTouchListener(new RecyclerViewOnClickListener(this, new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (youTubePlayerFragment != null && youTubePlayer != null) {
                    //update selected position
                    adapter.setSelectedPosition(position);

                    //load selected video
                    youTubePlayer.cueVideo(youtubeVideoArrayList.get(position));
                }
            }
        }));
    }

    /**
     * method to generate dummy array list of videos
     */
    private void generateDummyVideoList() {
        youtubeVideoArrayList = new ArrayList<>();

        //get the video id array from strings.xml
        String[] videoIDArray = getResources().getStringArray(R.array.video_id_array);

        //add all videos to array list
        Collections.addAll(youtubeVideoArrayList, videoIDArray);
    }
}
