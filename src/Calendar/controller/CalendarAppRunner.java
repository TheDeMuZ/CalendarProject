package Calendar.controller;

import Calendar.model.*;
import Calendar.view.*;

/**
 * Glowna klasa programu
 */
public class CalendarAppRunner {
	/*
	 * Glowna metoda, ktora wywoluje sie przy wlaczeniu programu
	 */
	public static void main(String[] args) {
		EventList model = new EventList();
		
		if(args.length > 0) {
			CalendarTerminal view = new CalendarTerminal();
			ControllerTerminal controller = new ControllerTerminal(model, view);
			controller.start();
			controller.readArguments(args);
			controller.stop();	
		}
		
		else {
			CalendarGUI view = new CalendarGUI();
			ControllerGUI controller = new ControllerGUI(model, view);
			controller.start();
		}
	}
}
