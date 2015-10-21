package edu.sjsu.cmpe277.lab2.mytube.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.youtube.player.YouTubeIntents;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//adapter used to populate list view
class FavAdapter extends ArrayAdapter<VideoItem> {

    private Context mContext;
    YouTubeService youTubeService = new YouTubeService();
    Handler handler;

    //private ArrayList<String> posList = new ArrayList<String>();

    public FavAdapter(Context context, int resources, List<VideoItem> videos) {
        super(context, resources, videos);
        mContext = context;
    }
//
//    public ArrayList<String> getPostList(){
//        return posList;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final VideoItem video = getItem(position);

        handler = new Handler();
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fav_list_view, parent, false);
        }

        ImageView ved = (ImageView) convertView.findViewById(R.id.thumbnail_fv);
        ved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(mContext, video.getVideoId(), true, false);
                mContext.startActivity(intent);
            }
        });

        //set star check box listener
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.check_button_fv);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    FavFragment.posList.add(video.getFavorPlaylistItemId());
                    /*new Thread() {
                        @Override
                        public void run() {
                            youTubeService.favorListItemDelete(video.getFavorPlaylistItemId());
                        }
                    }.start();*/
                }else{
                    FavFragment.posList.remove(video.getFavorPlaylistItemId());
                }

            }
        });

        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.title_fv);
        //TextView numOfViews = (TextView) convertView.findViewById(R.id.num_of_views);
        TextView publishDate = (TextView) convertView.findViewById(R.id.publish_date_fv);
        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail_fv);

        // Populate the data into the template view using the data object
        title.setText(video.getTitle());
        //numOfViews.setText("Views: " + Integer.toString(video.getNumOfViews()));
        publishDate.setText("Publish Date: " + video.getPublishDate());
        new DownloadImageTask(thumbnail).execute(video.getThumbnailURL());
        // Return the completed view to render on screen
        return convertView;
    }

    //class used to download image for thumbnail
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            int width = 100;
            int height = 100;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return Bitmap.createScaledBitmap(mIcon11, width, height, true);
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }


    }
}
