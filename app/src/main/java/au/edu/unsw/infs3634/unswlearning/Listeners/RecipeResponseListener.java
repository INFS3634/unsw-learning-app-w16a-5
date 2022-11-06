package au.edu.unsw.infs3634.unswlearning.Listeners;

import au.edu.unsw.infs3634.unswlearning.Api.RecipeApiResponse;

public interface RecipeResponseListener {
    void fetched(RecipeApiResponse response, String message);
    void error(String message);
}
