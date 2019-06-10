package Calendar.exception;

/**
 * Wyjatek nieprawidlowego rozszerzenia pliku
 */
public class ExtensionException extends Exception {
	private static final long serialVersionUID = 5003846808519225808L;
	
	/**
	 * Konstruktor klasy ExtensionException
	 * @param message Wiadomosc
	 */
	public ExtensionException(String message) {
		super("Nieobs³ugiwany typ pliku: " + message);
	}
}
