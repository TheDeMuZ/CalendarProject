package Calendar.controller;

import java.io.File;

/**
 * Glowna klasa reagujaca na poczynania uzytkownika zarzadzajaca modelem i widokiem
 */
public abstract class Controller {
	/**
	 * Metoda, ktora uruchamia kontroler
	 */
	public abstract void start();
	
	/**
	 * Metoda, ktora konczy dzialanie kontrolera
	 */
	public abstract void stop();
    
	/**
	 * Metoda sprawdzajaca rozszerzenie pliku
	 * @param file Plik do sprawdzenia
	 * @return Rozszerzenie pliku
	 */
	protected String getFileExtension(File file) {
        String extension = "";
 
        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
            extension = "";
        }
 
        return extension;
    }
}
