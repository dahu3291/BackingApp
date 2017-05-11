package com.ajibadedah.backingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajibadedah.backingapp.model.Recipe;
import com.ajibadedah.backingapp.model.Step;
import com.ajibadedah.backingapp.utility.RecipeJsonUtils;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeStepListFragment.OnStepClickedListener} interface
 * to handle interaction events.
 * Use the {@link RecipeStepListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeStepListFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private String mParam1;
    private Boolean mIsTwoPane;
    private Boolean mIsMaster;

    private OnStepClickedListener mListener;

    private Recipe recipe;
    private ArrayList<Object> arrayList;
    private ArrayList<Object> ingredientList;

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;
    private RecyclerView ingredientRecyclerView;
    private RecipeAdapter ingredientAdapter;

    public RecipeStepListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param json Parameter 1.
     * @return A new instance of fragment RecipeStepListFragment.
     */
    public static RecipeStepListFragment newInstance(String json, boolean isTwoPane, boolean isMaster) {
        RecipeStepListFragment fragment = new RecipeStepListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, json);
        args.putBoolean(ARG_PARAM2, isTwoPane);
        args.putBoolean(ARG_PARAM3, isMaster);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mIsTwoPane = getArguments().getBoolean(ARG_PARAM2);
            mIsMaster = getArguments().getBoolean(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        recipe = RecipeJsonUtils.getGson().fromJson(mParam1, Recipe.class);
        ArrayList<Step> steps = recipe.getSteps();
        arrayList = new ArrayList<>();
        arrayList.addAll(steps);
        arrayList.add(0, "Ingredients");
        recipeAdapter = new RecipeAdapter(arrayList, this);

        if (mIsTwoPane){
            if (mIsMaster){

                recipeRecyclerView = (RecyclerView) view.findViewById(R.id.recipe_list_recycler);
                recipeRecyclerView.setAdapter(recipeAdapter);
                recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

            } else {

                ingredientRecyclerView = (RecyclerView) view.findViewById(R.id.ingredient_list_recycler);
                ingredientList = new ArrayList<>();
                ingredientList.addAll(recipe.getIngredients());
                ingredientAdapter = new RecipeAdapter(ingredientList);
                ingredientRecyclerView.setAdapter(ingredientAdapter);
                ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                ingredientRecyclerView.setVisibility(View.VISIBLE);
                recipeRecyclerView = (RecyclerView) view.findViewById(R.id.recipe_list_recycler);
                recipeRecyclerView.setVisibility(View.GONE);
            }
        } else {
            ingredientRecyclerView = (RecyclerView) view.findViewById(R.id.ingredient_list_recycler);
            recipeRecyclerView = (RecyclerView) view.findViewById(R.id.recipe_list_recycler);
            recipeRecyclerView.setAdapter(recipeAdapter);
            recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepClickedListener) {
            mListener = (OnStepClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRecipeClicked(int position) {

        if (mIsTwoPane){
            if (mIsMaster){
                //get Step position - 1 because of Ingredient Offset
                if (position == 0) {
                    String jsonRecipe = RecipeJsonUtils.getGson().toJson(recipe);
                    mListener.onStepClicked(jsonRecipe, position - 1);
                } else {
                    String jsonSteps = RecipeJsonUtils.getGson().toJson(recipe.getSteps());
                    mListener.onStepClicked(jsonSteps, position - 1);
                }
            }
        } else {

            if (position == 0){
                ingredientList = new ArrayList<>();
                ingredientList.addAll(recipe.getIngredients());
                ingredientAdapter = new RecipeAdapter(ingredientList);
                ingredientRecyclerView.setAdapter(ingredientAdapter);
                ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                ingredientRecyclerView.setVisibility(View.VISIBLE);
                recipeRecyclerView.setVisibility(View.GONE);
            } else {
                //get Step position - 1 because of Ingredient Offset
                String jsonSteps = RecipeJsonUtils.getGson().toJson(recipe.getSteps());
                mListener.onStepClicked(jsonSteps, position - 1);
            }
        }

    }

    public boolean changeView(){
        boolean shouldSuperOnBack;
        if (ingredientRecyclerView.getVisibility() == View.VISIBLE){
            ingredientRecyclerView.setVisibility(View.GONE);
            recipeRecyclerView.setVisibility(View.VISIBLE);
            shouldSuperOnBack = false;
        } else {
            shouldSuperOnBack = true;
        }
        return shouldSuperOnBack;
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
    interface OnStepClickedListener {
        void onStepClicked(String json, int position);
    }
}
