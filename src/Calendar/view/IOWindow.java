package Calendar.view;

import java.awt.Component;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import Calendar.exception.ExtensionException;

/**
 * Klasa sluzaca do wykonania operacji odczytu/zapisu danych z pliku
 */
public class IOWindow {
	private JFileChooser fileChooser;
	
	/**
	 * Konstruktor klasy IOWIndow
	 * @param object Okno
	 * @param mode Tryb pracy (eksport/import)
	 * @throws ExtensionException
	 */
	public IOWindow(Component object, boolean mode) throws ExtensionException {
		fileChooser = new JFileChooser();
		
		String[] extensionNameList = {"XML", "Outlook CSV"};
		String[] extensionList = {"xml", "csv"};
		
		for (int i = 0; i < extensionList.length; i++)
			fileChooser.setFileFilter(new FileNameExtensionFilter(extensionNameList[i], extensionList[i]));
		
		if (mode) {
			fileChooser.showSaveDialog(object);
		}
		
		else {
			fileChooser.showOpenDialog(object);
		}
		
		boolean isExtensionSupported = false;
		
		if (!(fileChooser.getSelectedFile() == null || getFilename() == null))
		{
			if (!getFilename().contains("."))
				throw new ExtensionException("Plik nie ma rozszerzenia!");
			
			for (String v : extensionList)
				if (getExtension().equals(v))
					isExtensionSupported = true;
			
			if (!isExtensionSupported)
				throw new ExtensionException(getExtension());
		}
	}
	
	/**
	 * Metoda zwracaja sciezke pliku
	 * @return Sciezka pliku
	 */
	public String getFilename() {
		return fileChooser.getSelectedFile().getAbsolutePath();
	}
	
	/**
	 * Metoda zwracajaca rozszerzenie pliku
	 * @return Rozszerzenie
	 */
	public String getExtension() {
		return getFilename().substring(getFilename().lastIndexOf(".")).substring(1);
	}
}
