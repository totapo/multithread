package objetos;

import java.util.Random;

public class EscritorT extends Thread {
	private Base b;
	private Random r;
	private Testador t;
	private GerenciadorAcesso a;
	private static final String escrever = "MODIFICADO";
	
	public EscritorT(GerenciadorAcesso a, Testador t){
		this.a = a;
		r = new Random();
		this.t = t;
	}
	
	@Override
	public void run() {
		int pos;
		try {
			a.bd.acquire(); //pega bd
			b = a.getBase();
			
			for(int i=0; i<100; i++){
				pos = r.nextInt()%b.getTamanho();
				b.escrever(escrever, pos);
			}
			
			Thread.sleep(1);
			
			a.bd.release(); //libera o bd
			
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