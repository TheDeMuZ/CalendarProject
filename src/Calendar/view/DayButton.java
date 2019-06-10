package Calendar.view;

import java.awt.Color;

import javax.swing.*;

import Calendar.interfaces.SpecialButtonInterface;

/**
 * Klasa specjalnego przycisku dnia
 */
public class DayButton extends JButton implements SpecialButtonInterface {
	private static final long serialVersionUID = 1L;
	
	private boolean pressed;
	private int index;
	
	/**
	 * Konstruktor klasy DayButton
	 * @param index Indeks
	 */
	public DayButton(int index) {
		super(Integer.toString(index));
		
		this.index = index;
		this.pressed = false;
		
		setBackground(Color.CYAN);
	}
	
	/*
	 * @see Calendar.interfaces.SpecialButtonInterface#setPressed(boolean)
	 */
	@Override
	public void setPressed(boolean state) {
		if (state) {
			setBackground(Color.BLUE);
			pressed = true;
		}
			
		else {
			setBackground(Color.CYAN);
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
}
