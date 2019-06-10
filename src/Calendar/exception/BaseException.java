package Calendar.exception;

/**
 * Og�lny wyjatek programu
 */
public class BaseException extends Exception {
	private static final long serialVersionUID = 8109294850608855650L;
	
	/**
	 * Konstruktor klasy BaseException
	 * @param message Wiadomosc
	 */
	public BaseException(String message) {
		super("B��d wydarzenia: " + message);
	}
}
