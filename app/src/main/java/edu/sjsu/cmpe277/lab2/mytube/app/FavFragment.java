package edu.sjsu.cmpe277.lab2.mytube.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.ListFragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p>
 * <p>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class FavFragment extends ListFragment {
    public static ArrayList<String> posList = new ArrayList<String>();


    private static final String TAG = "FavTab";
    private FavAdapter adapter;

    private OnFragmentInteractionListener mListener;

    private YouTubeService youTubeService = new YouTubeService();
    private Handler handler = new Handler();
    private List<VideoItem> favorList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Create the adapter to convert the array to views
        //replace FragmentContent.ITEMS with arraylist from youtube interface
        //SearchAdapter adapter = new SearchAdapter(getActivity(), FragmentContent.ITEMS);

        // Attach the adapter to a ListView
        //setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fav, container, false);

        return view;
    }

    public void refreshListview() {
        new Thread() {
            @Override
            public void run() {
                favorList = youTubeService.getFavorListVideos();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new FavAdapter(getActivity(), R.layout.fav_list_view, favorList);
                        setListAdapter(adapter);
                        //adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


    }


    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.delete:

                if(posList!=null){


                    new Thread() {
                        @Override
                        public void run() {
                            for(String s: posList){
                                youTubeService.favorListItemDelete(s);
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    posList.clear();
                                    refreshListview();
                                }
                            });
                        }
                    }.start();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
