package Calendar.database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import Calendar.model.Event;

/**
 * Klasa zarzadzajaca baza danych
 */
public class EventDatabase extends SQLData {
	public DateTimeFormatter formatter;
	
	/**
	 * Kontruktor klasy EventDatabase
	 */
	public EventDatabase() {
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	}
	
	/**
	 * Metoda dodajaca wydarzenie do bazy
	 * @param event Wydarzenie
	 * @throws Exception
	 */
	public void createEvent(Event event) throws Exception {
		if (event != null) {
			connect();
			String query = "INSERT INTO event_list (event_name, event_place, event_description, event_start, event_end, event_alarm) values (?, ?, ?, ?, ?, ?)";
			try (PreparedStatement ps = con.prepareStatement(query)) {
				 ps.setString(1, event.getName());
				 ps.setString(2, event.getPlace());
				 ps.setString(3, event.getDescription());
				 ps.setTimestamp(4, Timestamp.valueOf(event.getStart()));
				 ps.setTimestamp(5, Timestamp.valueOf(event.getEnd()));
				 if(event.getAlarm() == null)
					 ps.setNull(6, java.sql.Types.TIMESTAMP);
				 else
					ps.setTimestamp(6, Timestamp.valueOf(event.getAlarm()));
				 ps.executeUpdate();
			} catch (SQLException e) {
				throw new Exception("Wyst¹pi³ b³¹d podczas dodawania wydarzenia do bazy danych.");
            }
			disconnect();
		}
	}
	
	/**
	 * Metoda usuwajaca wydarzenie z bazy
	 * @param event Wydarzenie
	 * @throws Exception
	 */
	public void deleteEvent(Event event) throws Exception
	{
		if (event != null) {
			connect();
			String query ="DELETE FROM event_list WHERE event_name = ? AND event_start = ? AND event_end = ?";
			try (PreparedStatement ps = con.prepareStatement(query)) {
				ps.setString(1, event.getName());
				ps.setTimestamp(2, Timestamp.valueOf(event.getStart()));
				ps.setTimestamp(3, Timestamp.valueOf(event.getEnd()));
				ps.executeUpdate();
			} catch (SQLException e) {
				throw new Exception("Wyst¹pi³ b³¹d podczas usuwania wydarzenia z bazy danych.");
          }
			disconnect();
		}
		
	}
	
	/**
	 * Metoda usuwajaca wszystkie wydarzenia
	 * @throws Exception
	 */
	public void deleteAllEvents() throws Exception {
		connect();
		String query = "DELETE FROM event_list";
		try (PreparedStatement ps = con.prepareStatement(query)) {
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new Exception("Wyst¹pi³ b³¹d podczas usuwania wydarzenia z bazy danych.");
        }
		disconnect();
	}
	
	/**
	 * Metoda zwracajaca wszystkie wydarzenia
	 * @return Wszystkie wydarzenia
	 * @throws Exception
	 */
	public List<Event> getAllEvents() throws Exception {
		List<Event> eventList = new ArrayList<Event>();
		connect();
		String query = "SELECT event_name, event_place, event_description, event_start, event_end, event_alarm FROM event_list";		
		try {
			stmt = con.createStatement();  
	        rs = stmt.executeQuery(query); 
	        
	        while (rs.next()) {
	        	String name = rs.getString(1);
	        	String place = rs.getString(2);
	        	String description = rs.getString(3);
	        	LocalDateTime start = Timestamp.valueOf(rs.getString(4)).toLocalDateTime();
	        	LocalDateTime end = Timestamp.valueOf(rs.getString(5)).toLocalDateTime();
	        	LocalDateTime alarm = null;
	        	if(rs.getString(6) != null)
	        		alarm = Timestamp.valueOf(rs.getString(6)).toLocalDateTime();
	        	eventList.add(new Event(name, place, description, start, end, alarm));
	        }
		} catch (Exception e) {
			throw new Exception("Wyst¹pi³ b³¹d podczas pobierania informacji z bazy danych." + e.getClass());
        }
		
		disconnect();
		return eventList;
	}
	
	/**
	 * Metoda dodajaca do bazy brakuj¹ce wydarzenia z modelu
	 * @param events Wydarzenie
	 * @throws Exception
	 */
	public void addOtherEvents(List<Event> events) throws Exception {
		connect();
		List<Event> eventsInDatabase =  getAllEvents();
		
		for (Event elDatabase: eventsInDatabase) {
			for(int i = 0; i < events.size(); i++) {
				if (elDatabase.equals(events.get(i))) {
					events.remove(events.get(i--));
				}
			}
		}
			
		for(Event el: events)
			createEvent(el);
		
		disconnect();
	}
	
}