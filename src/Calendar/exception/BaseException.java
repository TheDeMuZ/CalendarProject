package Calendar.exception;

/**
 * Ogólny wyjatek programu
 */
public class BaseException extends Exception {
	private static final long serialVersionUID = 8109294850608855650L;
	
	/**
	 * Konstruktor klasy BaseException
	 * @param message Wiadomosc
	 */
	public BaseException(String message) {
		super("B³¹d wydarzenia: " + message);
	}
}
