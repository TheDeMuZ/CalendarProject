package Calendar.interfaces;

/**
 * Interfejs specjalnych przyciskow interfejsu graficznego
 */
public interface SpecialButtonInterface {
	/**
	 * Metoda ustawiajaca stan przycisku
	 * @param state Stan
	 */
	public void setPressed(boolean state);
	
	/**
	 * Metoda zwracajaca stan przycisku
	 * @return Stan przycisku
	 */
	public boolean isPressed();
	
	/**
	 * Metoda zwracajaca indeks przycisku
	 * @return Indeks
	 */
	public int getIndex();
}
