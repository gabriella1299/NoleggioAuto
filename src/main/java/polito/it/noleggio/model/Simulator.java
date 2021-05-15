package polito.it.noleggio.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.PriorityQueue;

import polito.it.noleggio.model.Event.EventType;

public class Simulator {
	
	//EVENTI
	private PriorityQueue<Event> queue;
	
	//PARAMETRI DI SIMULAZIONE (non cambiano durante la simulazione--> costanti)
	private int NC; //number of cars
	private Duration T_IN; //intervallo di tempo tra clienti
	private LocalTime oraApertura=LocalTime.of(8, 0);
	private LocalTime oraChiusura=LocalTime.of(20, 0);
	
	//STATO DEL SISTEMA
	private int nAuto; //auto attualmente presenti
	
	//MISURE IN USCITA
	private int nClienti;
	private int nClientiInsoddisfatti;
	
	//IMPOSTAZIONE PARAMETRI INIZIALI
	public void setNumCars(int NC) {
		this.NC=NC;
	}

	public void setClientFrequency(Duration d) {
		this.T_IN=d;		
	}
	
	//SIMULAZIONE
	public void run() { //deve eseguire la simulazione
		
		//inizializzo coda
		this.queue=new PriorityQueue<Event>();
		
		//inizializzo stato iniziale
		this.nAuto=NC; //all'inizio tutte le auto sono in garage
		this.nClienti=0; //nessun cliente arrrivato
		this.nClientiInsoddisfatti=0; //conseguenza del fatto che nessun cliente e' arrivato ancora
		
		//eventi iniziali
		LocalTime ora=this.oraApertura;
		while(ora.isBefore(oraChiusura)) { //finche' sono prima della chiusura aggiungo eventi di tipo 'nuovo cliente'
			this.queue.add(new Event(ora,EventType.NUOVO_CLIENTE));
			ora=ora.plus(this.T_IN);
		}
		
		//ciclo di simulazione (sempre uguale)
		while(!this.queue.isEmpty()) {
			Event e=this.queue.poll(); //prende il minimo della coda
			//System.out.println(e);
			processEvent(e);
		}
		
	}
	
	private void processEvent (Event e) { //Stiamo gestendo UN EVENTO SOLO PER VOLTA
		
		//che tipo di evento e'?
		switch(e.getType()) {
		
			case NUOVO_CLIENTE:
				this.nClienti++;//e' sempre un cliente che e' arrivato, anche se insoddisfatto
				//ho ancora auto disponibili?
				if(this.nAuto>0) {
					//noleggia
					this.nAuto--;
					double num=Math.random()*3;//numero casuale REALE [0,1)
					//int num=(int)(Math.random()*3)+1; -->cosi facevo facevo solo una volta l'add
					//se avessi avuto probabilita diverse avrei comunque dovuto creare tutti questi filtri per ogni percentuale
					if(num<1.0) {
						//prevedo che arrivera' un auto tra 1h: ora di adesso e' e.getTime+1h
						this.queue.add(
								new Event(e.getTime().plus(Duration.of(1, ChronoUnit.HOURS)),
										  EventType.RITORNO_AUTO));
					}
					else if(num<2.0) {
						//prevedo che arrivera' un auto tra 2h
						this.queue.add(
								new Event(e.getTime().plus(Duration.of(2, ChronoUnit.HOURS)),
										  EventType.RITORNO_AUTO));
					}
					else {
						//prevedo che arrivera' un auto tra 3h
						this.queue.add(
								new Event(e.getTime().plus(Duration.of(3, ChronoUnit.HOURS)),
										  EventType.RITORNO_AUTO));
					}
				}else {
					//insoddisfatto
					this.nClientiInsoddisfatti++;
				}
				break;
			
			case RITORNO_AUTO:
				this.nAuto++; //ritorna auto in garage
				break;
		}
		
	}

	public int getTotClients() { //clienti soddisfatti+insoddisfatti
		return this.nClienti;
	}

	public int getDissatisfied() {
		return this.nClientiInsoddisfatti;
	}
	
}
