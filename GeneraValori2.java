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
	private double unitąTempo; // secondi
	private double frequenza; //frequenza generatrice dell' onda quadra indicata in Hertz
	private double tensione=12; //tensione erogata dal generatore
	private double resistenza=1000000; //ohm
	private double capacitą=0.000001; //farad
	private double [] x; //=new double [(int)(tempoOsservazione/unitąTempo)];
	private double [] y; //=new double [(int)(tempoOsservazione/unitąTempo)];
	public GeneraValori2(double tempoOsservazione,double unitąTempo, double frequenza, double tensione, double resistenza, double capacitą){
		this.tempoOsservazione=tempoOsservazione;
		this.unitąTempo=unitąTempo;
		this.frequenza=frequenza;
		this.tensione=tensione;
		this.resistenza=resistenza;
		this.capacitą=capacitą;
		this.x=new double [(int)(tempoOsservazione/unitąTempo)];
		this.y= new double [(int)(tempoOsservazione/unitąTempo)];
		this.coordinate();
	}
		public GeneraValori2(double tempoOsservazione,double unitąTempo, double frequenza){
		this.tempoOsservazione=tempoOsservazione;
		this.unitąTempo=unitąTempo;
		this.frequenza=frequenza;
		this.x=new double [(int)(tempoOsservazione/unitąTempo)];
		this.y= new double [(int)(tempoOsservazione/unitąTempo)];
		this.coordinate();
	}
	private void coordinate(){
		double tau=resistenza*capacitą;
		double e=2.7182818284590;
		double tempo=0;
		double tempoLocal=0;
		boolean generatoreOn= true; //true indica generatore in funzione, false generatore spento
		//tenendo conto dell' unitą di tempo e della frequenza(Hz) capisco quante ordinate creare per ogni fase
		double cicliOndeQuadre=1/frequenza/unitąTempo;
		double tensioneCondensatore=0;
		int count=0;
		//ciclo di campionamento (digitalizzazione fenomeno fisico)
		for(int i=0; i<(int)(tempoOsservazione/unitąTempo); i++){
			if(count==1/frequenza/unitąTempo){
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
			tempoLocal+=unitąTempo;
			tempo+=unitąTempo;
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