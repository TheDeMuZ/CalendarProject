package Calendar.view;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import Calendar.controller.ControllerGUI;

/**
 * Klasa tworzaca okno dodawania nowych wydarzen
 */
public class AddEventWindow implements ActionListener {
	private ControllerGUI controller;
	private JFrame frame;
	
	private String name, place, description, start, end, alarm;
	
	private JButton button_Add, button_Cancel;
	private JRadioButton radioButton_Alarm;
	private JTextField textField_Name, textField_Place, textField_Description;
	private JComboBox[] comboBox_Start, comboBox_End, comboBox_Alarm;
	
	/**
	 * Kontruktor klasy AddEventWindow
	 * @param controller Kontroler
	 */
	public AddEventWindow(final ControllerGUI controller) {
		this.controller = controller;
		this.alarm = null;
		
		comboBox_Start = new JComboBox[5];
		comboBox_End = new JComboBox[5];
		comboBox_Alarm = new JComboBox[5];
		
		frame = new JFrame("Nowe wydarzenie");
		frame.setSize(700, 300);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				controller.setAddEventWindowOpenVariable(false);
			}
		});
		
		Container pane = frame.getContentPane();
		pane.setLayout(null);
		
		button_Add = new JButton("Dodaj");
		button_Add.addActionListener(this);
		pane.add(button_Add);
		button_Add.setBounds(35, 200, 220, 30);
		
		button_Cancel = new JButton("Anuluj");
		button_Cancel.addActionListener(this);
		pane.add(button_Cancel);
		button_Cancel.setBounds(260, 200, 220, 30);
		
		//nazwa
		JLabel label_Name = new JLabel("Nazwa:");
		pane.add(label_Name);
		label_Name.setBounds(15, 46, label_Name.getPreferredSize().width, label_Name.getPreferredSize().height);
		
		textField_Name = new JTextField();
		pane.add(textField_Name);
		textField_Name.setBounds(68, 45, 200, textField_Name.getPreferredSize().height);
		
		//miejsce
		JLabel label_Place = new JLabel("Miejsce:");
		pane.add(label_Place);
		label_Place.setBounds(15, 86, label_Place.getPreferredSize().width, label_Place.getPreferredSize().height);
		
		textField_Place = new JTextField();
		pane.add(textField_Place);
		textField_Place.setBounds(68, 85, 200, textField_Place.getPreferredSize().height);
		
		//opis
		JLabel label_Description = new JLabel("Opis:");
		pane.add(label_Description);
		label_Description.setBounds(15, 126, label_Description.getPreferredSize().width, label_Description.getPreferredSize().height);
		
		textField_Description = new JTextField();
		pane.add(textField_Description);
		textField_Description.setBounds(68, 125, 200, textField_Description.getPreferredSize().height);
		
		//start, end, alarm
		String[] dayList = new String[31];
		
		for (int i = 0; i < 31; i++)
			dayList[i] = String.format("%02d", i + 1);
		
		String[] monthList = new String[12];
		
		for (int i = 0; i < 12; i++)
			monthList[i] = String.format("%02d", i + 1);
		
		String[] yearList = new String[51];
		
		for (int i = 0; i <= 50; i++)
			yearList[i] = Integer.toString(LocalDateTime.now().getYear() - 25 + i);
		
		String[] hourList = new String[24];
		
		for (int i = 0; i < 24; i++)
			hourList[i] = String.format("%02d", i);
		
		String[] minuteList = new String[60];
		
		for (int i = 0; i < 60; i++)
			minuteList[i] = String.format("%02d", i);
		
		JLabel label_Time = new JLabel("Dzieñ    Miesi¹c     Rok             Godzina");
		pane.add(label_Time);
		label_Time.setBounds(360, 16, label_Time.getPreferredSize().width, label_Time.getPreferredSize().height);
		
		//start
		JLabel label_Start = new JLabel("Start:");
		pane.add(label_Start);
		label_Start.setBounds(310, 46, label_Start.getPreferredSize().width, label_Start.getPreferredSize().height);
		
		comboBox_Start[0] = new JComboBox <String>(dayList);
		comboBox_Start[1] = new JComboBox <String>(monthList);
		comboBox_Start[2] = new JComboBox <String>(yearList);
		comboBox_Start[3] = new JComboBox <String>(hourList);
		comboBox_Start[4] = new JComboBox <String>(minuteList);
		
		for (int i = 0; i < 5; i++) {
			pane.add(comboBox_Start[i]);
			comboBox_Start[i].setBounds(360 + 60 * i, 45, 60, 20);
		}
		
		//end
		JLabel label_End = new JLabel("Koniec:");
		pane.add(label_End);
		label_End.setBounds(310, 86, label_End.getPreferredSize().width, label_End.getPreferredSize().height);
		
		comboBox_End[0] = new JComboBox <String>(dayList);
		comboBox_End[1] = new JComboBox <String>(monthList);
		comboBox_End[2] = new JComboBox <String>(yearList);
		comboBox_End[3] = new JComboBox <String>(hourList);
		comboBox_End[4] = new JComboBox <String>(minuteList);
		
		for (int i = 0; i < 5; i++) {
			pane.add(comboBox_End[i]);
			comboBox_End[i].setBounds(360 + 60 * i, 85, 60, 20);
		}
		
		//alarm
		JLabel label_AlarmButton = new JLabel("alarm w³¹czony");
		pane.add(label_AlarmButton);
		label_AlarmButton.setBounds(565, 206, label_AlarmButton.getPreferredSize().width, label_AlarmButton.getPreferredSize().height);
		
		radioButton_Alarm = new JRadioButton();
		radioButton_Alarm.setBounds(545, 205, radioButton_Alarm.getPreferredSize().width, radioButton_Alarm.getPreferredSize().height);
		pane.add(radioButton_Alarm);
		
		JLabel label_Alarm = new JLabel("Alarm:");
		pane.add(label_Alarm);
		label_Alarm.setBounds(310, 126, label_Alarm.getPreferredSize().width, label_Alarm.getPreferredSize().height);
		
		comboBox_Alarm[0] = new JComboBox <String>(dayList);
		comboBox_Alarm[1] = new JComboBox <String>(monthList);
		comboBox_Alarm[2] = new JComboBox <String>(yearList);
		comboBox_Alarm[3] = new JComboBox <String>(hourList);
		comboBox_Alarm[4] = new JComboBox <String>(minuteList);
		
		for (int i = 0; i < 5; i++) {
			pane.add(comboBox_Alarm[i]);
			comboBox_Alarm[i].setBounds(360 + 60 * i, 125, 60, 20);
		}
	}
	
	/*
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object object = event.getSource();
		
		if (object == button_Add) {
			name = textField_Name.getText();
			place = textField_Place.getText();
			description = textField_Description.getText();
			start = (String)comboBox_Start[2].getSelectedItem() + "-" + (String)comboBox_Start[1].getSelectedItem() + "-" + (String)comboBox_Start[0].getSelectedItem() + " " + (String)comboBox_Start[3].getSelectedItem() + ":" + (String)comboBox_Start[4].getSelectedItem();
			end = (String)comboBox_End[2].getSelectedItem() + "-" + (String)comboBox_End[1].getSelectedItem() + "-" + (String)comboBox_End[0].getSelectedItem() + " " + (String)comboBox_End[3].getSelectedItem() + ":" +  (String)comboBox_End[4].getSelectedItem();
			
			if (radioButton_Alarm.isSelected())
				alarm = (String)comboBox_Alarm[2].getSelectedItem() + "-" + (String)comboBox_Alarm[1].getSelectedItem() + "-" + (String)comboBox_Alarm[0].getSelectedItem() + " " + (String)comboBox_Alarm[3].getSelectedItem() + ":" + (String)comboBox_Alarm[4].getSelectedItem();
				
			if (place.equals(new String("")))
				place = null;
				
			if (description.equals(new String("")))
				description = null;
				
			if (name.length() > 0 && start.length() > 0 && end.length() > 0)
				controller.addEvent(this);
		}
		
		else if (object == button_Cancel) {
			controller.setAddEventWindowOpenVariable(false);
			frame.dispose();
		}
	}
	
	/**
	 * Metoda zwracajaca nazwe wydarzenia do stworzenia
	 * @return Nazwa
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Metoda zwracajaca miejsce wydarzenia do stworzenia
	 * @return Miejsce
	 */
	public String getPlace() {
		return place;
	}
	
	/**
	 * Metoda zwracajaca opis wydarzenia do stworzenia
	 * @return Opis
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Metoda zwracajaca date rozpoczenia wydarzenia do stworzenia
	 * @return Data rozpoczecia
	 */
	public String getStart() {
		return start;
	}

	/**
	 * Metoda zwracajaca date zakonczenia wydarzenia do stworzenia
	 * @return Data zakonczenia
	 */
	public String getEnd() {
		return end;
	}
	
	/**
	 * Metoda zwracajaca date alarmu wydarzenia do stworzenia
	 * @return Data alarmu
	 */
	public String getAlarm() {
		return alarm;
	}
}