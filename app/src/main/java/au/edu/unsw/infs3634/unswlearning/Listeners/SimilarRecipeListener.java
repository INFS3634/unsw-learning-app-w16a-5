package au.edu.unsw.infs3634.unswlearning.Listeners;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Api.RecipeApiResponse;
import au.edu.unsw.infs3634.unswlearning.Api.SimilarRecipeApiResponse;

public interface SimilarRecipeListener {
    void fetched(List<SimilarRecipeApiResponse> response, String message);
    void error(String message);
}
