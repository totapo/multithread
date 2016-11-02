package objetos;

import java.util.Random;

public class Escritor extends Thread {
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
/*
//WRITER
<ENTRY Section>
  wmutex.P();//reserve entry section for writers - avoids race conditions
    writecount++;//report yourself as a writer entering
    if (writecount == 1)//checks if you're first writer
        readTry.P();//if you're first, then you must lock the readers out. Prevent them from trying to enter CS
  wmutex.V();//release entry section

<CRITICAL Section>
  resource.P();//reserve the resource for yourself - prevents other writers from simultaneously editing the shared resource
   // writing is performed
  resource.V();//release file

<EXIT Section>
  wmutex.P();//reserve exit section
    writecount--;//indicate you're leaving
    if (writecount == 0)//checks if you're the last writer
        readTry.V();//if you're last writer, you must unlock the readers. Allows them to try enter CS for reading
  wmutex.V();//release exit section
  */
