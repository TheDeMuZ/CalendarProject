package Calendar.exception;

/**
 * Wyjatek bledu wykonywania operacji
 */
public class OperationException extends Exception {
	private static final long serialVersionUID = -3410829539373248593L;

	/**
	 * Konstruktor klasy OperationException
	 * @param message Wiadomosc
	 */
	public OperationException(String message) {
		super("B³¹d wydarzenia " + message);
	}
}
