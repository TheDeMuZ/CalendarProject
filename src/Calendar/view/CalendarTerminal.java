package Calendar.view;

/**
 * Klasa inferfejsu terminala
 */
public class CalendarTerminal {
	/**
	 * Metoda wyswietlajaca blad
	 * @param text Tekst bledu
	 */
	public void showError(String text) {
		System.out.println("Uwaga wyst¹pi³ b³¹d:");
		System.out.println(text);
		System.out.println();
	}
		
	/**
	 * Metoda wyswietlajaca dane przekazane przez kontroler
	 * @param header Nag³owek
	 * @param body Tekst glowny
	 */
	public void showStatement(String header, String body){
		System.out.println(header);
		System.out.println(body);
	}
	   
	/**
	 * Metoda wyswietlajaca dany tekst
	 * @param text Tekst
	 */
	public void showText(String text){
		System.out.println(text);
	}
}
