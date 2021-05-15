package polito.it.noleggio.model;

import java.time.LocalTime;

public class Event implements Comparable<Event>{
	
	public enum EventType{ //classi che definiscono costanti
		//sono entrambe delle costanti che assumono il valore indicato
		NUOVO_CLIENTE, //=1
		RITORNO_AUTO //=2
	}
	
	private LocalTime time; //a quale istante di tempo si rifersice questo evento
	private EventType type; //assume val. 1 o 2
	
	public Event(LocalTime time, EventType type) {
		super();
		this.time = time;
		this.type = type;
	}
	
	@Override
	public int compareTo(Event other) {
		return this.time.compareTo(other.time); //lo fa per tempo crescente: priorita' piu' alta avra' t minore
	}

	@Override
	public String toString() {
		return "Event [time=" + time + ", type=" + type + "]";
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}
	
	
	
}
