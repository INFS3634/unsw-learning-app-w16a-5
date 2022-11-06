package au.edu.unsw.infs3634.unswlearning;

import android.content.Context;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Api.RecipeApiResponse;
import au.edu.unsw.infs3634.unswlearning.Listeners.RecipeResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
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
}