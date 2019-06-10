package Calendar.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Calendar.exception.BaseException;

/**
 * Klasa wydarzenia
 */
public class Event implements Comparable<Event> {

	private String name;
	private String place;
	private String description;
	
	private LocalDateTime start;
	private LocalDateTime end;
	private LocalDateTime alarm;
	
	/**
	 * Kontruktor klasy Event
	 * @param name Nazwa
	 * @param place Miejsce
	 * @param description Opis
	 * @param start Data rozpoczecia
	 * @param end Data zakonczenia
	 * @param alarm Data alarmu
	 * @throws BaseException
	 */
	public Event(String name, String place, String description, LocalDateTime start, LocalDateTime end, LocalDateTime alarm) throws BaseException {
		super();
		
		if (name.isEmpty() || name == null)
			throw new BaseException("Pusta nazwa wydarzenia");
		
		if (start == null)
			throw new BaseException("Musisz podaæ start wydarzenia");
		
		if (end == null)
			throw new BaseException("Musisz podaæ koniec wydarzenia");
		
		if(start.isAfter(end))
			throw new BaseException("Start wydarzenia musi zaczynaæ sie wczeœniej, ni¿ koniec");
		
		if(alarm != null && start.isBefore(alarm))
			throw new BaseException("Przypomnienie musi zaczynac siê przed wydarzeniem");
		
		this.name = name;
		this.place = place;
		this.description = description;
		this.start = start;
		this.end = end;
		this.alarm = alarm;
	}

	/**
	 * Metoda zwracajaca nazwe wydarzenia
	 * @return Nazwa wydarzenia
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Metoda ustawiajaca nazwê wydarzenia
	 * @param name Nazwa
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Metoda zwracajaca miejsce wydarzenia
	 * @return Miejsce
	 */
	public String getPlace() {
		return place;
	}
	
	/**
	 * Metoda ustawiajaca miejsce wydarzenia
	 * @param place Miejsce
	 */
	public void setPlace(String place) {
		this.place = place;
	}
	
	/**
	 * Metoda zwracajaca opis wydarzenia
	 * @return Opis
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Metoda ustawiajaca opis wydarzenia
	 * @param description Opis
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Metoda zwracajaca date rozpoczecia wydarzenia
	 * @return Data rozpoczenia
	 */
	public LocalDateTime getStart() {
		return start;
	}
	
	/**
	 * Metoda ustawiajaca date rozpoczenia wydarzenia
	 * @param start Data rozpoczenia
	 */
	public void setStart(LocalDateTime start) {
		this.start = start;
	}
	
	/**
	 *  Metoda zwracajaca date zakonczenia wydarzenia
	 * @return Data zakonczenia
	 */
	public LocalDateTime getEnd() {
		return end;
	}
	
	/**
	 * Metoda ustawiajaca date zakonczenia wydarzenia
	 * @param end Data zakonczenia
	 */
	public void setEnd(LocalDateTime end) {
		this.end = end;
	}
	
	/**
	 * Metoda zwracajaca date alarmu wydarzenia
	 * @return Data alarmu
	 */
	public LocalDateTime getAlarm() {
		return alarm;
	}
	
	/**
	 * Metoda ustawiajaca date alarmu wydarzenia
	 * @param alarm Data alarmu
	 */
	public void setAlarm(LocalDateTime alarm) {
		this.alarm = alarm;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String tmp = "Nazwa wydarzenia: " + getName();
		
		if(getPlace() == null)
			tmp += "\nMiejsce: brak";
		else
			tmp += "\nMiejsce: " + getPlace();
		
		if(getDescription() == null)
			tmp += "\nOpis: brak";
		else 
			tmp += "\nOpis: " + getDescription();
		
		tmp += "\nData rozpoczêcia: " + getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toString();
		tmp += "\nData zakoñczenia: " + getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toString();
		
		if(getAlarm() == null)
			tmp += "\nAlarm: brak";
		else
			tmp += "\nAlarm: " + getAlarm().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toString();
		
		tmp += "\n";
		return tmp;
	}

	/*
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Event e) {
		if(this.start.isEqual(e.start)) return 0;
		if(this.start.isAfter(e.start)) return 1;
		return -1;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alarm == null) ? 0 : alarm.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((place == null) ? 0 : place.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (alarm == null) {
			if (other.alarm != null)
				return false;
		} else if (!alarm.equals(other.alarm))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (place == null) {
			if (other.place != null)
				return false;
		} else if (!place.equals(other.place))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}
	
}
