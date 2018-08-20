package com.example.android.bakingapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.bakingapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ButtonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ButtonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ButtonFragment extends Fragment {

    // Get the step change listener
    private onStepIdChange mCallback;

    // Tag for logging
    private static final String TAG = "ButtonFragment";

    // IDs for the data pieces of the fragment used to query
    private static final String NUM_STEPS = "num_steps";
    private static final String STEP_ID = "step_id";

    // Member UI variables
    private View rootView;
    private Button mPrevButton;
    private Button mNextButton;

    // Member variables for button logic
    private int mStepId;
    private int mNumSteps;

    public ButtonFragment() {
        // Required empty public constructor
    }

    // Helper that creates the fragment
    public static ButtonFragment newInstance(int stepId, int numSteps) {
        ButtonFragment fragment = new ButtonFragment();
        Bundle args = new Bundle();
        args.putInt(STEP_ID, stepId);
        args.putInt(NUM_STEPS, numSteps);
        fragment.setArguments(args);
        return fragment;
    }

    // Get the number of steeps and current ID for the visibility and increment logic
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStepId = getArguments().getInt(STEP_ID);
            mNumSteps = getArguments().getInt(NUM_STEPS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the rootView
        rootView = inflater.inflate(R.layout.fragment_button, container, false);

        // Inflate the UI
        inflateUIButtons();

        // Return the rootView
        return rootView;
    }

    // Execute the Button logic in onStart
    @Override
    public void onStart() {
        super.onStart();
        buttonVisibilityLogic();
    }

    // Create a callback that will be used by the main StepDetailActivity to receive changes to give
    // To the StepDetailFragment
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onStepIdChange) {
            mCallback = (onStepIdChange) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    // Detach the callback when the fragment is detached from the step detail activity
    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface onStepIdChange {
        void onStepIdChange(int incrementedStepId);
    }

    // Button UI Logic
    private void inflateUIButtons() {

        // Instantiate/Initialize the button variable
        mPrevButton = (Button) rootView.findViewById(R.id.prev_step_button);
        mNextButton = (Button) rootView.findViewById(R.id.next_step_button);

        // Set the click listener for the next step
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepId = mStepId + 1;
                // Call the visibility logic again
                buttonVisibilityLogic();
                mCallback.onStepIdChange(mStepId);
            }
        });

        // Set the click listener for the next step
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepId = mStepId - 1;
                // Call the visibility logic again
                buttonVisibilityLogic();
                mCallback.onStepIdChange(mStepId);
            }
        });
    }

    // Logic to show or not show the buttons
    private void buttonVisibilityLogic() {
        // If it isn't the first or maximum step
        if (mStepId != (mNumSteps - 1) && mStepId != 0) {
            mPrevButton.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.VISIBLE);
            // If it is first step
        } else if (mStepId == 0) {
            mPrevButton.setVisibility(View.GONE);
            mNextButton.setVisibility(View.VISIBLE);
            // Logic for the last step
        } else {
            mPrevButton.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.GONE);
        }
    }
}
