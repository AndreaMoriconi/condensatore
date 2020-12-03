/**
 * @(#)GeneraValori2.java
 *
 *
 * @AndreaMoriconi
 * @version 1.00 2020/9/13
 */
package com.github.plot;
import java.util.*;
import java.lang.Math.*;

public class GeneraValori2 {
	private double tempoOsservazione; //stabilisce periodo di osservazione del fenomeno
	private double unit‡Tempo; // secondi
	private double frequenza; //frequenza generatrice dell' onda quadra indicata in Hertz
	private double tensione=12; //tensione erogata dal generatore
	private double resistenza=1000000; //ohm
	private double capacit‡=0.000001; //farad
	private double [] x; //=new double [(int)(tempoOsservazione/unit‡Tempo)];
	private double [] y; //=new double [(int)(tempoOsservazione/unit‡Tempo)];
	public GeneraValori2(double tempoOsservazione,double unit‡Tempo, double frequenza, double tensione, double resistenza, double capacit‡){
		this.tempoOsservazione=tempoOsservazione;
		this.unit‡Tempo=unit‡Tempo;
		this.frequenza=frequenza;
		this.tensione=tensione;
		this.resistenza=resistenza;
		this.capacit‡=capacit‡;
		this.x=new double [(int)(tempoOsservazione/unit‡Tempo)];
		this.y= new double [(int)(tempoOsservazione/unit‡Tempo)];
		this.coordinate();
	}
		public GeneraValori2(double tempoOsservazione,double unit‡Tempo, double frequenza){
		this.tempoOsservazione=tempoOsservazione;
		this.unit‡Tempo=unit‡Tempo;
		this.frequenza=frequenza;
		this.x=new double [(int)(tempoOsservazione/unit‡Tempo)];
		this.y= new double [(int)(tempoOsservazione/unit‡Tempo)];
		this.coordinate();
	}
	private void coordinate(){
		double tau=resistenza*capacit‡;
		double e=2.7182818284590;
		double tempo=0;
		double tempoLocal=0;
		boolean generatoreOn= true; //true indica generatore in funzione, false generatore spento
		//tenendo conto dell' unit‡ di tempo e della frequenza(Hz) capisco quante ordinate creare per ogni fase
		double cicliOndeQuadre=1/frequenza/unit‡Tempo;
		double tensioneCondensatore=0;
		int count=0;
		//ciclo di campionamento (digitalizzazione fenomeno fisico)
		for(int i=0; i<(int)(tempoOsservazione/unit‡Tempo); i++){
			if(count==1/frequenza/unit‡Tempo){
				generatoreOn=!generatoreOn;
				count=0;
				tensioneCondensatore=y[i-1];
				if(generatoreOn){
					tempoLocal = Formula.tLocalCarica(tau, e, tensioneCondensatore, tensione);
				}else{
					tempoLocal = 0;
				}
				//System.out.println("tC"+tensioneCondensatore);
			}
			if(generatoreOn){
				y[i]=Formula.carica(tempoLocal, e, tau, tensione );
				x[i]=tempo;
			}else{
				y[i]=Formula.scarica(tempoLocal, e, tau, tensioneCondensatore );
				x[i]=tempo;
			}
			count++;
			tempoLocal+=unit‡Tempo;
			tempo+=unit‡Tempo;
		}
		System.out.println("tempooss*freq "+tempoOsservazione*frequenza);
		System.out.println("cicliOndeQuadre "+cicliOndeQuadre);



	}
	public double getTempoOsservazione(){
		return tempoOsservazione;
	}
	public double getTensione(){
		return tensione;
	}
	public double[] getX(){
		return x;
	}
	public double[] getY(){
		return y;
	}
}
class Formula{
	public static double carica(double tempo, double e, double tau, double generatoreT){
		double y= generatoreT*(1-(Math.pow(e, (tempo-(tempo*2)/tau))));
		return y;
	}
	public static double scarica(double tempo, double e, double tau, double tensione){
		double y=tensione*(Math.pow(e, (tempo-(tempo*2)/tau)));
		return y;
	}
	public static double tLocalCarica( double tau, double e, double tAttuale, double tFinale){
		double tempo;
		tempo = -tau* Math.log (1-tAttuale/tFinale);
		//System.out.println("mathlog(e) "+(Math.log(e)* Math.log(1-(tAttuale/tFinale)))+" tempo "+tempo);
		return tempo;
	}
		public static double tLocalScarica( double tau, double e, double tAttuale, double tensioneIniz){
		double tempo;
		tempo = -tau* Math.log (tAttuale/tensioneIniz);
		return tempo;
	}

}