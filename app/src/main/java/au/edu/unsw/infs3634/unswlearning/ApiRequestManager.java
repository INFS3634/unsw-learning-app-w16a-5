package au.edu.unsw.infs3634.unswlearning;

import android.content.Context;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Api.InstructionsReponse;
import au.edu.unsw.infs3634.unswlearning.Api.RecipeApiResponse;
import au.edu.unsw.infs3634.unswlearning.Api.RecipeInfoResponse;
import au.edu.unsw.infs3634.unswlearning.Api.SimilarRecipeApiResponse;
import au.edu.unsw.infs3634.unswlearning.Listeners.InstructionsListener;
import au.edu.unsw.infs3634.unswlearning.Listeners.RecipeInfoListener;
import au.edu.unsw.infs3634.unswlearning.Listeners.RecipeResponseListener;
import au.edu.unsw.infs3634.unswlearning.Listeners.SimilarRecipeListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ApiRequestManager {

    Context context;
    //call upon instance of the Retrofit REST client for our API interfaces
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //creating constructor class for request manager
    //context is the current state of the application or object
    public ApiRequestManager(Context context) {
        this.context = context;
    }

    //method to be called in activities to retrieve recipe information from Spoonacular
    public void getRecipes(RecipeResponseListener listener, List<String> tags) {
        //instantiating instance of the interface
        CallRecipes callRecipes = retrofit.create(CallRecipes.class);
        //call from response class RecipeApiResponse
        //parsing API key, number of recipes to fetch and any items searched in the search bar as specified by API doco
        Call<RecipeApiResponse> call = callRecipes.callRecipe(context.getString(R.string.api_key), "50", tags);
        //using call Retrofit function to asynchronously send the request and notify call back of response
        call.enqueue(new Callback<RecipeApiResponse>() {
            @Override
            //interface from RecipeResponseListener that gets the JSON objects stated in the RecipeApiResponse class
            public void onResponse(Call<RecipeApiResponse> call, Response<RecipeApiResponse> response) {

                if(response.isSuccessful()) {
                    listener.fetched(response.body(), response.message());
                }
                else {
                    listener.error(response.message());
                    return;
                }
            }

            @Override
            public void onFailure(Call<RecipeApiResponse> call, Throwable t) {
                listener.error(t.getMessage());
            }
        });
    }

    //parses interface RecipeInfoListener and id
    //to be called from RecipeInfoActivity
    public void getRecipeInfo(RecipeInfoListener listener, int id) {
        //instantiating instance of the interface
        CallRecipeInfo callRecipeInfo = retrofit.create(CallRecipeInfo.class);
        //parsing API key and id as specified by API doco
        Call<RecipeInfoResponse> call = callRecipeInfo.callRecipeInfo(id, context.getString(R.string.api_key));
        //using call Retrofit function to asynchronously send the request and notify call back of response
        call.enqueue(new Callback<RecipeInfoResponse>() {
            @Override
            //interface from RecipeInfoListener that gets the JSON objects stated in the RecipeInfoResponse class
            public void onResponse(Call<RecipeInfoResponse> call, Response<RecipeInfoResponse> response) {
                if(response.isSuccessful()) {
                    listener.fetched(response.body(), response.message());
                }
                else {
                    listener.error(response.message());
                    return;
                }
            }

            @Override
            public void onFailure(Call<RecipeInfoResponse> call, Throwable t) {
                listener.error(t.getMessage());
            }
        });
    }

    //method to be called to get a list of similar recipes to the recipe selected
    public void getSimilarRecipes(SimilarRecipeListener listener, int id) {
        //instantiating instance of the interface
        CallSimilarRecipes callSimilarRecipes = retrofit.create(CallSimilarRecipes.class);
        //parsing API key, id  and number of similar recipes as specified by API doco
        Call<List<SimilarRecipeApiResponse>> call = callSimilarRecipes.callSimilarRecipe(id, "4", context.getString(R.string.api_key));
        //using call Retrofit function to asynchronously send the request and notify call back of response
        call.enqueue(new Callback<List<SimilarRecipeApiResponse>>() {
            @Override
            //interface from SimilarRecipeListener that gets the JSON objects stated in the SimilarRecipeApiResponse class
            public void onResponse(Call<List<SimilarRecipeApiResponse>> call, Response<List<SimilarRecipeApiResponse>> response) {
                if(response.isSuccessful()) {
                    listener.fetched(response.body(), response.message());
                }
                else {
                    listener.error(response.message());
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<SimilarRecipeApiResponse>> call, Throwable t) {
                listener.error(t.getMessage());
            }
        });
    }

    //method to be called to get instructions for the recipe selected
    public void getInstructions(InstructionsListener listener, int id) {
        //instantiating instance of the interface
        CallInstructions callInstructions = retrofit.create(CallInstructions.class);
        //parsing API key and id of similar recipes as specified by API doco
        Call<List<InstructionsReponse>> call = callInstructions.callInstructions(id, context.getString(R.string.api_key));
        //using call Retrofit function to asynchronously send the request and notify call back of response
        call.enqueue(new Callback<List<InstructionsReponse>>() {
            @Override
            //interface from InstructionsListener that gets the JSON objects stated in the InstructionsResponse class
            public void onResponse(Call<List<InstructionsReponse>> call, Response<List<InstructionsReponse>> response) {
                if(response.isSuccessful()) {
                    listener.fetched(response.body(), response.message());
                }
                else {
                    listener.error(response.message());
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<InstructionsReponse>> call, Throwable t) {
                listener.error(t.getMessage());
            }
        });
    }




    //all API queries and GET methods
    private interface CallRecipes {
        //endpoint of base URL
        @GET("recipes/random")
        //call the API class
        Call<RecipeApiResponse> callRecipe(
                //passing the API key and the parameters as specified by the API doco
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("tags") List<String> tags
        );
    }

    private interface CallRecipeInfo {
        @GET("recipes/{id}/information")
            //call the API class
        Call<RecipeInfoResponse> callRecipeInfo (
                //passing the api key and the parameters as specified by the API doco
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }

    private interface CallSimilarRecipes {
        @GET("recipes/{id}/similar")
        Call<List<SimilarRecipeApiResponse>> callSimilarRecipe(
                //path as this is imbedded in the @GET
                @Path("id") int id,
                @Query("number") String number,
                @Query("apiKey") String apiKey
        );
    }

    private interface CallInstructions {
        @GET("recipes/{id}/analyzedInstructions")
        Call<List<InstructionsReponse>> callInstructions (
                //passing the api key and the parameters as specified by the API doco
                @Path("id") int id,
                @Query("apiKey") String apiKey

        );
    }
}