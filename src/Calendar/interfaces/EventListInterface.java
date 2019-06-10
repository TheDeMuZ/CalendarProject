package Calendar.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import Calendar.exception.CollisionException;
import Calendar.exception.BaseException;
import Calendar.model.Event;

/**
 * Interfejs modelu (listy wydarzen)
 */
public interface EventListInterface {
	/**
	 * Metoda dodajaca wydarzenie do modelu
	 * @param e Wydarzenie
	 * @throws CollisionException
	 * @throws BaseException
	 */
	public void addEventObject(Event e) throws CollisionException, BaseException;
	
	/**
	 * Metoda usuwajaca wydarzenie z modelu
	 * @param e Wydarzenie
	 * @throws BaseException
	 */
	public void deleteEvent(Event e) throws BaseException;
	
	/**
	 * Metoda zwracajaca liste wydarzen
	 * @return Lista wydarzen
	 */
	public List<Event> getAllEvent();
	
	/**
	 * Metoda usuwajaca wydarzenia starsze od podanej daty
	 * @param data Wybrana data
	 * @throws BaseException
	 */
	public void deleteBefore(LocalDateTime data) throws BaseException;
	
	/**
	 * Metoda usuwajaca wydarzenia starsze od podanej daty
	 * @param data Tekst daty
	 * @throws BaseException
	 */
	public void deleteBeforString(String data) throws BaseException;
	
	/**
	 * Metoda usuwajaca wydarzenie z modelu
	 * @param name Nazwa
	 * @param place Miejsce
	 * @param description Opis
	 * @param start Data rozpoczecia
	 * @param end Data zakonczenia
	 * @param alarm Data alarmu
	 * @throws BaseException
	 */
	public void deleteEventString(String name, String place, String description, String start, String end, String alarm) throws BaseException;
	
	/**
	 * Metoda dodajaca wydarzenie do modelu
	 * @param name Nazwa
	 * @param place Miejsce
	 * @param description Opis
	 * @param start Data rozpoczecia
	 * @param end Data zakonczenia
	 * @param alarm Data alarmu
	 * @throws CollisionException
	 * @throws BaseException
	 */
	public void addEventString(String name, String place, String description, String start, String end, String alarm) throws CollisionException, BaseException;
}
