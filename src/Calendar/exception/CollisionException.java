package Calendar.exception;

/**
 * Wyjatek kolizji wydarzen
 */
public class CollisionException extends Exception {
	private static final long serialVersionUID = 3349393915014351908L;
	
	/**
	 * Konstruktor klasy CollisionException
	 * @param message Wiadomosc
	 */
	public CollisionException(String message) {
		super("Kolizja wydarzeñ: " + message);
	}
}
