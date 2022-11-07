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
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //creating constructor class for request manager
    public ApiRequestManager(Context context) {
        this.context = context;
    }

    //method to be called in MainActivity to
    public void getRecipes(RecipeResponseListener listener, List<String> tags) {
        //instantiating instance of the interface
        CallRecipes callRecipes = retrofit.create(CallRecipes.class);
        Call<RecipeApiResponse> call = callRecipes.callRecipe(context.getString(R.string.api_key), "50", tags);
        call.enqueue(new Callback<RecipeApiResponse>() {
            @Override
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

    //passes interface RecipeInfoListener and id
    //to be called from RecipeInfoActivity
    public void getRecipeInfo(RecipeInfoListener listener, int id) {
        CallRecipeInfo callRecipeInfo = retrofit.create(CallRecipeInfo.class);
        //pass an instance of the api call with the interface CallRecipeInfo specifying what to pass
        Call<RecipeInfoResponse> call = callRecipeInfo.callRecipeInfo(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeInfoResponse>() {
            @Override
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

    public void getSimilarRecipes(SimilarRecipeListener listener, int id) {
        CallSimilarRecipes callSimilarRecipes = retrofit.create(CallSimilarRecipes.class);
        Call<List<SimilarRecipeApiResponse>> call = callSimilarRecipes.callSimilarRecipe(id, "4", context.getString(R.string.api_key));
        call.enqueue(new Callback<List<SimilarRecipeApiResponse>>() {
            @Override
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

    public void getInstructions(InstructionsListener listener, int id) {
        CallInstructions callInstructions = retrofit.create(CallInstructions.class);
        Call<List<InstructionsReponse>> call = callInstructions.callInstructions(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<List<InstructionsReponse>>() {
            @Override
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




    //all API reponses
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