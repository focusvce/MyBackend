package com.hybrid.mybackend;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerAdapterMain extends RecyclerView.Adapter<RecyclerAdapterMain.ViewholderNewsFeed> {

    public static final String KEY_UID = "user_id";
    private static final String KEY_LID = "lid";
    private static final String KEY_PID = "pid";
    private ArrayList<NewsFeed> newsFeedArrayList = new ArrayList<>();
    private LayoutInflater layoutInflater = null;
    private DateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd 'at' h:mm a");
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private MyApplication myApplication;
     String user_id;



    public RecyclerAdapterMain(Context context){
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        imageLoader = volleySingleton.getImageLoader();
        // below is for getting application context
        myApplication = MyApplication.getInstance();
    }

    public void setFeed(ArrayList<NewsFeed> newsFeedArrayList){
        this.newsFeedArrayList = newsFeedArrayList;
        notifyDataSetChanged();
    }


    @Override
    public ViewholderNewsFeed onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.raw_layout, parent, false);

        ViewholderNewsFeed viewholderNewsFeed = new ViewholderNewsFeed(view);

        return viewholderNewsFeed;
    }

    @Override
    public void onBindViewHolder(final ViewholderNewsFeed holder, int position) {

        final NewsFeed feedObject  = newsFeedArrayList.get(position);
        holder.title.setText(feedObject.getTitle());
        holder.description.setText(feedObject.getDescription());
        holder.comment_count.setText(feedObject.getComment_count()+"");
        holder.urll.setText(feedObject.getLink());
        holder.up.setText(feedObject.getUp()+"");
        Date gotDate = feedObject.getDop();
        String formatedDate = dateFormat.format(gotDate);
        holder.timeAndDate.setText(formatedDate);
        holder.shared_by.setText(feedObject.getUser_name());
        String urlThumbnail = feedObject.getUser_pic();
        if(urlThumbnail != null){

            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.avatar.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(myApplication.getBaseContext(),"Error loading image", Toast.LENGTH_LONG).show();
                    holder.avatar.setImageResource(R.drawable.ic_profile_black);
                }
            });

        }else {
            // default
            holder.avatar.setImageResource(R.drawable.ic_profile_black);
        }

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.up_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String UP_DOWN_URL = "http://focusvce.com/android/backend/up_count.php";
                doNetworkThiny(holder, UP_DOWN_URL);
            }
        });

        holder.down_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String UP_DOWN_URL = "http://focusvce.com/android/backend/down_count.php";
                doNetworkThiny(holder,UP_DOWN_URL);
            }
        });

        holder.shared_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Touch:", "He touched");
                final String USER_DETAILS_FOR_ACTIVITY = "http://focusvce.com/android/backend/details_test.php";
                final int user_id_int = feedObject.getUid();
                //convert into string for sending in the params
                 user_id = Integer.toString(user_id_int);

                // get the details and start the user activity with the details
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, USER_DETAILS_FOR_ACTIVITY, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("MyResponse: ", response.toString());
                                String u_name = response.getString("user_name");
                                String u_email = response.getString("user_email");
                                String u_status = response.getString("user_status");
                                String u_description = response.getString("user_description");
                                String u_pic = response.getString("user_pic");
                                Intent intent = new Intent(myApplication.getApplicationContext(), UserProfile.class);
                                intent.putExtra("USER_NAME", u_name);
                                intent.putExtra("USER_STATUS", u_status);
                                intent.putExtra("USER_DESC", u_description);
                                intent.putExtra("USER_EMAIL", u_email);
                                intent.putExtra("USER_PIC", u_pic);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                myApplication.startActivity(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("user-Activity(volley):", error.toString());

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put(KEY_UID,user_id);
                        Log.e("User-id", params.get(KEY_UID));
                        return params;

                    }

                };

                /*StringRequest request = new StringRequest(Request.Method.POST, USER_DETAILS_FOR_ACTIVITY, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response: ", response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error: ", error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put(KEY_UID,user_id);
                        return params;

                    }

                };*/

                requestQueue.add(request);

            }
        });

    }

    @Override
    public int getItemCount() {
        return newsFeedArrayList.size();
    }

    static class ViewholderNewsFeed extends RecyclerView.ViewHolder{

        TextView urll, title, description, up, comment_count,shared_by,timeAndDate;
        ImageView up_view, comment,down_view;
        CircleImageView avatar;


        public ViewholderNewsFeed(View itemView){
            super(itemView);
            urll = (TextView) itemView.findViewById(R.id.url);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            up = (TextView) itemView.findViewById(R.id.up_count);
            up_view = (ImageView) itemView.findViewById(R.id.up);
            comment = (ImageView) itemView.findViewById(R.id.comment);
            down_view = (ImageView) itemView.findViewById(R.id.down);
            comment_count = (TextView) itemView.findViewById(R.id.comment_count);
            shared_by = (TextView) itemView.findViewById(R.id.shared_by);
            timeAndDate = (TextView)itemView.findViewById(R.id.timeAndDate);
            avatar = (CircleImageView)itemView.findViewById(R.id.avatar);
        }
    }


    void doNetworkThiny(final ViewholderNewsFeed holder,String UP_DOWN_URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UP_DOWN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                holder.up.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_LID,"1");
                params.put(KEY_PID, "1");

                return params;
            }

        };
        requestQueue.add(stringRequest);
    }

}
