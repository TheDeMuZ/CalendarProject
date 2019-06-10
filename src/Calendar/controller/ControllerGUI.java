package Calendar.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Calendar.exception.*;
import Calendar.model.*;
import Calendar.view.*;

/**
 * Kontroler zarzadzajacy interfejsem graficznym
 */
public class ControllerGUI extends Controller {
	private EventList model;
	private CalendarGUI view;
	
	private boolean addEventWindowOpen;
	private int alarmValue;
	
	/**
	 * Konstruktor klasy ControllerGUI
	 * @param model Glowny obiekt modelu
	 * @param view Glowny obiekt interfejsu graficznego
	 */
	public ControllerGUI(EventList model, CalendarGUI view) {
		super();
		this.model = model;
		this.view = view;
		this.addEventWindowOpen = false;
		this.alarmValue = 0;
	}

	/*
	 * @see Calendar.controller.Controller#start()
	 */
	@Override
	public void start() {		
		try {
			model.getListFromDatabase();
		}

		catch (BaseException e) {
			JOptionPane.showMessageDialog(view.getFrame(), e.getMessage(), e.getClass().getSimpleName(), JFrame.EXIT_ON_CLOSE);
		}
		
		Thread alarm = new Thread(new Runnable() {
			@Override
			public void run() {
				for (;;)
					if (alarmValue != 1) {
						LocalDateTime now = LocalDateTime.now();
						
						for (Event v : model.getEventList()) {							
							if (v.getAlarm() != null
									&& v.getAlarm().getYear() == now.getYear() && v.getAlarm().getDayOfYear() == now.getDayOfYear()
									&& v.getAlarm().getHour() == now.getHour() && v.getAlarm().getMinute() == now.getMinute()) {
								alarmValue = 1;
								alarmValue = JOptionPane.showConfirmDialog(view.getFrame(), v.toString(), "ALARM", JOptionPane.OK_CANCEL_OPTION);
								
								if (alarmValue == 0)
									v.setAlarm(null);
							}
						}
					}
			}
		});
    	
    	alarm.start();
		
		view.setController(this);
		view.show();
	}

	/**
	 * Metoda otwierajaca okno wyswietlajace wydarzenia dla konkretnego dnia
	 * @param year Rok
	 * @param month Miesiac
	 * @param button Przycisk z siatki dni glownego okna, ktory zostal klikniety
	 * @return Okno wydarzen dla dnia
	 */
	public DayWindow openDayWindow(int year, int month, DayButton button) {
		return new DayWindow(this, year, month, button);
	}

	/**
	 * Metoda sluzaca do eksportowania danych do pliku
	 */
	public void exportFile() {
		IOWindow io = null;

		try {
			io = new IOWindow(view.getFrame(), false);
		}

		catch (ExtensionException e) {
			JOptionPane.showMessageDialog(view.getFrame(), e.getMessage(),
					e.getClass().getSimpleName(), JFrame.EXIT_ON_CLOSE);
		}

		if (io != null) {
			String filename;

			try {
				filename = io.getFilename();
			}

			catch (NullPointerException e) {
				return;
			}

			String extension = io.getExtension();
			
			if (extension.equals(new String("xml")) || extension.equals(new String("XML")))
				try {
					model.saveXMLFile(filename);
				}

				catch (OperationException e) {
					JOptionPane.showMessageDialog(view.getFrame(),
							e.getMessage(), e.getClass().getSimpleName(),
							JFrame.EXIT_ON_CLOSE);
				}

			else if (extension.equals(new String("csv")) || extension.equals(new String("CSV")))
				try {
					model.exportCSV(filename);
				}

				catch (OperationException e) {
					JOptionPane.showMessageDialog(view.getFrame(),
							e.getMessage(), e.getClass().getSimpleName(),
							JFrame.EXIT_ON_CLOSE);
				}
		}
	}

	/**
	 * Metoda sluzaca do importowania danych z pliku
	 */
	public void importFile() {
		IOWindow io = null;

		try {
			io = new IOWindow(view.getFrame(), false);
		}

		catch (ExtensionException e) {
			JOptionPane.showMessageDialog(view.getFrame(), e.getMessage(), e
					.getClass().getSimpleName(), JFrame.EXIT_ON_CLOSE);
		}

		if (io != null) {
			String filename;

			try {
				filename = io.getFilename();
			}

			catch (NullPointerException e) {
				return;
			}

			String extension = io.getExtension();

			if (extension.equals(new String("xml")) || extension.equals(new String("XML")))
				try {
					model.loadXMLFile(filename);
				}

				catch (BaseException | CollisionException | OperationException e) {
					JOptionPane.showMessageDialog(view.getFrame(),
							e.getMessage(), e.getClass().getSimpleName(),
							JFrame.EXIT_ON_CLOSE);
				}
			
			else if (extension.equals(new String("csv")) || extension.equals(new String("CSV")))
				try {
					model.importCSV(filename);
				}

				catch (OperationException e) {
					JOptionPane.showMessageDialog(view.getFrame(),
							e.getMessage(), e.getClass().getSimpleName(),
							JFrame.EXIT_ON_CLOSE);
				}
		}
	}
	
	/**
	 * Metoda generujaca opisy wydarzen dla interfejsu graficznego
	 * @param eventList Lista wydarzen
	 * @return Wygenerowane opisy wydarzen
	 */
	public List<String> getStringsForEvents(List <Event> eventList) {
		List <String> eventStrings = new ArrayList<String>();

		for (Event v : eventList) {		
			String text = "<html>";
			
			text += v.getStart().getYear() + "/"
				+ String.format("%02d", v.getStart().getMonthValue()) + "/"
				+ String.format("%02d", v.getStart().getDayOfMonth());
			
			if (!(v.getStart().getYear() == v.getEnd().getYear() && v.getStart().getDayOfYear() == v.getEnd().getDayOfYear()))
				text += " - " + v.getEnd().getYear() + "/"
					+ String.format("%02d", v.getEnd().getMonthValue()) + "/"
					+ String.format("%02d", v.getEnd().getDayOfMonth());
			
			text += "<br>Start: "
				+ v.getStart().format(DateTimeFormatter.ofPattern("HH:mm"))
				+ "<br>Koniec: "
				+ v.getEnd().format(DateTimeFormatter.ofPattern("HH:mm"))
				+ "<br>Alarm: ";
			
			if (v.getAlarm() == null)
				text += "Brak";
			
			else
				text += v.getAlarm().format(DateTimeFormatter.ofPattern("HH:mm"));
			
			text += "<br>Nazwa: "
				+ v.getName()
				+ "<br>Miejsce: ";
			
			if (v.getPlace() == null)
				text += "Brak";
			
			else
				text += v.getPlace();
			
			text += "<br>Opis: ";
			
			if (v.getDescription() == null)
				text += "Brak";
			
			else
				text += v.getDescription();
			
			eventStrings.add(text);
		}
		
		return eventStrings;
	}

	/**
	 * Metoda filtrujaca wydarzenia za pomoca dnia oraz wzoru
	 * @param date Data
	 * @param pattern Szukany tekst
	 * @return Szukane wydarzenia
	 */
	public List <Event> getEventsForDay(LocalDateTime date, String pattern) {
		model.sort();
		List <Event> tmp = new ArrayList <>();
		
		if (pattern.equals(new String("")))
			try {
				tmp = model.filterByDay(date);
			}
			
			catch (CollisionException | BaseException e) {
				JOptionPane.showMessageDialog(view.getFrame(), e.getMessage(), e
						.getClass().getSimpleName(), JFrame.EXIT_ON_CLOSE);
			}
		
		else {
			try {
				tmp = model.filterByPattern(pattern).filterByDay(date);
			}
			
			catch (CollisionException | BaseException e) {
				JOptionPane.showMessageDialog(view.getFrame(), e.getMessage(), e
						.getClass().getSimpleName(), JFrame.EXIT_ON_CLOSE);
			}
		}
		
		return tmp;
	}
	
	/**
	 * Metoda filtrujaca wydarzenia za pomocą wzoru
	 * @param pattern Szukany tekst
	 * @return Szukane wydarzenia
	 */
	public List <Event> getEventList(String pattern) {
		model.sort();
		List <Event> tmp = new ArrayList <>();
		
		if (!pattern.equals(new String("")))
			try {
				tmp = model.filterByPattern(pattern).getEventList();
			}
			
			catch (CollisionException | BaseException e) {
				JOptionPane.showMessageDialog(view.getFrame(), e.getMessage(), e
						.getClass().getSimpleName(), JFrame.EXIT_ON_CLOSE);
			}
		
		else
			tmp = model.getEventList();
		
		return tmp;
	}
	
	/**
	 * Metoda usuwajaca wydarzenia starsze niz podana data
	 * @param date Podana data
	 */
	public void deleteEventsBefore(LocalDateTime date) {
		try {
			model.deleteBefore(date);
		}
		
		catch (BaseException e) {
			JOptionPane.showMessageDialog(view.getFrame(), e.getMessage(), e
					.getClass().getSimpleName(), JFrame.EXIT_ON_CLOSE);
		}
	}
	
	/**
	 * Metoda otwierajaca okno dodawania nowych wydarzen
	 */
	public void openNewEventWindow() {
		if (view.getDayWindow() != null)
			view.getDayWindow().dispose2();
		
		setAddEventWindowOpenVariable(true);
		new AddEventWindow(this);
	}
	
	/**
	 * Metoda ustawiajaca wartosc zmiennej addEventWindowOpen
	 * @param is Nowa wartosc zmiennej
	 */
	public void setAddEventWindowOpenVariable(boolean is) {
		this.addEventWindowOpen = is;
	}
	
	/**
	 * Metoda zwracajaca wartosc zmiennej addEventWindowOpen
	 * @return Czy okno dodawania wydarzen jest otwarte
	 */
	public boolean isAddEventWindowOpen() {
		return addEventWindowOpen;
	}
	
	/**
	 * Metoda dodajaca wydarzenia do modelu
	 * @param window Okno dodawania nowych wydarzen
	 */
	public void addEvent(AddEventWindow window) {
		try {
			model.addEvent(window.getName(), window.getPlace(),
					window.getDescription(), window.getStart(),
					window.getEnd(), window.getAlarm());
		}

		catch (CollisionException | BaseException e) {
			JOptionPane.showMessageDialog(view.getFrame(), e.getMessage(), e
					.getClass().getSimpleName(), JFrame.EXIT_ON_CLOSE);
		}
	}

	/**
	 * Metoda usuwajaca wydarzenia z modelu
	 * @param button Przycisk wydarzenia
	 */
	public void deleteEvent(EventButton button) {
		try {
			model.deleteEvent(button.getEvent());
		}

		catch (BaseException e) {
			JOptionPane.showMessageDialog(view.getFrame(),
					e.getMessage(), e.getClass().getSimpleName(),
					JFrame.EXIT_ON_CLOSE);
		}
	}

	/*
	 * @see Calendar.controller.Controller#stop()
	 */
	@Override
	public void stop() {
		try {
			model.addOtherEventsToDatabase();
		}
		
		catch (BaseException e) {
			JOptionPane.showMessageDialog(view.getFrame(), e.getMessage(), e.getClass().getSimpleName(), JFrame.EXIT_ON_CLOSE);
		}
	}
}
