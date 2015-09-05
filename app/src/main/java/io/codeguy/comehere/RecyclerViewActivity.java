package io.codeguy.comehere;

/**
 * Created by KaiHin on 7/8/2015.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.codeguy.comehere.Adapter.MyRecyclerAdapter;
import io.codeguy.comehere.DataObject.pending;

public class RecyclerViewActivity extends ActionBarActivity {
    // JSON Node names
    public static final String ITEM_ID = "id";
    public static final String ITEM_NAME = "item";
    private static String TAG = MainActivity.class.getSimpleName();
    private static String LOG_TAG = "RecyclerViewActivity";
    String url = "http://api.tutorialsbuzz.com/Orders/read_allorder.php";
    ArrayList<HashMap<String, String>> Item_List;
    ProgressDialog PD;
    ListAdapter adapter;
    JSONArray comeHereDB = null;
    ArrayList<pending> data = new ArrayList<>();
    private String urlJsonObj = "http://10.0.2.2/android/readAllpending.php";
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_place);
        Log.v("oscar", "in the RecyclerViewActivity on create view");

//        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        Item_List = new ArrayList<HashMap<String, String>>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerAdapter(this, ReadDataFromDB());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
//        RecyclerView.ItemDecoration itemDecoration =
//                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
//        mRecyclerView.addItemDecoration(itemDecoration);

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerAdapter) mAdapter).setClickListener(new MyRecyclerAdapter.ClickListener() {

            @Override
            public void itemClicked(View view, int position) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

//    private ArrayList<DataObject> getDataSet() {
//        ArrayList results = new ArrayList<DataObject>();
//        for (int index = 0; index < 20; index++) {
//            DataObject obj = new DataObject("Some Primary Text " + index,
//                    "Secondary " + index);
//            results.add(index, obj);
//        }
//        return results;
//    }

    private ArrayList<pending> ReadDataFromDB() {
        Log.v("oscar", "int the ReadDataFromDb");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("oscar", response.toString());
                ArrayList result = new ArrayList<pending>();
                try {
                    comeHereDB = response.getJSONArray("come_here");
                    Log.v("oscar", "before looping");
                    for (int i = 0; i < comeHereDB.length(); i++) {
                        pending currentPendingItem = new pending();
                        JSONObject currentJsonObject = comeHereDB.getJSONObject(i);
                        String id = currentJsonObject.getString("pending_id");
                        String name = currentJsonObject.getString("pending_name");
                        String appkey = currentJsonObject.getString("app_registered_key");
                        String expired = currentJsonObject.getString("time_expired");

                        Log.v("oscar", "pending name: " + name);
                        currentPendingItem.setName(name);
                        currentPendingItem.setId(id);
                        currentPendingItem.setTimeExpired(expired);
                        currentPendingItem.setInstalledKey(appkey);
                        data.add(i, currentPendingItem);
//                    result.add
                        Log.v("oscar", "the array data is " + data.get(i).toString());
                        Log.v("oscar", "adding...");

                    }

                    MainActivity ma = new MainActivity();
                    ma.data = data;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
        Log.v("oscar", "end of the reading data");
        Log.v("oscar", " the array list is..." + data.toString());
        return this.data;
    }

    //    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener
//    {
//        private GestureDetector gestureDetector;
//        private ClickListener clickListener;
//        public RecyclerTouchListener(Context context,final RecyclerView recyclerView, final ClickListener clickListener)
//        {
//             this.clickListener = clickListener;
//             gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
//                 @Override
//                 public boolean onSingleTapUp(MotionEvent e) {
//                     View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
//                     if(child != null && clickListener != null)
//                     {
//                         clickListener.onLoingClick(child, recyclerView.getChildPosition(child));
//                     }
//                     Log.v("oscar","onSignleTapUpe" + e);
//                     return true;
//                 }
//
//                 @Override
//                 public void onLongPress(MotionEvent e) {
//                     View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
//                     if(child != null && clickListener != null)
//                     {
//                         clickListener.onLoingClick(child, recyclerView.getChildPosition(child));
//                     }
//                     Log.v("oscar","onLongPRess "+e);
//                 }
//             });
//
//        }
//        @Override
//        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//            View child = rv.findChildViewUnder(e.getX(), e.getY());
//            if(child != null && clickListener != null)
//            {
//                clickListener.onLoingClick(child, rv.getChildPosition(child));
//            }
//            return false;
//        }
//
//        @Override
//        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//            Log.v("oscar","onTouchEvent " +e);
//        }
//    }
    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLoingClick(View view, int position);
    }


    /**
     * Created by KaiHin on 7/10/2015.
     */

}