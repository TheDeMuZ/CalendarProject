package Calendar.view;

import java.awt.*;
import java.awt.event.*;
import java.time.*;

import javax.swing.*;

import Calendar.controller.ControllerGUI;

/**
 * Klasa tworzaca glowne okno interfejsu graficznego aplikacji
 */
public class CalendarGUI implements ActionListener, ItemListener {
	private ControllerGUI controller;
	
	private MenuFrame frame;
	private Insets insets;
	private DaySheet calendar;
	private DayWindow dayWindow;
	
	private JComboBox <String> comboBox_monthList, comboBox_yearList;
	private JButton button_Left, button_Right;
	
	/**
	 * Konstruktor klasy CalendarGUI
	 */
	public CalendarGUI() {}
	
	/**
	 * Metoda uruchamiajaca widok aplikacji
	 */
	public void show() {
		final CalendarGUI gui = this;
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame = new MenuFrame(controller, gui, "Kalendarz");
				frame.setSize(800, 600);
				frame.setVisible(true);
				addComponents(frame);
				
				frame.addComponentListener(new ComponentAdapter() {
				    public void componentResized(ComponentEvent componentEvent) {
				    	if (calendar != null)
				    		calendar.adjust(5, 50, frame.getWidth() / 7 - 4, frame.getHeight() / 7 - 17);
				    	
				    	if (button_Left != null && button_Right != null) {
				    		setCoords(button_Left, frame.getWidth() - 110, 10);
				    		setCoords(button_Right, frame.getWidth() - 70, 10);
				    	}
				    }
				});
			}
		});
	}
	
	/**
	 * Metoda ustawiajaca okno wydarzen dla dnia
	 * @param window Okno wydarzen dla dnia
	 */
	public void setDayWindow(DayWindow window) {
		if (dayWindow != null)
			dayWindow.dispose();
		
		dayWindow = window;
	}
	
	/**
	 * Metoda ustawiajaca kontroler
	 * @param controller Kontroler
	 */
	public void setController(ControllerGUI controller) {
		this.controller = controller;
	}
	
	/**
	 * Metoda zwracaja ramke glownego okna
	 * @return Ramka okna
	 */
	public MenuFrame getFrame() {
		return frame;
	}
	
	/**
	 * Metoda dodajaca komponenty do glownego okna
	 * @param frame Ramka okna
	 */
	private void addComponents(MenuFrame frame) {
		Container pane = frame.getContentPane();
		LocalDateTime date = LocalDateTime.now();
		insets = pane.getInsets();
		pane.setLayout(null);
		
		//wybór miesi¹ca
		JLabel label_monthList = new JLabel("Wybierz miesi¹c:");
		pane.add(label_monthList);
		setCoords(label_monthList, 10, 15);
		
		String monthList[] = {
			"Styczeñ", "Luty", "Marzec", "Kwiecieñ",
			"Maj", "Czerwiec", "Lipiec", "Sierpieñ",
			"Wrzesieñ", "PaŸdziernik", "Listopad", "Grudzieñ"
		};
		
		comboBox_monthList = new JComboBox <String>(monthList);
		pane.add(comboBox_monthList);
		setCoords(comboBox_monthList, 115, 10);
		comboBox_monthList.setSelectedIndex(date.getMonthValue() - 1);
		comboBox_monthList.addItemListener(this);
		
		//wybór roku
		JLabel label_yearList = new JLabel("Wybierz rok:");
		pane.add(label_yearList);
		setCoords(label_yearList, 240, 15);
		
		String[] yearList = new String[51];
		int oldestYear = date.getYear() - 25;
		
		for (int i = 0; i <= 50; i++)
			yearList[i] = Integer.toString(oldestYear + i);
		
		comboBox_yearList = new JComboBox <String>(yearList);
		pane.add(comboBox_yearList);
		setCoords(comboBox_yearList, 320, 10);
		comboBox_yearList.setSelectedIndex(25);
		comboBox_yearList.addItemListener(this);
		
		//strza³eczki
		button_Left = new JButton("<");
		button_Left.addActionListener(this);
		pane.add(button_Left);
		setCoords(button_Left, frame.getWidth() - 110, 10);

		button_Right = new JButton(">");
		button_Right.addActionListener(this);
		pane.add(button_Right);
		setCoords(button_Right, frame.getWidth() - 70, 10);
		
		//kalendarz
		calendar = new DaySheet(this, pane, date, 5, 50, frame.getWidth() / 7 - 4, frame.getHeight() / 7 - 17);
	}
	
	/**
	 * Metoda pomocnicza do ustawiania pozycji komponentow
	 * @param object Komponent
	 * @param x Pozycja x
	 * @param y Pozycja y
	 */
	private void setCoords(Container object, int x, int y) {
		Dimension size = object.getPreferredSize();
		object.setBounds(insets.left + x, insets.top + y, size.width, size.height);
	}
	
	/**
	 * Metoda zwracaja obiekt okna wydarzen dla dnia
	 * @return Okno wydarzen dla dnia
	 */
	public DayWindow getDayWindow() {
		return dayWindow;
	}
	
	/*
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent event) {
		Object object = event.getSource();
		
		if ((object == comboBox_monthList || object == comboBox_yearList) && comboBox_yearList != null && comboBox_monthList != null) {
			calendar.reload(LocalDateTime.of(Integer.parseInt((String)comboBox_yearList.getSelectedItem()), comboBox_monthList.getSelectedIndex() + 1, 1, 0, 0));
		}
	}

	/*
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object object = event.getSource();
		
		if (object instanceof DayButton) {
			final DayButton button = (DayButton)object;
			
			if (!controller.isAddEventWindowOpen()) {
				calendar.setFocus(button);
				
				if (button.isPressed())
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							if (dayWindow != null)
								dayWindow.dispose();
							
							dayWindow = controller.openDayWindow(Integer.parseInt((String)comboBox_yearList.getSelectedItem()), comboBox_monthList.getSelectedIndex() + 1, button);
						}
					});
				
				else if (dayWindow != null)
					dayWindow.dispose();
			}
		}
		
		else if (object == button_Left) {
			if (dayWindow != null)
				dayWindow.dispose();
			
			if (comboBox_monthList.getSelectedIndex() == 0) {
				if (comboBox_yearList.getSelectedIndex() != 0) {
					comboBox_monthList.setSelectedIndex(11);
					comboBox_yearList.setSelectedIndex(comboBox_yearList.getSelectedIndex() - 1);
				}
			}
			
			else
				comboBox_monthList.setSelectedIndex(comboBox_monthList.getSelectedIndex() - 1);
		}
		
		else if (object == button_Right) {
			if (dayWindow != null)
				dayWindow.dispose();
			
			if (comboBox_monthList.getSelectedIndex() == 11) {
				if (comboBox_yearList.getSelectedIndex() != 50) {
					comboBox_monthList.setSelectedIndex(0);
					comboBox_yearList.setSelectedIndex(comboBox_yearList.getSelectedIndex() + 1);
				}
			}
			
			else
				comboBox_monthList.setSelectedIndex(comboBox_monthList.getSelectedIndex() + 1);
		}
	}
}
