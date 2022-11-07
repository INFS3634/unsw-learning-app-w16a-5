package au.edu.unsw.infs3634.unswlearning.Listeners;

import au.edu.unsw.infs3634.unswlearning.Api.RecipeApiResponse;
import au.edu.unsw.infs3634.unswlearning.Api.RecipeInfoResponse;

public interface RecipeInfoListener {
    void fetched(RecipeInfoResponse response, String message);
    void error(String message);
}
