package Calendar.view;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Calendar.controller.ControllerGUI;

/**
 * Klasa tworzaca okno sluzace do usuwania wydarzen starszych od podanej daty
 */
public class DeleteOldEventsWindow implements ActionListener {
	ControllerGUI controller;
	JFrame frame;
	
	private JComboBox<String>[] comboBox_Start;
	private JButton button_Delete, button_Cancel;
	
	/**
	 * Konstruktor klasy DeleteOldEventsWindow
	 * @param controller Kontroler
	 */
	public DeleteOldEventsWindow(ControllerGUI controller) {
		this.controller = controller;
		
		frame = new JFrame("Usuñ stare wydarzenia");
		frame.setSize(345, 160);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Container pane = frame.getContentPane();
		pane.setLayout(null);
		
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
		label_Time.setBounds(20, 16, label_Time.getPreferredSize().width, label_Time.getPreferredSize().height);
		
		//start
		comboBox_Start = new JComboBox[5];
		comboBox_Start[0] = new JComboBox <String>(dayList);
		comboBox_Start[1] = new JComboBox <String>(monthList);
		comboBox_Start[2] = new JComboBox <String>(yearList);
		comboBox_Start[3] = new JComboBox <String>(hourList);
		comboBox_Start[4] = new JComboBox <String>(minuteList);
		
		for (int i = 0; i < 5; i++) {
			pane.add(comboBox_Start[i]);
			comboBox_Start[i].setBounds(20 + 60 * i, 45, 60, 20);
		}
		
		button_Delete = new JButton("Usuñ");
		button_Delete.setBounds(25, 85, 140, 30);
		button_Delete.addActionListener(this);
		pane.add(button_Delete);
		
		button_Cancel = new JButton("Anuluj");
		button_Cancel.setBounds(175, 85, 140, 30);
		pane.add(button_Cancel);
		button_Cancel.addActionListener(this);
	}

	/*
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object object = event.getSource();
		
		if (object == button_Delete) {
			controller.deleteEventsBefore(LocalDateTime.of(
					Integer.parseInt((String)comboBox_Start[2].getSelectedItem()),
					Integer.parseInt((String)comboBox_Start[1].getSelectedItem()),
					Integer.parseInt((String)comboBox_Start[0].getSelectedItem()),
					Integer.parseInt((String)comboBox_Start[3].getSelectedItem()),
					Integer.parseInt((String)comboBox_Start[4].getSelectedItem()),
					0));
			
			JOptionPane.showMessageDialog(frame, "Wydarzenia zosta³y usuniête.", "", JFrame.EXIT_ON_CLOSE);
		}
			
		else if (object == button_Cancel)
			frame.dispose();
	}
}