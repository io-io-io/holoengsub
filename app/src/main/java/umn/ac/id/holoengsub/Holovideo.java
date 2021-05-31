package umn.ac.id.holoengsub;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import Model.VideoDetails;
import umn.ac.id.holoengsub.adapter.MyCustomAdapter;

public class Holovideo extends AppCompatActivity{

    String API_KEY="AIzaSyDX14mUIP3eui3O7yai0EEO4gb5M2SrZIM";
    ArrayList<VideoDetails> videoDetailsArrayList;
    MyCustomAdapter myCustomAdapter;
    String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UC1B3Dhu4PWXz7g39xkBxswA&maxResults=5&key=AIzaSyDX14mUIP3eui3O7yai0EEO4gb5M2SrZIM";

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        listView = (ListView)findViewById(R.id.listview);
        videoDetailsArrayList = new ArrayList<>();

        myCustomAdapter = new MyCustomAdapter(Holovideo.this,videoDetailsArrayList);
        displayVideo();

    }

    private void displayVideo() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonVideoId = jsonObject1.getJSONObject("id");
                        JSONObject jsonObjectSnippet = jsonObject1.getJSONObject("snippet");
                        JSONObject jsonObjectDefault  = jsonObjectSnippet.getJSONObject("thumbnails").getJSONObject("medium");

                        String video_id = jsonVideoId.getString("videoId");

                        VideoDetails vd = new VideoDetails();
                        vd.setVideoId(video_id);
                        vd.setTitle(jsonObjectSnippet.getString("title"));
                        vd.setDescription(jsonObjectSnippet.getString("description"));
                        vd.setUrl(jsonObjectDefault.getString("url"));
                        videoDetailsArrayList.add(vd);
                    }

                    listView.setAdapter(myCustomAdapter);
                    myCustomAdapter.notifyDataSetChanged();

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
        );

        requestQueue.add(stringRequest);
    }

}

