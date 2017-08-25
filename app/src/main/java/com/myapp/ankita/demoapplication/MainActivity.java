package com.myapp.ankita.demoapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    JSONObject jsonobject;
    JSONArray jsonarray;
    ListView listview;
    ProgressDialog mProgressDialog;
    SearchView searchView;
    CustomListAdapter adapter;
    static String artworkUrl60 = "artworkUrl60";
    static String trackName = "trackName";
    static String artistName = "artistName";
    static String primaryGenreName = "primaryGenreName";
    static String trackTimeMillis = "trackTimeMillis";
    static String trackPrice = "trackPrice";
    static String previewUrl = "previewUrl";

    ArrayList<JSONData> songsList = new ArrayList<JSONData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        new GetSongs().execute();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }

    private class GetSongs extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("Loading…");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            jsonobject = JSONfunctions.getJSONfromURL("https://itunes.apple.com/search?term=adele");
            try {
                    jsonarray = jsonobject.getJSONArray("results");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        jsonobject = jsonarray.getJSONObject(i);
                        artworkUrl60 = jsonobject.getString("artworkUrl60");
                        trackName = jsonobject.getString("trackName");
                        artistName = jsonobject.getString("artistName");
                        primaryGenreName = jsonobject.getString("primaryGenreName");
                        trackTimeMillis = jsonobject.getString("trackTimeMillis");
                        trackPrice = jsonobject.getString("trackPrice");
                        previewUrl = jsonobject.getString("previewUrl");

                        JSONData result = new JSONData(artworkUrl60,trackName,artistName,primaryGenreName,
                                trackTimeMillis,trackPrice,previewUrl);
                        result.setArtworkUrl60(jsonobject.getString("artworkUrl60"));
                        result.setTrackName(jsonobject.getString("trackName"));
                        result.setArtistName(jsonobject.getString("artistName"));
                        result.setPrimaryGenreName(jsonobject.getString("primaryGenreName"));
                        result.setTrackTimeMillis(jsonobject.getString("trackTimeMillis"));
                        result.setTrackPrice(jsonobject.getString("trackPrice"));
                        result.setPreviewUrl(jsonobject.getString("previewUrl"));

                        songsList.add(result);
                    }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            listview = (ListView) findViewById(R.id.listView);
            adapter = new CustomListAdapter(MainActivity.this, songsList);
            listview.setAdapter(adapter);
            listview.setTextFilterEnabled(true);
            mProgressDialog.dismiss();
        }
    }
}
