package Calendar.controller;

import java.io.File;
import java.time.format.DateTimeParseException;

import Calendar.exception.*;
import Calendar.exception.BaseException;
import Calendar.model.*;
import Calendar.view.*;

/**
 * Kontroler dla terminala
 */
public class ControllerTerminal extends Controller {
	private EventList model;
	private CalendarTerminal view;
	
	/**
	 * Konstruktor klasy ControllerTerminal
	 * @param model Glowny obiekt modelu
	 * @param view Glowny obiekt interfejsu
	 */
	public ControllerTerminal(EventList model, CalendarTerminal view) {
		super();
		this.model = model;
		this.view = view;
	}
	
	/**
	 * Metoda rozpoznajaca argumenty wykonania programu
	 * @param args Argumenty programu
	 */
	public void readArguments(String[] args) {
		try {
			if(args[0].equals(new String("-add")))
				addArgument(args);
			else if(args[0].equals(new String("-delete_event")))
				deleteEventArgument(args);
			else if(args[0].equals(new String("-delete_alarm")))
				deleteAlarmArgument(args);
			else if(args[0].equals(new String("-show")))
				showArgument(args);
			else if(args[0].equals(new String("-filter")))
				filterArgument(args);
			else if(args[0].equals(new String("-export")))
				exportToFile(args);
			else if(args[0].equals(new String("-import")))
				importToFile(args);
			else 
				view.showError("Wprowadzi³eœ niepoprawnie argument, spójrz w instrukcjê");
		} 
		catch (CollisionException | BaseException | OperationException e) {
			view.showError(e.toString());
		} 
		catch (DateTimeParseException e) {
			view.showError("Data i godzina musi wygl¹daæ nastêpuj¹co YYYY-MM-DD HH:MM");
		}
	}
	
	/**
	 * Metoda dodajaca wydarzenia do modelu
	 * @param args Argumenty programu
	 * @throws CollisionException
	 * @throws BaseException
	 */
	private void addArgument(String[] args) throws CollisionException, BaseException {
		String name = null;
		String place= null;
		String description= null;
		String dataStart= null;
		String dataEnd= null;
		String dataAlarm= null;
		
		for(int i = 1, j=2; i < args.length && j < args.length; i+=2, j+=2) {
			if(args[i].equals(new String("-n"))) {
				name = args[j];
			}
			else if(args[i].equals(new String("-p"))) {
				place = args[j];
			}
			else if(args[i].equals(new String("-d"))) {
				description = args[j];
			}
			else if(args[i].equals(new String("-s"))) {
				dataStart = args[j];
			}
			else if(args[i].equals(new String("-e"))) {
				dataEnd = args[j];
			}
			else if(args[i].equals(new String("-a"))) {
				dataAlarm = args[j];
			}
		}
		try {
			model.addEvent(name, place, description, dataStart, dataEnd, dataAlarm);
		} catch (CollisionException | BaseException e) {
			view.showError(e.toString());
		}
		
	}
	
	/**
	 * Metoda usuwajaca wydarzenia z modelu
	 * @param args Argumenty programu
	 * @throws BaseException
	 */
	private void deleteEventArgument(String[] args) throws BaseException {
		
		if(args.length > 3 || args.length < 3) 
			view.showError("Wprowadzi³eœ niepoprawnie argument, spójrz w instrukcjê");
		else {
			if(args[1].equals(new String("-n")))
				model.deleteEvent(model.searchEvent(args[2]));
			else if(args[1].equals(new String("-s"))) 
				model.deleteEvent(model.searchEventStartData(args[2]));
			else 
				view.showError("Wprowadzi³eœ niepoprawnie argument, spójrz w instrukcjê");
		}
	}
	
	/**
	 * Metoda usuwajaca alarm wydarzenia
	 * @param args
	 */
	private void deleteAlarmArgument(String[] args) {
		if(args.length != 3) 
			view.showError("Wprowadzi³eœ niepoprawnie argument, spójrz w instrukcjê");
		else {
			if(args[1].equals(new String("-a"))) 
				model.deleteAlarm(model.searchEventAlarm(args[2]));
			else if(args[1].equals(new String("-n"))) 
				model.deleteAlarm(model.searchEvent(args[2]));
			else
				view.showError("Wprowadzi³eœ niepoprawnie argument, spójrz w instrukcjê");
		}
	}
	
	/**
	 * Metoda wyswietlajaca zawartosc modelu
	 * @param args Argumenty programu
	 * @throws CollisionException
	 * @throws BaseException
	 */
	private void showArgument(String[] args) throws CollisionException, BaseException {
		if(args.length == 1) {
			view.showText("Lista eventów:");
			for(Event el: model.getEventList())
				view.showText(el.toString());
		}
		else if(args.length == 2)
			if(args[1].equals(new String("-about")))
				view.showStatement("O programie", "Program napisany w javie");
			else
				view.showError("Wprowadzi³eœ niepoprawnie argument, spójrz w instrukcjê");
		else if(args.length == 3)
			if(args[1].equals(new String("-d"))) {
				view.showText("Lista eventów na konkretny dzien:" + args[2]);
				for(Event el: model.filterByDay(args[2]))
					view.showText(el.toString());
			}
			else
				view.showError("Wprowadzi³eœ niepoprawnie argument, spójrz w instrukcjê");
			
	}
	
	/**
	 * Metoda wyswietlajaca przefiltrowana zawartosc modelu
	 * @param args Argumenty programu
	 */
	private void filterArgument(String[] args) {
		if(args.length > 5 || args.length < 5)
			view.showError("Wprowadzi³eœ niepoprawnie argument, spójrz w instrukcjê");
		else {
			String dataStart= null;
			String dataEnd= null;
			for(int i = 1, j=2; i < args.length && j < args.length; i+=2, j+=2) {
				if(args[i].equals(new String("-s"))) {
					dataStart = args[j];
				}
				else if(args[i].equals(new String("-e"))) {
					dataEnd = args[j];
				}
			}
			
			if(dataStart == null || dataEnd == null)
				view.showError("Wprowadzi³eœ niepoprawnie argument, spójrz w instrukcjê");
			else {
				view.showText("Lista eventów od " + dataStart + " do " + dataEnd);
				for(Event el: model.filterPeriod(dataStart, dataEnd))
					view.showText(el.toString());
			}
		}
	}
	
	/**
	 * Metoda sluzaca do eksportowania danych
	 * @param args Argumenty programu
	 * @throws OperationException
	 */
	public void exportToFile(String[] args) throws OperationException {
		if(args.length > 3 || args.length < 3)
			view.showError("Wprowadzi³eœ niepoprawnie argument, spójrz w instrukcjê");
		else {
			String[] splited_path = args[2].split("\\.");
			if(splited_path[1].equalsIgnoreCase(new String("csv")))
				model.exportCSV(args[2]);
			else if(splited_path[1].equalsIgnoreCase(new String("xml"))) {
				model.saveXMLFile(args[2]);
			}
			else {
				view.showError("SprawdŸ wpisane rozszerzenie pliku. Musi byæ ono w postaci 'csv' lub 'xml'");
			}
		}
	}
	
	/**
	 * Metoda sluzaca do importowania danych
	 * @param args Argumenty programu
	 * @throws OperationException
	 * @throws BaseException
	 * @throws CollisionException
	 */
	public void importToFile(String[] args) throws OperationException, BaseException, CollisionException {
		if(args.length > 3 || args.length < 3)
			view.showError("Wprowadzi³eœ niepoprawnie argument, spójrz w instrukcjê");
		else {
			String extension = getFileExtension(new File(args[2]));
			if(extension.equalsIgnoreCase(new String(".csv")))
				model.importCSV(args[2]);
			else if(extension.equalsIgnoreCase(new String(".xml"))) {
				model.loadXMLFile(args[2]);
			}
			else {
				view.showError("SprawdŸ wpisane rozszerzenie pliku. Musi byæ ono w postaci 'csv' lub 'xml'");
			}
		}
	}

	/*
	 * @see Calendar.controller.Controller#start()
	 */
	@Override
	public void start() {
		try {
			model.getListFromDatabase();
		} catch (BaseException e) {
			view.showError(e.toString());
		}
		
	}

	/*
	 * @see Calendar.controller.Controller#stop()
	 */
	@Override
	public void stop() {
		try {
			model.addOtherEventsToDatabase();
		} catch (BaseException e) {
			view.showError(e.toString());
		}
		
	}
}
