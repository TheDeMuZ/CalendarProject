package Calendar.view;

import java.awt.*;
import java.time.*;
import java.util.List;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Klasa tworzaca siatke przyciskow kalendarza
 */
public class DaySheet {
	private List <JButton> dayList;
	private static String[] daysOfWeek = {"PN", "WT", "ŒR", "CZ", "PT", "SO", "ND"};
	private CalendarGUI window;
	private Container pane;
	private int x, y, width, height;
	//private EventList eventList;
	
	/**
	 * Kontruktor klasy DaySheet
	 * @param window Okno
	 * @param pane Panel zawartosci
	 * @param date Data
	 * @param x Pozycja x w oknie
	 * @param y Pozycja y w oknie
	 * @param width Szerokosc
	 * @param height Wysokosc
	 */
	public DaySheet(CalendarGUI window, Container pane, LocalDateTime date, int x, int y, int width, int height) {
		this.window = window;
		this.pane = pane;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		//this.eventList = eventList;
		dayList = new ArrayList <JButton>();
		reload(date);
	}
	
	/**
	 * Metoda dostosowujaca siatke do rozmiaru okna
	 * @param x Pozycja x w oknie
	 * @param y Pozycja y w oknie
	 * @param width Szerokosc w oknie
	 * @param height Wysokosc w oknie
	 */
	public void adjust(int x, int y, int width, int height) {		
		Insets insets = pane.getInsets();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		for (int k = 0; k < 7; k++)
			for (int i = 0; i < 7; i++)
				dayList.get(7 * k + i).setBounds(insets.left + x + width * i, insets.top + y + height * k, width, height);
	}
	
	/**
	 * Metoda tworzaca i ustawiajaca ponownie przyciski siatki
	 * @param date Data
	 */
	public void reload(LocalDateTime date) {
		int dayOfWeek_fistDayOfMonth = date.withDayOfMonth(1).getDayOfWeek().getValue() - 1;
		int monthLength = date.getMonth().length(true);
		
		for (JButton v : dayList) {
			v.setVisible(false);
			pane.remove(v);
		}
		
		dayList.clear();
		Insets insets = pane.getInsets();
		int index = 1;
		
		for (int i = 0; i < 7; i++) {
			JButton button = new JButton(daysOfWeek[i]);
			button.setBackground(Color.GREEN);
			button.setBounds(insets.left + x + width * i, insets.top + y, width, height);
			dayList.add(button);
			pane.add(button);
		}
		
		for (int i = 0; i < dayOfWeek_fistDayOfMonth; i++) {
			JButton button = new JButton();
			button.setBackground(Color.LIGHT_GRAY);
			button.setBounds(insets.left + x + width * i, insets.top + y + height, width, height);
			dayList.add(button);
			pane.add(button);
		}
		
		for (int i = dayOfWeek_fistDayOfMonth; i < 7; i++) {
			DayButton button = new DayButton(index++);
			button.setBounds(insets.left + x + width * i, insets.top + y + height, width, height);
			button.addActionListener(window);
			dayList.add(button);
			pane.add(button);
		}
		
		for (int k = 1; k < 6; k++)
			for (int i = 0; i < 7; i++) {
				if (index <= monthLength) {
					DayButton button = new DayButton(index++);
					button.setBounds(insets.left + x + width * i, insets.top + y + height * (k + 1), width, height);
					button.addActionListener(window);
					dayList.add(button);
					pane.add(button);
				}
				
				else {
					JButton button = new JButton();
					button.setBackground(Color.LIGHT_GRAY);
					button.setBounds(insets.left + x + width * i, insets.top + y + height * (k + 1), width, height);
					dayList.add(button);
					pane.add(button);
				}
			}
	}
	
	/**
	 * Metoda ustawiajaca czy konkretny przycisk dnia jest wcisniety
	 * @param day Przycisk dnia
	 */
	public void setFocus(DayButton day) {
		boolean isPressed = day.isPressed();
		
		for (JButton v : dayList)
			if (v.getClass().getSimpleName().equals("DayButton")) {
				((DayButton)v).setPressed(false);
			}
		
		if (!isPressed)
			day.setPressed(true);
	}
}
