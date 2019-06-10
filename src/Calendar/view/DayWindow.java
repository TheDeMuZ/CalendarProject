package Calendar.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Calendar.controller.ControllerGUI;
import Calendar.model.Event;

import java.awt.Container;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa tworzaca okno wydarzen dla konkretnego dnia
 */
public class DayWindow implements ActionListener {
	private ControllerGUI controller;
	private JFrame frame;

	private DayButton dayButton;
	private LocalDateTime date;
	private List<EventButton> buttonList;

	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem menu_Add, menu_DeleteSelected, menu_SelectAll,
			menu_UnselectAll;

	private JButton button_Up, button_Down, button_Filter;
	private JTextField textField_Filter;
	
	private int globalY = 0;
	
	/**
	 * Konstruktor klasy DayWindow (dla wszystkich wydarzen)
	 * @param controller Kontroler
	 */
	public DayWindow(ControllerGUI controller) {
		this.controller = controller;

		frame = new JFrame("Wydarzenia");
		frame.setSize(400, 700);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		addMenu();
		addComponents();

		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				resize();
			}
		});
	}

	/**
	 * Konstruktor klasy DayWindow (dla konkretnego dnia)
	 * @param controller Kontroler
	 * @param year Rok
	 * @param month Miesiac
	 * @param dayButton Przycisk dnia
	 */
	public DayWindow(ControllerGUI controller, int year, int month, final DayButton dayButton) {
		this.controller = controller;
		this.dayButton = dayButton;
		this.date = LocalDateTime.of(year, month, dayButton.getIndex(), 0, 0);

		frame = new JFrame(Integer.toString(year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", dayButton.getIndex()));
		frame.setSize(400, 700);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		addMenu();
		addComponents();

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				dayButton.setPressed(false);
			}
		});

		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				resize();
			}
		});
	}

	/**
	 * Metoda zamykajaca okno wydarzen dla dnia
	 */
	public void dispose2() {
		if (dayButton != null)
			dayButton.setPressed(false);

		dispose();
	}

	/**
	 * Metoda dodajaca do okna komponenty menu
	 */
	private void addMenu() {
		menuBar = new JMenuBar();

		menu = new JMenu("Operacje");
		menuBar.add(menu);

		menu_Add = new JMenuItem("Dodaj wydarzenie");
		menu_Add.addActionListener(this);
		menu.add(menu_Add);

		menu.addSeparator();

		menu_DeleteSelected = new JMenuItem("Usuñ wybrane wydarzenia");
		menu_DeleteSelected.addActionListener(this);
		menu.add(menu_DeleteSelected);

		menu.addSeparator();

		menu_SelectAll = new JMenuItem("Zaznacz wszystkie");
		menu_SelectAll.addActionListener(this);
		menu.add(menu_SelectAll);

		menu.addSeparator();

		menu_UnselectAll = new JMenuItem("Odznacz wszystkie");
		menu_UnselectAll.addActionListener(this);
		menu.add(menu_UnselectAll);

		frame.setJMenuBar(menuBar);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Metoda dodajaca do okna glowne komponenty
	 */
	private void addComponents() {
		Container pane = frame.getContentPane();
		pane.setLayout(null);

		button_Up = new JButton();
		button_Up.setBounds(frame.getWidth() - 40, 5, 20, frame.getHeight() / 2 - 35);
		button_Up.addActionListener(this);
		pane.add(button_Up);

		button_Down = new JButton();
		button_Down.setBounds(frame.getWidth() - 40, frame.getHeight() / 2 - 30, 20, frame.getHeight() / 2 - 35);
		button_Down.addActionListener(this);
		pane.add(button_Down);

		textField_Filter = new JTextField();
		textField_Filter.setBounds(5, 6, frame.getWidth() * 2 / 3, textField_Filter.getPreferredSize().height);
		pane.add(textField_Filter);
		
		button_Filter = new JButton("Szukaj");
		button_Filter.setBounds(10 + frame.getWidth() * 2 / 3, 4, frame.getWidth() * 1 / 3 - 53, textField_Filter.getPreferredSize().height);
		button_Filter.addActionListener(this);
		pane.add(button_Filter);
		
		reload();
	}

	/**
	 * Metoda wczytujaca ponownie wydarzenia
	 */
	public void reload() {
		reload("");
	}
	
	/**
	 * Metoda wczytujaca ponownie i filtrujaca wydarzenia
	 * @param pattern Wzor
	 */
	public void reload(String pattern) {
		Container pane = frame.getContentPane();

		if (buttonList != null) {
			for (EventButton v : buttonList)
				pane.remove(v);

			buttonList.clear();
		}

		List<Event> eventList;

		if (dayButton != null)
			eventList = controller.getEventsForDay(date, pattern);

		else
			eventList = controller.getEventList(pattern);

		List<String> eventStrings = controller.getStringsForEvents(eventList);

		buttonList = new ArrayList<EventButton>();

		for (int i = 0; i < eventList.size(); i++)
			buttonList.add(new EventButton(i, eventStrings.get(i), eventList
					.get(i)));

		for (EventButton v : buttonList) {
			pane.add(v);
			v.addActionListener(this);
		}

		resize();
		frame.setSize(frame.getWidth() + 1, frame.getHeight());
		frame.setSize(frame.getWidth() - 1, frame.getHeight());
	}

	/**
	 * Metoda, ktora dopasowuje przyciski wydarzen do rozmiaru okna
	 */
	private void resize() {
		if (buttonList.size() > 0)
			buttonList.get(0).setBounds(5, globalY + 45, frame.getWidth() - 47,
					buttonList.get(0).getPreferredSize().height);

		for (int i = 1; i < buttonList.size(); i++)
			buttonList.get(i).setBounds(
					5,
					buttonList.get(i - 1).getY() + buttonList.get(i - 1).getHeight(),
					frame.getWidth() - 47,
					buttonList.get(i).getPreferredSize().height);

		button_Up.setBounds(frame.getWidth() - 40, 5, 20, frame.getHeight() / 2 - 35);
		button_Down.setBounds(frame.getWidth() - 40, frame.getHeight() / 2 - 30, 20, frame.getHeight() / 2 - 35);
		textField_Filter.setBounds(5, 5, frame.getWidth() * 2 / 3, textField_Filter.getPreferredSize().height);
		button_Filter.setBounds(10 + frame.getWidth() * 2 / 3, 4, frame.getWidth() * 1 / 3 - 53, textField_Filter.getPreferredSize().height);
	}

	/**
	 * Metoda zamykajaca okno wydarzen dla dnia
	 */
	public void dispose() {
		frame.dispose();
	}

	/*
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object object = event.getSource();

		if (object instanceof EventButton)
			((EventButton) object).setPressed(!((EventButton) object)
					.isPressed());
		
		else if (object == button_Up) {
			globalY += 100;
			resize();
		}
			
		else if (object == button_Down) {
			globalY -= 100;
			resize();
		}
			
		else if (object == button_Filter) {
			reload(textField_Filter.getText());
		}
			
		else if (object == menu_Add)
			controller.openNewEventWindow();

		else if (object == menu_DeleteSelected) {
			for (int i = 0; i < buttonList.size(); i++)
				if (buttonList.get(i).isPressed()) {
					frame.getContentPane().remove(buttonList.get(i));
					controller.deleteEvent(buttonList.get(i));
					buttonList.remove(i--);
				}

			frame.setSize(frame.getWidth() + 1, frame.getHeight());
			frame.setSize(frame.getWidth() - 1, frame.getHeight());

			JOptionPane.showMessageDialog(frame, "Wydarzenia usuniête.", "",
					JFrame.EXIT_ON_CLOSE);
		}

		else if (object == menu_SelectAll) {
			for (EventButton v : buttonList)
				v.setPressed(true);
		}

		else if (object == menu_UnselectAll) {
			for (EventButton v : buttonList)
				v.setPressed(false);
		}
	}
}
