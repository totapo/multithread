package objetos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Testador {
	private int qtdLeitores, qtdEscritores, qtdTestes;
	private long inicio,fim;
	private boolean acessoConcorrente; //
	private List<Thread> threads;
	private Random r;
	public Semaphore rodandoMutex, travaContadorRodando; //primeiro Semáforo faz o testador dormir depois de iniciar todas as threads,
	//o segundo é utilizado pelas para atualizar o valor de threads ativas
	public int qtdRodando;
	private Base b;
	
	public Testador(){
		threads = new ArrayList<Thread>();
		r = new Random();
		rodandoMutex = new Semaphore(0);
		travaContadorRodando=new Semaphore(1);
	}
	
	
	public void setup(int leitores, int escritores, int testes, boolean acessoConcorrente){
		this.qtdLeitores = leitores;
		this.qtdEscritores = escritores;
		this.qtdTestes = testes;
		this.acessoConcorrente = acessoConcorrente;
	}
	
	public double runTest(){
		double media=0;
		Thread aux;
		GerenciadorAcesso mgr;
		int qtdLeitores,qtdEscritores;
		try {
			for(int i = qtdTestes; i>0; i--){
				qtdLeitores = this.qtdLeitores;
				qtdEscritores = this.qtdEscritores;
				b = new Base();
				mgr = new GerenciadorAcesso(b);
				threads.clear();
				
				//cria threads
				for(int j = 0; j<100; j++){
					if(qtdLeitores>0){
						aux = (acessoConcorrente)?new Leitor(mgr,this):new LeitorT(mgr,this);
						threads.add(aux);
						qtdLeitores--;
					} else if(qtdEscritores>0){
						aux = (acessoConcorrente)?new Escritor(mgr,this):new EscritorT(mgr,this);
						threads.add(aux);
						qtdEscritores--;
					}
				}
				//embaralha threads
				int cont = r.nextInt(5);
				do{
					Collections.shuffle(threads);
					cont--;
				} while(cont>0);
				//inicia o timer e seta o contador de threads ativas pra 100
				inicio = System.currentTimeMillis();
				qtdRodando=100;
				//inicia as threads
				for(Thread t : threads){
					t.start();
				}
				rodandoMutex.acquire(); //para esperar todas rodarem
				fim = System.currentTimeMillis();
				media+=(double)((fim-inicio));
			}
			return media/qtdTestes;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return media;
		}
	}
	
}
