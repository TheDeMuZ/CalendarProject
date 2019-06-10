package Calendar.view;

import java.awt.Color;

import javax.swing.JButton;

import Calendar.interfaces.SpecialButtonInterface;
import Calendar.model.Event;

/**
 * Klasa tworzaca przyciski wydarzen
 */
public class EventButton extends JButton implements SpecialButtonInterface {
	private static final long serialVersionUID = 1739140352604156739L;
	
	private Event event;
	private int index;
	private boolean pressed;
	
	/**
	 * Konstruktor klasy EventButton
	 * @param index Indeks
	 * @param text Tekst
	 * @param event Wydarzenie
	 */
	public EventButton(int index, String text, Event event) {
		super(text);
		this.event = event;
		this.index = index;
		this.pressed = false;
		
		setBackground(Color.LIGHT_GRAY);
	}

	/*
	 * @see Calendar.interfaces.SpecialButtonInterface#setPressed(boolean)
	 */
	@Override
	public void setPressed(boolean state) {
		if (state) {
			setBackground(Color.YELLOW);
			pressed = true;
		}
			
		else {
			setBackground(Color.LIGHT_GRAY);
			pressed = false;
		}
	}

	/*
	 * @see Calendar.interfaces.SpecialButtonInterface#isPressed()
	 */
	@Override
	public boolean isPressed() {
		return pressed;
	}

	/*
	 * @see Calendar.interfaces.SpecialButtonInterface#getIndex()
	 */
	@Override
	public int getIndex() {
		return index;
	}
	
	/**
	 * Metoda zwracajaca wydarzenie przypisane do przycisku
	 * @return Wydarzenie
	 */
	public Event getEvent() {
		return event;
	}
}
