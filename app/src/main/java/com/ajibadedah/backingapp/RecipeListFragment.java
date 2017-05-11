package com.ajibadedah.backingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ajibadedah.backingapp.IdlingResource.SimpleIdlingResource;
import com.ajibadedah.backingapp.model.Recipe;
import com.ajibadedah.backingapp.sync.RecipeManager;
import com.ajibadedah.backingapp.utility.RecipeJsonUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeListFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private boolean mIsTwoPane;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;

    private ArrayList<Object> arrayList;

    private TextView emptyTextView;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    public RecipeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecipeListFragment.
     */
    public static RecipeListFragment newInstance(boolean isTwoPane) {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, isTwoPane);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIsTwoPane = getArguments().getBoolean(ARG_PARAM1);
        }
        // Get the IdlingResource instance
        getIdlingResource();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        emptyTextView = (TextView) view.findViewById(R.id.empty_recipe);
        emptyTextView.setVisibility(View.VISIBLE);
        arrayList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(arrayList, this);
        recipeRecyclerView = (RecyclerView) view.findViewById(R.id.recipe_list_recycler);
        recipeRecyclerView.setAdapter(recipeAdapter);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int columns = (int)((displayMetrics.widthPixels / displayMetrics.density) / 300);
        recipeRecyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), columns, GridLayoutManager.VERTICAL, false));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecipeManager.getInstance(getContext()).getRecipes(new RecipeManager.RecipeConsumer() {
            @Override
            public void onRecipesAvailable(List<Recipe> recipes) {
                arrayList.addAll(recipes);
                recipeAdapter.notifyDataSetChanged();
                if (arrayList.size() > 0) emptyTextView.setVisibility(View.GONE);
            }
        }, mIdlingResource);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("temp", arrayList.size());

    }

    @Override
    public void onResume() {
        super.onResume();
        //resize recyclers gridview depending on orientation
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        int columns = (int)((displayMetrics.widthPixels / displayMetrics.density) / 300);
//        recipeRecyclerView.setLayoutManager(
//                new GridLayoutManager(getContext(), columns, GridLayoutManager.VERTICAL, false));

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        String json = RecipeJsonUtils.getGson().toJson(arrayList.get(position));
        mListener.onRecipeClicked(json);
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
    interface OnFragmentInteractionListener {
        void onRecipeClicked(String recipeJson);
    }
}
