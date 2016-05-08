package com.hybrid.mybackend;

import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TopMainActivity extends AppCompatActivity {

    String URL = "http://www.focusvce.com/android/backend/json_test.php";
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private ArrayList<NewsFeed> newsFeedsList = new ArrayList<>();
    private RecyclerView newsFeedRecycler;
    private RecyclerAdapterMain recyclerAdapterMain;
    private TextView mTextError;
    private static final String NEWS_FEED = "news_feed";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_main);
        newsFeedRecycler = (RecyclerView)findViewById(R.id.recyclerView);
        newsFeedRecycler.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapterMain = new RecyclerAdapterMain(this);
        newsFeedRecycler.setAdapter(recyclerAdapterMain);
        mTextError = (TextView) findViewById(R.id.errorTextView);
        if(savedInstanceState != null){
            newsFeedsList  = savedInstanceState.getParcelableArrayList(NEWS_FEED);
            recyclerAdapterMain.setFeed(newsFeedsList);
        }else{
            Toast.makeText(TopMainActivity.this, "First time working ", Toast.LENGTH_SHORT).show();
            sendJsonrequest();
            

        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(NEWS_FEED, newsFeedsList);

    }

    private void sendJsonrequest() {
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Toast.makeText(TopMainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                mTextError.setVisibility(View.INVISIBLE);
                newsFeedsList = parseJsonResponse(response);
                recyclerAdapterMain.setFeed(newsFeedsList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(TopMainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                handleVolleyError(error);
            }
        });

        requestQueue.add(request);

    }

    private ArrayList<NewsFeed> parseJsonResponse(JSONArray response) {
        ArrayList<NewsFeed> newsFeedsList = new ArrayList<>();
        if(response != null && response.length()!=0) {


            StringBuilder data = new StringBuilder();
            String title, link, description, dop = null,user_name,user_pic;
            int sr_key, up, down, comment_count, uid;
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject objectFeed = response.getJSONObject(i);
                    sr_key = objectFeed.getInt("sr_key");
                    up = objectFeed.getInt("up");
                    down = objectFeed.getInt("down");
                    comment_count = objectFeed.getInt("comment_count");
                    title = objectFeed.getString("title");
                    link = objectFeed.getString("link");
                    description = objectFeed.getString("description");
                    dop = objectFeed.getString("dop");
                    user_name = objectFeed.getString("user_name");
                    user_pic = objectFeed.getString("user_pic");
                    uid = objectFeed.getInt("uid");


                    // data.append(sr_key + " " + up + " " + down + " " + comment_count + " " + title + " " + link + " " + description + " " + dop + "\n \n ");
                    NewsFeed newsFeedObj = new NewsFeed();
                    newsFeedObj.setId(sr_key);
                    newsFeedObj.setUp(up);
                    newsFeedObj.setDown(down);
                    newsFeedObj.setComment_count(comment_count);
                    newsFeedObj.setTitle(title);
                    newsFeedObj.setLink(link);
                    newsFeedObj.setDescription(description);
                    Date dateOfPost = dateFormat.parse(dop);
                    newsFeedObj.setDop(dateOfPost);
                    newsFeedObj.setUser_name(user_name);
                    newsFeedObj.setUser_pic(user_pic);
                    newsFeedObj.setUid(uid);

                    newsFeedsList.add(newsFeedObj);

                }

                //Toast.makeText(TopMainActivity.this, newsFeedsList.toString(), Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return newsFeedsList;


    }

    private void handleVolleyError(VolleyError error) {
        //if any error occurs in the network operations, show the TextView that contains the error message
        mTextError.setVisibility(View.VISIBLE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            mTextError.setText(R.string.error_timeout);

        } else if (error instanceof AuthFailureError) {
            mTextError.setText(R.string.error_auth_failure);
            //TODO
        } else if (error instanceof ServerError) {
            mTextError.setText(R.string.error_auth_failure);
            //TODO
        } else if (error instanceof NetworkError) {
            mTextError.setText(R.string.error_network);
            //TODO
        } else if (error instanceof ParseError) {
            mTextError.setText(R.string.error_parser);
            //TODO
        }
    }
}
