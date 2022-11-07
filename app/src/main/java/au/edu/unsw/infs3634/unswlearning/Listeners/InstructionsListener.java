package au.edu.unsw.infs3634.unswlearning.Listeners;

import java.util.List;

import au.edu.unsw.infs3634.unswlearning.Api.InstructionsReponse;
import au.edu.unsw.infs3634.unswlearning.Api.RecipeApiResponse;

public interface InstructionsListener {
    void fetched(List<InstructionsReponse> response, String message);
    void error(String message);
}
