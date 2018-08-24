package com.example.android.bakingapp.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.StepDetailActivity;
import com.example.android.bakingapp.adapters.IngredientAdapter;
import com.example.android.bakingapp.adapters.StepAdapter;
import com.example.android.bakingapp.database.IngredientsDBModel;
import com.example.android.bakingapp.database.IngredientsViewModel;
import com.example.android.bakingapp.database.RecipeStepsDatabase;
import com.example.android.bakingapp.database.StepsDBModel;
import com.example.android.bakingapp.database.StepsViewModel;
import com.example.android.bakingapp.database.StepsViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link 'RecipeDetailFragment.OnFragmentInteractionListener'} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailFragment extends Fragment implements StepAdapter.stepClickHandler {

    // TAG for logging
    private static final String TAG = RecipeDetailFragment.class.getSimpleName();

    // Key for the recipe Id
    private static final String RECIPE_ID = "recipe_id";
    private static final String NUM_STEPS = "num_steps";
    private static final String STEP_ID = "step_id";

    // Recipe ID to make the DB call for the ingredients and the steps
    private int mRecipeId;
    private int mNumSteps;

    // Member variable for the click listener on the recipe items
    private OnMasterDetailStepItemClick mCallback;

    // Member variable for the root view
    private View rootView;

    // Get UI member variables
    private RecyclerView mRecyclerView;
    private StepAdapter mStepAdapter;
    private IngredientAdapter mIngredientsAdapter;
    private GridView mIngredientsGridView;
    private TextView mRecipeTitleTextView;

    // Data member variables
    private IngredientsDBModel[] mIngredients;
    private StepsDBModel[] mSteps;
    private String mRecipeTitle;

    // Required empty constructor
    public RecipeDetailFragment() {
        // Nothing to see here
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipeId Recipe Id needed for getting the ingredients and recipe steps.
     * @return A new instance of fragment RecipeDetailFragment.
     */
    // Setting the recipe Id and initializing the fragment
    public static RecipeDetailFragment newInstance(int recipeId) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    // Getting of the recipe Id
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (savedInstanceState != null) {
                // Get the recipe Id after a rotation
                mRecipeId = savedInstanceState.getInt(RECIPE_ID);
            } else {
                // Define the variables
                mRecipeId = getArguments().getInt(RECIPE_ID);
            }
            // Set them using the ViewModels
            setUpIngredientsViewModel();
            setUpStepViewModel();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set the rootView
        rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        // Inflate the layout for this fragment
        return rootView;
    }

    // Handle for what happens when a recipe item is pressed
    public void onRecipeItemPressed(int stepId) {
        if (mCallback != null) {
            mCallback.onMasterDetailStepItemClick(stepId);
        }
    }

    // Define the click listener when the fragment is attached to the activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMasterDetailStepItemClick) {
            mCallback = (OnMasterDetailStepItemClick) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    // Clear the listener when the fragment is detached from the activity
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
     */
    public interface OnMasterDetailStepItemClick {
        // TODO: Update argument type and name
        void onMasterDetailStepItemClick(int stepId);
    }

    // Data and UI functions
    // Set up the ViewModel for the ingredients
    private void setUpIngredientsViewModel() {
        // Call the ViewModel class (IngredientsViewModel)
        IngredientsViewModel ingredientsViewModel = ViewModelProviders.of(getActivity()).get(IngredientsViewModel.class);

        // Load the ingredients from the database
        // Using LiveData to keep the data from not querying a second time when the activity goes through lifecycle events
        LiveData<IngredientsDBModel[]> recipeIngredients = ingredientsViewModel.getmIngredients(mRecipeId);

        recipeIngredients.observe(this, new Observer<IngredientsDBModel[]>() {
            @Override
            public void onChanged(@Nullable IngredientsDBModel[] ingredientsDBModels) {
                // Update the ingredients model
                mIngredients = ingredientsDBModels;
                // Ingredients UI components
                inflateIngredientsUIComponent();
            }
        });
    }

    // Set up the ViewModel for the steps
    private void setUpStepViewModel() {
        // Get the Factory
        StepsViewModelFactory factory = new StepsViewModelFactory(RecipeStepsDatabase.getsInstance(getActivity()), mRecipeId);
        // Call the ViewModel class (IngredientsViewModel)
        StepsViewModel stepViewModel = ViewModelProviders.of(getActivity(), factory).get(StepsViewModel.class);

        // Load the ingredients from the database
        // Using LiveData to keep the data from not querying a second time when the activity goes through lifecycle events
        LiveData<StepsDBModel[]> recipeSteps = stepViewModel.getmSteps();

        recipeSteps.observe(this, new Observer<StepsDBModel[]>() {
            @Override
            public void onChanged(@Nullable StepsDBModel[] stepsDBModels) {

                // Get the length and set the steps
                mSteps = stepsDBModels;

                // Get the recipe name from the first step
                mRecipeTitle = stepsDBModels[0].recipeName;

                // Set the number of steps
                mNumSteps = stepsDBModels.length;

                // Steps UI components
                inflateStepsUIComponent();
            }
        });
    }

    // Handler for the ingredients view
    private void inflateIngredientsUIComponent() {
        // Get a reference to the GridView to show the ingredients
        mIngredientsGridView = (GridView) rootView.findViewById(R.id.fragment_ingredients_grid_view);

        // Get the ingredients adapter
        mIngredientsAdapter = new IngredientAdapter(getActivity(), R.layout.ingredient_grid_view_item, mIngredients);

        // Set the Adapter of the GridView
        mIngredientsGridView.setAdapter(mIngredientsAdapter);
    }

    // Handler for the steps view
    private void inflateStepsUIComponent() {
        // Inflate the Title TextView and set the title
        mRecipeTitleTextView = (TextView) rootView.findViewById(R.id.recipe_title);
        mRecipeTitleTextView.setText(mRecipeTitle);

        // Define the recyclerview for the steps
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_steps_recycler_view);

        // Set the Adapter of the RecyclerView
        mStepAdapter = new StepAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mStepAdapter);

        // Set the layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // Get the length and set the steps
        mStepAdapter.setStepData(mSteps);
    }



    // Override onStepClicked to handle the click
    @Override
    public void onStepItemClick(int clickedStepId) {

        // If we're in the Master Detail View, don't start the new activity
        if (getActivity().findViewById(R.id.recipe_master_detail_linear_layout) != null) {
            // Otherwise allow onMasterDetailStepItemClick to control it
            mCallback.onMasterDetailStepItemClick(clickedStepId);
        } else {
            // Create the Intent to handle the clickedRecipe
            Intent clickedStepIntent = new Intent(getActivity(), StepDetailActivity.class);

            // Add the clicked recipe parcel to the intent
            clickedStepIntent.putExtra(RECIPE_ID, mRecipeId);
            clickedStepIntent.putExtra(STEP_ID, clickedStepId);
            clickedStepIntent.putExtra(NUM_STEPS, mNumSteps);

            // Start the activity with the passed values
            startActivity(clickedStepIntent);
        }
    }

    // Fragment version of onSavedInstantSate
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_ID, mRecipeId);
    }
}
