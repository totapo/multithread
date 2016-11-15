package objetos;

import java.util.Random;

public class Escritor extends Thread { //representa um Escritor para o modelo que permite acesso concorrente
	private Base b;
	private Random r;
	private Testador t;
	private GerenciadorAcesso a;
	private static final String escrever = "MODIFICADO";
	
	public Escritor(GerenciadorAcesso a, Testador t){
		this.a = a;
		r = new Random();
		this.t = t;
	}
	
	@Override
	public void run() {
		int pos;
		try {
			a.wmutex.acquire();
			a.writeCount++;
			if(a.writeCount==1)
				a.readTry.acquire(); //lock readers out
			a.wmutex.release();
			
			a.bd.acquire(); //pega bd
			b = a.getBase();
			//escreve na base
			for(int i=0; i<100; i++){
				pos = r.nextInt()%b.getTamanho();
				b.escrever(escrever, pos);
			}
			
			Thread.sleep(1);
			
			a.bd.release(); //libera o bd
			a.wmutex.acquire();
			a.writeCount--;
			if(a.writeCount==0)	//se for o ultimo escritor
				a.readTry.release(); //libera a entrada de leitores
			a.wmutex.release();
			
			t.travaContadorRodando.acquire(); //trava o contador de threads do testador
			t.qtdRodando--;
			if(t.qtdRodando==0){	//se é a última thread 
				t.rodandoMutex.release();	//libera o testador
			}
			t.travaContadorRodando.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
