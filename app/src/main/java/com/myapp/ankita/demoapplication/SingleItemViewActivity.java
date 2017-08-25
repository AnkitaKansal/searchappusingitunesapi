package com.myapp.ankita.demoapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SingleItemViewActivity extends AppCompatActivity implements OnClickListener, OnTouchListener,
        OnCompletionListener, OnBufferingUpdateListener {

    String artworkUrl60;
    String trackName;
    String artistName;
    String primaryGenreName;
    String trackTimeMillis;
    String trackPrice;
    String previewUrl;
    ProgressDialog mProgressDialog;
    private ImageButton listImageButton;
    private SeekBar listSeekBar;
    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds;
    private int currentPosition = 0;
    private final Handler handler = new Handler();
    private Bitmap bmImg = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item_view);

        new loadSingleView().execute();
        initView();
    }

    public class loadSingleView extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SingleItemViewActivity.this);
            mProgressDialog.setMessage("Loading…");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Intent i = getIntent();
                trackName = i.getStringExtra("trackName");
                artistName = i.getStringExtra("artistName");
                primaryGenreName = i.getStringExtra("primaryGenreName");
                trackTimeMillis = i.getStringExtra("trackTimeMillis");
                trackPrice = i.getStringExtra("trackPrice");
                artworkUrl60 = i.getStringExtra("artworkUrl60");
                previewUrl = i.getStringExtra("previewUrl");

                URL url = new URL(artworkUrl60);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bmImg = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String args) {
            TextView txttrackName = (TextView) findViewById(R.id.listTrackName);
            TextView txtartistName = (TextView) findViewById(R.id.listArtistName);
            TextView txtprimaryGenreName = (TextView) findViewById(R.id.listGenre);
            TextView txttrackTimeMillis = (TextView) findViewById(R.id.listDuration);
            TextView txttrackPrice = (TextView) findViewById(R.id.listPrice);
            TextView txtpreviewUrl = (TextView) findViewById(R.id.listPreviewUrl);
            ImageView imgartworkUrl60 = (ImageView) findViewById(R.id.listArtworkImage);

            txttrackName.setText(trackName);
            txtartistName.setText(artistName);
            txtprimaryGenreName.setText(primaryGenreName);
            txttrackTimeMillis.setText(trackTimeMillis);
            txttrackPrice.setText(trackPrice);
            txtpreviewUrl.setText(previewUrl);
            imgartworkUrl60.setImageBitmap(bmImg);

            mProgressDialog.dismiss();
        }
    }

    private void initView() {
        listImageButton = (ImageButton) findViewById(R.id.listImageButton);
        listImageButton.setOnClickListener(this);

        listSeekBar = (SeekBar) findViewById(R.id.listSeekBar);
        listSeekBar.setMax(99);
        listSeekBar.setOnTouchListener(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    private void primarySeekBarProgressUpdater() {
        currentPosition = mediaPlayer.getCurrentPosition();
        listSeekBar.setProgress((int) (((float) currentPosition / mediaFileLengthInMilliseconds) * 100));
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.listImageButton) {
            try {
                mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(previewUrl));
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            listImageButton.setImageResource(R.drawable.button_play);
            mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                listImageButton.setImageResource(R.drawable.button_pause);
            } else {
                mediaPlayer.pause();
                listImageButton.setImageResource(R.drawable.button_play);
            }
            primarySeekBarProgressUpdater();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.listSeekBar) {
            if (mediaPlayer.isPlaying()) {
                SeekBar sb = (SeekBar) v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        listImageButton.setImageResource(R.drawable.button_play);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        listSeekBar.setSecondaryProgress(percent);
    }

    @Override
    protected void onStop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        super.onStop();
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        }*/
}
