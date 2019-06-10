package Calendar.view;

import java.awt.event.*;
import javax.swing.*;
import Calendar.controller.ControllerGUI;

/**
 * Klasa tworzaca ramke i menu glownego okna interfejsu
 */
public class MenuFrame extends JFrame implements ActionListener, WindowListener {
	private static final long serialVersionUID = 1L;
	
	private ControllerGUI controller;
	private CalendarGUI main;
	
	private JMenuBar menuBar;
	private JMenu menu_File, menu_Events, menu_Settings, menu_About;
	
	private JMenuItem menu_FileImport, menu_FileExport,
		menu_EventsAdd, menu_EventsDeleteBefore, menu_EventsShowAll,
		menu_SettingsFullscreen,
		menu_AboutVersion, menu_AboutAuthors;
	
	/**
	 * Konstruktor klasy MenuFrame
	 * @param controller Kontroler
	 * @param main Okno
	 * @param title Tytul okna
	 */
	public MenuFrame(ControllerGUI controller, CalendarGUI main, String title) {
		super(title);
		this.main = main;
		this.controller = controller;
		
		menuBar = new JMenuBar();
		
		menu_File = new JMenu("Plik");
		menuBar.add(menu_File);
		
			menu_FileImport = new JMenuItem("Import");
			menu_FileImport.addActionListener(this);
			menu_File.add(menu_FileImport);
			
			menu_File.addSeparator();
			
			menu_FileExport = new JMenuItem("Eksport");
			menu_FileExport.addActionListener(this);
			menu_File.add(menu_FileExport);
		
		menu_Events = new JMenu("Wydarzenia");
		menuBar.add(menu_Events);
		
			menu_EventsAdd = new JMenuItem("Dodaj wydarzenie");
			menu_EventsAdd.addActionListener(this);
			menu_Events.add(menu_EventsAdd);
			
			menu_Events.addSeparator();
			
			menu_EventsDeleteBefore = new JMenuItem("Usuń wydarzenia przed datą");
			menu_EventsDeleteBefore.addActionListener(this);
			menu_Events.add(menu_EventsDeleteBefore);
			
			menu_Events.addSeparator();
			
			menu_EventsShowAll = new JMenuItem("Poka� wszystkie");
			menu_EventsShowAll.addActionListener(this);
			menu_Events.add(menu_EventsShowAll);
			
		menu_Settings = new JMenu("Ustawienia");
		menuBar.add(menu_Settings);
		
			menu_SettingsFullscreen = new JMenuItem("Pełny ekran");
			menu_SettingsFullscreen.addActionListener(this);
			menu_Settings.add(menu_SettingsFullscreen);
		
		menu_About = new JMenu("O programie");
		menuBar.add(menu_About);
		
			menu_AboutVersion = new JMenuItem("Wersja");
			menu_AboutVersion.addActionListener(this);
			menu_About.add(menu_AboutVersion);
			
			menu_About.addSeparator();
			
			menu_AboutAuthors = new JMenuItem("Autorzy");
			menu_AboutAuthors.addActionListener(this);
			menu_About.add(menu_AboutAuthors);
		
		setJMenuBar(menuBar);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/*
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object object = event.getSource();
		
		if (object == menu_FileImport)
			controller.importFile();
		
		else if (object == menu_FileExport)
			controller.exportFile();
		
		else if (object == menu_EventsAdd)
			controller.openNewEventWindow();
		
		else if (object == menu_EventsDeleteBefore)
			new DeleteOldEventsWindow(controller);
	
		else if (object == menu_EventsShowAll)
			main.setDayWindow(new DayWindow(controller));
		
		else if (object == menu_SettingsFullscreen) {
			if (getExtendedState() != JFrame.MAXIMIZED_BOTH)
				setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
			
		else if (object == menu_AboutVersion)
			JOptionPane.showMessageDialog(this, "Wersja programu: 1.0", "Wersja", JFrame.EXIT_ON_CLOSE);
		
		else if (object == menu_AboutAuthors)
			JOptionPane.showMessageDialog(this, "Autorzy:\nJakub Musiał\nMateusz Kubiak", "Autorzy", JFrame.EXIT_ON_CLOSE);
	}
	
	/*
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent event) {
		controller.stop();
	}

	/*
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent event) {}

	/*
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent event) {}

	/*
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent event) {}

	/*
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent event) {}

	/*
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent event) {}

	/*
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent event) {}
}
