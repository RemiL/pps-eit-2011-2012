package searcher;

public class InvalideQueryException extends Exception {

	private static final long serialVersionUID = 3479600833201842390L;

	public InvalideQueryException(String query) {
		super("La requ�te \"" + query + "\" est invalide.");
	}
}
