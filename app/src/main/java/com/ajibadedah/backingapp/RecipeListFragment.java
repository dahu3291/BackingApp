package com.ajibadedah.backingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajibadedah.backingapp.model.Ingredient;
import com.ajibadedah.backingapp.model.Recipe;
import com.ajibadedah.backingapp.model.Step;
import com.ajibadedah.backingapp.sync.RecipeManager;
import com.ajibadedah.backingapp.utility.NetworkUtils;
import com.ajibadedah.backingapp.utility.RecipeJsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;

    private ArrayList<Object> arrayList;

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Recipe.class, new Recipe.RecipeTypeAdapter())
            .registerTypeAdapter(Ingredient.class, new Ingredient.IngredientTypeAdapter())
            .registerTypeAdapter(Step.class, new Step.StepTypeAdapter())
            .create();

    public RecipeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecipeListFragment.
     */
    public static RecipeListFragment newInstance() {
        RecipeListFragment fragment = new RecipeListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);


//        if (savedInstanceState == null){
//            arrayList = new ArrayList<>();
//            NetworkUtils.getRecipeApi().getRecipes().enqueue(new Callback<List<Recipe>>() {
//                @Override
//                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
//                    arrayList.addAll(response.body());
//                    recipeAdapter.notifyDataSetChanged();
//
//
//                    String json;
//                    for (int i = 0; i < arrayList.size(); i++){
//                        json = gson.toJson(arrayList.get(i));
//                        setRecipeJson(getContext(), json, String.valueOf(i));
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<List<Recipe>> call, Throwable t) {
//
//                }
//            });
//        } else {
//            int size = savedInstanceState.getInt("temp");
//            String json;
//            for (int i = 0; i < size; i++){
//                json = getRecipeJson(getContext(), String.valueOf(i));
//                arrayList.add(gson.fromJson(json, Recipe.class));
//            }
//        }

        arrayList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(arrayList, this);
        recipeRecyclerView = (RecyclerView) view.findViewById(R.id.recipe_list_recycler);
        recipeRecyclerView.setAdapter(recipeAdapter);

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
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("temp", arrayList.size());

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //resize recyclers gridview depending on orientation
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        int thumbnailWidth = Integer.parseInt(getString(R.string.thumbnail_list_width));
//        int columns = (int)((displayMetrics.widthPixels / displayMetrics.density) / thumbnailWidth);

        recipeRecyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));

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
        mListener.onFragmentInteraction(json);
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
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String recipeJson);
    }
}
