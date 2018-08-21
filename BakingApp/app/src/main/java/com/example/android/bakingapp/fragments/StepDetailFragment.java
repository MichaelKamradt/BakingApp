package com.example.android.bakingapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.database.AppExecutors;
import com.example.android.bakingapp.database.RecipeStepsDatabase;
import com.example.android.bakingapp.database.StepsDBModel;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.MpegAudioHeader;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link 'StepDetailFragment.OnFragmentInteractionListener'} interface
 * to handle interaction events.
 * Use the {@link StepDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailFragment extends Fragment {

    // Tag for logging
    private static final String TAG = "StepDetailFragment";

    // IDs for the data pieces of the fragment used to query
    private static final String STEP_ID = "step_id";
    private static final String RECIPE_ID = "recipe_id";
    private static final String PREVIOUS_PLAY_TIME_ID = "previous_play_time";

    // Instantiate the member variables
    private int mStepId;
    private int mRecipeId;
    private String mStepDescription;
    private String mVideoUri;
    private TextView mStepInstructionsTextView;
    private SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private View rootView;
    private RecipeStepsDatabase mDB;
    private Context mContext;
    private long mPlayTimeInMillis;

    // Required empty public constructor
    public StepDetailFragment() {
        // Nothing to see here
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipeId RecipeId for the query.
     * @param stepId StepId for the query.
     * @return A new instance of fragment StepDetailFragment.
     */
    public static StepDetailFragment newInstance(int recipeId, int stepId) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_ID, recipeId);
        args.putInt(STEP_ID, stepId);
        fragment.setArguments(args);
        return fragment;
    }

    // Get the activity's context
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    // Initialize the member variables
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instantiate the DB
        mDB = RecipeStepsDatabase.getsInstance(getActivity());
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(RECIPE_ID);
            mStepId = getArguments().getInt(STEP_ID);
            Log.d(TAG, "RECIPE ID: " + "PLAY TIME: " + mPlayTimeInMillis);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Fetch variables based on the save instance state
        if (savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt(RECIPE_ID);
            mStepId = savedInstanceState.getInt(STEP_ID);
            mPlayTimeInMillis = savedInstanceState.getLong(PREVIOUS_PLAY_TIME_ID);
            Log.d(TAG, "SAVED INSTANT STATE " + "PLAY TIME: " + mPlayTimeInMillis);
        }

        // Inflate the Step Detail fragment layout
        rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Make the DB call and inflate the UI
        setUpSingleStepViewModel();
    }

    private void initializeExoPlayer(String mediaUri) {
        // Make sure the Uri is there
        if (mediaUri != null && mediaUri.length() > 0) {
            // Get the UI element
            mExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.fragment_step_video_view);
            // Create the necessary track selector and load controller to initialize the ExoPlayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            // Initialize the ExoPlayer
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
            mExoPlayerView.setPlayer(mExoPlayer);
            // Prepare the Media source
            // User agent, because it needs it
            String userAgent = Util.getUserAgent(mContext, "BakingApp");
            // Create the media source and add the necessary dependencies. DataSource and Extractors are blank for this implementation
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mediaUri),
                    new DefaultDataSourceFactory(mContext, userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            // Prepare and play when ready
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            // Set logic for picking up where the player left off
            mExoPlayer.seekTo(mPlayTimeInMillis);
        } else {
            // Hide the video if there isn't one
            mExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.fragment_step_video_view);
            mExoPlayerView.setVisibility(View.INVISIBLE);
            // Set the ExoPlayer to null
            mExoPlayer = null;
        }
    }

    // Set up the ViewModel for the steps
    private void setUpSingleStepViewModel() {

        // Create an instance of a runnable that will query for the step on the IO thread
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Query for the step. To be run on every fragment load
                // Not that I am not using LiveData as the data is not expected to change
                final StepsDBModel recipeStep = mDB.stepsDao().getSingleStep(mRecipeId, mStepId);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Populate the UI on the main thread
                        updateUIViews(recipeStep.description, recipeStep.videoURL);
                    }
                });
            }
        });
    }

    // Update the views
    public void updateUIViews(String description, String videoUri) {

        // Get the UI elements
        mStepInstructionsTextView = (TextView) rootView.findViewById(R.id.step_instructions_text_view);

        // Set the text
        mStepDescription = description;

        // Initialize the ExoPlayer
        mVideoUri = videoUri;

        // Set the text description
        mStepInstructionsTextView.setText(mStepDescription);

        // Initialize the player
        initializeExoPlayer(mVideoUri);
    }

    // Function to release/stop the ExoPlayer
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayTimeInMillis = mExoPlayer.getCurrentPosition();
            Log.d(TAG, "RELEASE PLAYER PLAY TIME: " + mPlayTimeInMillis);
            mExoPlayer.release();
            mExoPlayer.stop();
            mExoPlayer = null;
        }
    }

    // Set the Player to stop in pause / stop / destroy
    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    // Create the onSaveInstanceState logic to retreive the existing variables
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_ID, mRecipeId);
        outState.putInt(STEP_ID, mStepId);
        outState.putLong(PREVIOUS_PLAY_TIME_ID, mPlayTimeInMillis);
    }
}
