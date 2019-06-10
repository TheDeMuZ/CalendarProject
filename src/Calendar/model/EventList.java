package Calendar.model;

import Calendar.exception.BaseException;
import Calendar.exception.CollisionException;
import Calendar.exception.OperationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import Calendar.database.EventDatabase;

/**
 * Klasa modelu (listy wydarzen)
 */
public class EventList {
	private List<Event> eventList;
	private EventDatabase eventDatabase = null;
	
	public DateTimeFormatter formatter;

	/**
	 * Konstruktor klasy EventList
	 */
	public EventList() {
		super();
		eventList = new ArrayList<Event>();
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		eventDatabase = new EventDatabase();
	}
	
	/**
	 * Metoda ustawiajaca liste wydarzen
	 * @param eventList Lista wydarzen
	 */
	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}
	
	/**
	 * Medota zwracajaca liste wydarzen
	 * @return Lista wydarzen
	 */
	public List<Event> getEventList() {
		return eventList;
	}
	
	/**
	 * Metoda sortujaca liste wydarzen wedlug daty rozpoczecia
	 */
	public void sort() {
		eventList.sort(new Comparator <Event>() {
			@Override
			public int compare(Event arg0, Event arg1) {
				if (arg0.getStart().isAfter(arg1.getStart()))
					return 1;
				
				else if (arg0.getStart().isEqual(arg1.getStart()))
					return 0;
				
				return -1;
			}
		});
	}
	
	/**
	 * Metoda dodajaca wydarzenie
	 * @param e Wydarzenie
	 * @throws CollisionException
	 * @throws BaseException
	 */
	public void addEvent(Event e) throws CollisionException, BaseException {
		if(eventList.contains(e))
			throw new BaseException("Wydarzenie jest ju¿ zapisane w bazie danych.");
		
		for(Event element: eventList) {
			if(e.getStart().isEqual(element.getStart()) && e.getEnd().isEqual(element.getEnd()))
				throw new CollisionException("Wydarzenie ju¿ istnieje");
			
			if(((e.getStart().isAfter(element.getStart()) || e.getStart().isEqual(element.getStart()))
				&& e.getStart().isBefore(element.getEnd()))
				||
				((e.getEnd().isBefore(element.getEnd()) || e.getEnd().isEqual(element.getEnd()))
				&& e.getEnd().isAfter(element.getStart()))
			)
				if (!e.getStart().isEqual(e.getEnd()))
					throw new CollisionException("Wydarzenie nie mo¿e byæ pomiêdzy innymi wydarzeniami");
		}
		eventList.add(e);
		try {
			eventDatabase.createEvent(e);
		} catch (Exception ex) {
			throw new BaseException(ex.toString());
		}
		
	}
	
	/**
	 * Metoda dodajaca wydarzenie
	 * @param name Nazwa
	 * @param place Miejsce
	 * @param description Opis
	 * @param start Tekst daty rozpoczecia
	 * @param end Tekst daty zakonczenia
	 * @param alarm Tekst daty alarmu
	 * @throws CollisionException
	 * @throws BaseException
	 */
	public void addEvent(String name, String place, String description, String start, String end, String alarm) throws CollisionException, BaseException {
		LocalDateTime startDate = null;
		LocalDateTime endDate = null;
		LocalDateTime alarmDate = null;
		if(start != null)
			startDate = LocalDateTime.parse(start, formatter);
		
		if(end != null)
			endDate = LocalDateTime.parse(end, formatter);
		
		if(alarm != null)
			alarmDate = LocalDateTime.parse(alarm, formatter);
		addEvent(new Event(name, place, description, startDate, endDate, alarmDate));
		
	}
	
	/**
	 * Metoda dodajaca wydarzenie
	 * @param name Nazwa
	 * @param place Miejsce
	 * @param description Opis
	 * @param start Data rozpoczecia
	 * @param end Data zakonczenia
	 * @param alarm Data alarmu
	 * @throws CollisionException
	 * @throws BaseException
	 */
	public void addEvent(String name, String place, String description, LocalDateTime start, LocalDateTime end, LocalDateTime alarm) throws CollisionException, BaseException {
		addEvent(new Event(name, place, description, start, end, alarm));
		
	}
	
	/**
	 * Metoda usuwajaca wydarzenie
	 * @param event Wydarzenie
	 * @throws BaseException
	 */
	public void deleteEvent(Event event) throws BaseException {
		if(eventList.contains(event)) {
			eventList.remove(event);
			try {
				eventDatabase.deleteEvent(event);
			} catch (Exception e) {
				throw new BaseException(e.toString());
			}
		}
		else 
			throw new BaseException("Wydarzenie nie istnieje.");
	}
	
	/**
	 * Metoda szukajaca wydarzenia po nazwie
	 * @param name Nazwa
	 * @return Szukane wydarzenie
	 */
	public Event searchEvent(String name) {
		for(int i = 0; i < eventList.size(); i++)
			if(eventList.get(i).getName().equals(name))
				return eventList.get(i);
		return null;
	}
	
	/**
	 * Metoda szukajaca wydarzenia po jego dacie rozpoczecia
	 * @param StartData Data rozpoczenia
	 * @return Szukane wydarzenie
	 */
	public Event searchEventStartData(String StartData) {
		for(int i = 0; i < eventList.size(); i++)
			if(eventList.get(i).getStart().isEqual(LocalDateTime.parse(StartData, formatter)))
				return eventList.get(i);
		return null;
	}
	
	/**
	 * Metoda szukajaca wydarzenia po jego dacie alarmu
	 * @param AlarmData Data alarmu
	 * @return Szukane wydarzenie
	 */
	public Event searchEventAlarm(String AlarmData) {
		for(int i = 0; i < eventList.size(); i++)
			if(eventList.get(i).getAlarm().isEqual(LocalDateTime.parse(AlarmData, formatter)))
				return eventList.get(i);
		return null;
	}
	
	/**
	 * Metoda usuwajaca alarm
	 * @param e Wydarzenie
	 */
	public void deleteAlarm(Event e) {
		e.setAlarm(null);
	}
	
	/**
	 * Metoda usuwajaca wydarzenia starsze niz podana data
	 * @param data Podana data
	 * @throws BaseException
	 */
	public void deleteBefore(LocalDateTime data) throws BaseException {
		List<Event> tmp = new ArrayList<Event>();
		for (int i = 0; i < eventList.size(); i++)
			if (eventList.get(i).getEnd().isBefore(data)) {
				tmp.add(eventList.get(i));
				deleteEvent(eventList.get(i--));
			}
		
		for(Event element: tmp) {
			try {
				eventDatabase.deleteEvent(element);
			} catch (Exception e) {
				throw new BaseException(e.toString());
			}
		}
				
	}
	
	/**
	 * Metoda filtrujaca wydarzenie po dniu
	 * @param date Data
	 * @return Szukane wydarzenia
	 * @throws CollisionException
	 * @throws BaseException
	 */
	public List<Event> filterByDay(LocalDateTime date) throws CollisionException, BaseException{
		List<Event> filteredList = new ArrayList<Event>();

		for(Event v : eventList)
			if(v.getStart().getDayOfMonth() == date.getDayOfMonth() && v.getStart().getMonth() == date.getMonth() && v.getStart().getYear() == date.getYear())
				filteredList.add(v);
			else if(v.getEnd().getDayOfMonth() == date.getDayOfMonth() && v.getEnd().getMonth() == date.getMonth() && v.getEnd().getYear() == date.getYear())
				filteredList.add(v);
		
		return filteredList;
	}
	
	/**
	 * Metoda filtrujaca wydarzenie po dniu
	 * @param textDate Tekst daty
	 * @return Szukane wydarzenia
	 * @throws CollisionException
	 * @throws BaseException
	 */
	public List<Event> filterByDay(String textDate) throws CollisionException, BaseException{
		LocalDateTime date = null;
		try {
			date = LocalDateTime.parse(textDate, formatter);
		}
		catch(DateTimeParseException e) {
			textDate += " 00:00";
		}
		try {
			date = LocalDateTime.parse(textDate, formatter);
		}
		catch(DateTimeParseException e) {
			throw new DateTimeParseException(textDate, textDate, 0);
		}
		
		List<Event> filteredList = new ArrayList<Event>();

		for(Event v : eventList)
			if(v.getStart().getDayOfMonth() == date.getDayOfMonth() && v.getStart().getMonth() == date.getMonth() && v.getStart().getYear() == date.getYear())
				filteredList.add(v);
			else if(v.getEnd().getDayOfMonth() == date.getDayOfMonth() && v.getEnd().getMonth() == date.getMonth() && v.getEnd().getYear() == date.getYear())
				filteredList.add(v);
		
		return filteredList;
	}
	
	/**
	 * Metoda zwracaja wydarzenia pomiedzy okreslonymi datami
	 * @param from Data minimalna
	 * @param to Data maksymalna
	 * @return Szukane wydarzenia
	 */
	public List<Event> filterPeriod(String from, String to){
		LocalDateTime dateFrom = null;
		LocalDateTime dateTo = null;
		try {
			dateFrom = LocalDateTime.parse(from, formatter);
		}
		catch(DateTimeParseException e) {
			from += " 00:00";
		}
		try {
			dateFrom = LocalDateTime.parse(from, formatter);
		}
		catch(DateTimeParseException e) {
			throw new DateTimeParseException(from, from, 0);
		}
		
		try {
			dateTo = LocalDateTime.parse(to, formatter);
		}
		catch(DateTimeParseException e) {
			to += " 00:00";
		}
		try {
			dateTo = LocalDateTime.parse(to, formatter);
		}
		catch(DateTimeParseException e) {
			throw new DateTimeParseException(to, to, 0);
		}
		
		
		List<Event> filteredList = new ArrayList<Event>();
		for(Event v : eventList) {
			if(v.getStart().isAfter(dateFrom) && v.getEnd().isBefore(dateTo))
				filteredList.add(v);
		}
		
		return filteredList;
	}

	/**
	 * Metoda szukajaca wydarzen po wzorze
	 * @param pattern wzor
	 * @return Szukane wydarzenia
	 * @throws CollisionException
	 * @throws BaseException
	 */
	public EventList filterByPattern(String pattern) throws CollisionException, BaseException {
		List <Event> filteredList = new ArrayList <>();
		
		for (Event v : eventList) {
			if ((v.getName() != null && v.getName().contains(pattern))
					||
					(v.getPlace() != null && v.getPlace().contains(pattern))
					||
					(v.getDescription() != null && v.getDescription().contains(pattern)))
				filteredList.add(v);
		}
		
		EventList tmp = new EventList();
		tmp.setEventList(filteredList);
		
		return tmp;
	}
	
	/**
	 * Metoda czyszczaca liste wydarzen
	 */
	public void clearList(){
		eventList.clear();
	}
	
	/**
	 * Metoda pobierajaca dane z bazy do modelu
	 * @throws BaseException
	 */
	public void getListFromDatabase() throws BaseException {
		try {
			eventList =  eventDatabase.getAllEvents();
		} catch (Exception ex) {
			throw new BaseException(ex.getMessage());
		}
	}
	
	/**
	 * Metoda dodajaca brakujace wydarzenia do bazy
	 * @throws BaseException
	 */
	public void addOtherEventsToDatabase() throws BaseException {
		try {
			eventDatabase.addOtherEvents(getEventList());
		} catch (Exception e) {
			throw new BaseException("Nie uda³o siê zaktualizowaæ zmian w bazie danych.");
		}
	}
	
	/**
	 * Metoda zapisuja model do pliku XML
	 * @param path Sciezka pliku
	 * @throws OperationException
	 */
	public void saveXMLFile(String path) throws OperationException {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			Element rootElement = doc.createElement("events");
			doc.appendChild(rootElement);
			for(Event el: eventList) {
				Element eventElement = doc.createElement("event");
				rootElement.appendChild(eventElement);
				
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(el.getName()));
				eventElement.appendChild(name);
				
				Element placeEl = doc.createElement("place");
				if(el.getPlace()== null)
					placeEl.appendChild(doc.createTextNode(""));
				else
					placeEl.appendChild(doc.createTextNode(el.getPlace()));
				eventElement.appendChild(placeEl);
				
				Element descriptionEl = doc.createElement("description");
				if(el.getDescription()== null)
					descriptionEl.appendChild(doc.createTextNode(""));
				else
					descriptionEl.appendChild(doc.createTextNode(el.getDescription()));
				eventElement.appendChild(descriptionEl);
				
				Element startEl = doc.createElement("start");
				startEl.appendChild(doc.createTextNode(el.getStart().format(formatter).toString()));
				eventElement.appendChild(startEl);
				
				Element endEl = doc.createElement("end");
				endEl.appendChild(doc.createTextNode(el.getEnd().format(formatter).toString()));
				eventElement.appendChild(endEl);
				
				Element alarmEl = doc.createElement("alarm");
				if(el.getAlarm()== null)
					alarmEl.appendChild(doc.createTextNode(""));
				else
					alarmEl.appendChild(doc.createTextNode(el.getAlarm().format(formatter).toString()));
				eventElement.appendChild(alarmEl);
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(new File(path));
	        transformer.transform(source, result);
			
		}
		
		catch (Exception e) {
			throw new OperationException("Wyst¹pi³ b³¹d podczas zapisu pliku XML");
		}
	}
	
	/**
	 * Metoda wczytujaca dane do modelu z pliku XML
	 * @param path Sciezka pliku
	 * @throws BaseException
	 * @throws CollisionException
	 * @throws OperationException
	 */
	public void loadXMLFile(String path) throws BaseException, CollisionException, OperationException {
		clearList();
		try {
			File inputFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    Document doc = dBuilder.parse(inputFile);
		    doc.getDocumentElement().normalize();
		    NodeList nList = doc.getElementsByTagName("event");
		    for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String name = eElement.getElementsByTagName("name").item(0).getTextContent();
					String place = eElement.getElementsByTagName("place").item(0).getTextContent();
					String description = eElement.getElementsByTagName("description").item(0).getTextContent();
					String start = eElement.getElementsByTagName("start").item(0).getTextContent();
					String end = eElement.getElementsByTagName("end").item(0).getTextContent();
					String alarm = eElement.getElementsByTagName("alarm").item(0).getTextContent();
					if(place.length() == 0)
						place = null;
					if(description.length() == 0)
						description = null;
					if(alarm.length() == 0)
						alarm = null;
					addEvent(name, place, description, start, end, alarm);
					
				}
		    }
			
		} catch (BaseException e) {
			throw new BaseException(e.toString());
		} catch (CollisionException e) {
			throw new CollisionException(e.toString());
		} catch (Exception e) {
			throw new OperationException("Wyst¹pi³ b³¹d podczas wczytania pliku XML");
		}
		
	}

	/**
	 * Metoda zapisujaca model do pliku CSV
	 * @param path Sciezka pliku
	 * @throws OperationException
	 */
	public void exportCSV(String path) throws OperationException {
		BufferedWriter writer = null;	
		try {
			writer = new BufferedWriter( new FileWriter(path));

			String head = "\"Subject\",\"Start Date\",\"Start Time\",\"End Date\",\"End Time\",\"Reminder on/off\",\"Reminder Date\",\"Reminder Time\",\"Description\",\"Location\"\n";
			writer.write(head);
//			"A coœ","2.6.2019","04:00:00","2.6.2019","04:30:00","False","2.6.2019","03:45:00","","Kutno"
			for(Event el: eventList) {
				String obj = "\""+ el.getName()+"\",";
				obj += "\""+ el.getStart().getDayOfMonth()+"." +el.getStart().getMonthValue()+"."+el.getStart().getYear()+ "\",";
				obj += "\""+ el.getStart().getHour()+":"+ el.getStart().getMinute()+":"+ el.getStart().getSecond()+"\",";
				obj += "\""+ el.getEnd().getDayOfMonth()+"." +el.getEnd().getMonthValue()+"."+el.getEnd().getYear()+ "\",";
				obj += "\""+ el.getEnd().getHour()+":"+ el.getEnd().getMinute()+":"+ el.getEnd().getSecond()+"\",";
				if(el.getAlarm() == null) {
					obj += "\"False\"" + ",";
					obj += ",";
					obj += ",";
				}
				else {
					obj += "\"True\"" + ",";
					obj += "\""+ el.getAlarm().getDayOfMonth()+"." +el.getAlarm().getMonthValue()+"."+el.getAlarm().getYear()+ "\",";
					obj += "\""+ el.getAlarm().getHour()+":"+ el.getAlarm().getMinute()+":"+ el.getAlarm().getSecond()+"\",";
				}
				if(el.getDescription() == null)
					obj += "\"\",";
				else
					obj += "\""+el.getDescription()+"\"";
				if(el.getPlace() == null)
					obj += "\"\",";
				else
					obj += "\""+el.getPlace()+"\"";
				obj += "\n";
				writer.write(obj);
			}
			writer.close();
			
		}
		catch (IOException e) {
			throw new OperationException(e.toString());
		}
		catch (Exception e) {
			throw new OperationException("Wyst¹pi³ b³¹d podczas zapisu pliku CSV");
		}
		
	}
	
	/**
	 * Metoda naprawiajaca uszkodzone lancuchy daty
	 * @param date
	 * @return
	 */
	private String repairDate(String date) {
		if(date == null)
			return null;
		
		if(date.length() == 0)
			return null;
		String[] splited = date.split("\\s+");
		String[] splited_date = splited[0].split("\\.");
		String[] splited_time = splited[1].split("\\:");
		String day = null;
		String month = null;
		String year = null;
		String hour = null;
		String minute = null;
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		
		if(splited_date[0].length() < 2)
			day = "0" + splited_date[0];
		else
			day = splited_date[0];
		
		if(splited_date[1].length() < 2)
			month = "0" + splited_date[1];
		else
			month = splited_date[1];
		
		year = splited_date[2];
		
		if(splited_time[0].length() < 2)
			hour = "0" + splited_time[0];
		else
			hour = splited_time[0];
		
		if(splited_time[1].length() < 2)
			minute = "0" + splited_time[1];
		else
			minute = splited_time[1];
		return year + "-" + month + "-" + day + " " + hour + ":" + minute;
	}
	
	/**
	 * Metoda wczytujaca dane do modelu z pliku CSV
	 * @param path Sciezka pliku
	 * @throws OperationException
	 */
	public void importCSV(String path) throws OperationException{
		String allLine = "";

		try {
			FileReader fileReader = new FileReader(path);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String textLine = bufferedReader.readLine();
			boolean checkHeader = false;
			do {
				if(checkHeader)
					allLine+= textLine;
				else 
					checkHeader = true;
			    textLine = bufferedReader.readLine();
			   
			} 
			while(textLine != null);
			
			bufferedReader.close();
		}
		catch(IOException e) {
			throw new OperationException("Wyst¹pi³ b³¹d podczas wczytywania pliku CSV");
		}
		List<String> parts = Arrays.asList(allLine.split("\\\""));
		List<String> parts2 = new ArrayList<String>();
		int i=0;
		for(String el: parts) 
			if(!el.equals(new String(","))) {
				if(i!=11)
					parts2.add(el);
				else
					i=0;
				i++;
			}
		parts2.remove(0);

		try {
			for(int l= 0; l <parts2.size()/10; l++) {
				String name = null;
				String start = null;
				String end = null;
				String alarm = null;
				String description = null;
				String place = null;
				for(int k = 0; k < 10; k++) {
					if(k==0)
						name = parts2.get(l*10 +k);
					else if(k==1)
						start = parts2.get(l*10 +k);
					else if(k==2)
						start +=" " + parts2.get(l*10 +k);
					else if(k==3)
						end = parts2.get(l*10 +k);
					else if(k==4)
						end +=" " + parts2.get(l*10 +k);
					else if(k==5) {
						if(parts2.get(l*10 +k).equals(new String("True"))) {
							alarm = parts2.get(l*10 +(k+1));
							alarm += " " + parts2.get(l*10 +(k+2));
						}
					}
					else if(k==8) {
						if(!parts2.get(l*10 +k).isEmpty())
							description = parts2.get(l*10 +k);
					}
					else if(k==9) {
						if(!parts2.get(l*10 +k).isEmpty())
							place = parts2.get(l*10 +k);
					}
				}
				addEvent(name, place, description, this.repairDate(start), this.repairDate(end), this.repairDate(alarm));

			}
		}catch (Exception e) {
			throw new OperationException(e.toString());
		}
	}
}