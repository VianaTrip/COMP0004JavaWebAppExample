package uk.ac.ucl.model;

import java.io.IOException;

/**
 * Factory class to provide a singleton instance of the Model.
 */
public class ModelFactory {
  private static Model model;
  private static final String DATA_DIR = "data";

  /**
   * Gets the singleton instance of the Model.
   *
   * @return The Model instance
   * @throws IOException If there is an error initializing the Model
   */
  public static Model getModel() throws IOException {
    if (model == null) {
      model = new Model();

      // Initialize the model with a JSON storage
      JsonNoteStorage storage = new JsonNoteStorage(DATA_DIR);
      model.initialize(storage);
    }
    return model;
  }
}