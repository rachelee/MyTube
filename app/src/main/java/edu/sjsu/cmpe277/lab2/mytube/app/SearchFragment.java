package edu.sjsu.cmpe277.lab2.mytube.app;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.os.Handler;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.UserRecoverableAuthException;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.cmpe277.lab2.mytube.app.content.FragmentContent;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class SearchFragment extends ListFragment {

    private static final String TAG = "SearchTab";

    private OnFragmentInteractionListener mListener;

    private EditText searchInput;
    private YouTubeService youTubeService = new YouTubeService();
    private Handler handler = new Handler();
    private List<VideoItem> searchList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the adapter to convert the array to views
        // replace FragmentContent.ITEMS with arraylist from youtube interface
        //SearchAdapter adapter = new SearchAdapter(getActivity(), R.layout.search_list_view, searchVideos);

        // Attach the adapter to a ListView
        //setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchInput = (EditText) view.findViewById(R.id.search_box);
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    new Thread() {
                        @Override
                        public void run() {
                            searchList = youTubeService.search(searchInput.getText().toString());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    SearchAdapter adapter = new SearchAdapter(getActivity(), R.layout.search_list_view, searchList);
                                    setListAdapter(adapter);
                                }
                            });
                        }
                    }.start();
                    return true;
                }
                return false;
            }
        });
        return view;
    }

//    public void updateVideo(List<VideoItem> searchList) {
//
////        SearchAdapter adapter = new SearchAdapter(getActivity(), R.layout.search_list_view, (ArrayList<VideoItem>) searchList);
//
//
//    }


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

    //@Override
    //public void onListItemClick(ListView l, View v, int position, long id) {
    //    super.onListItemClick(l, v, position, id);
    //
    //    if (null != mListener) {
    //        // Notify the active callbacks interface (the activity, if the
    //        // fragment is attached to one) that an item has been selected.
    //        mListener.onFragmentInteraction(TAG, FragmentContent.ITEMS.get(position).getVideoId());
    //    }
    //}




}
