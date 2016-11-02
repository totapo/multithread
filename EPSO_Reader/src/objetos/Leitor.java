package objetos;

import java.util.Random;

public class Leitor extends Thread{
	private GerenciadorAcesso a;
	private Testador t;
	private Random r;
	private Base b;
	private String s;
	
	public Leitor(GerenciadorAcesso a, Testador t){
		this.a = a;
		r = new Random();
		this.t=t;
	}

	@Override
	public void run() {
		int pos;
		try {
			a.readTry.acquire();	//tenta entrar na base
			a.rmutex.acquire();
			a.readCount++;
			if(a.readCount==1)
				a.bd.acquire();	//se for o primeiro leitor trava a base
			a.rmutex.release();
			a.readTry.release();
			
			b = a.getBase();
			
			for(int i=0; i<100; i++){
				pos = r.nextInt()%b.getTamanho();
				s = b.ler(pos);
			}
			
			Thread.sleep(1);
			
			a.rmutex.acquire();
			a.readCount--;
			if(a.readCount==0){	//se for o ultimo leitor libera a base
				a.bd.release();
			}
			a.rmutex.release();
			
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

//READER
<ENTRY Section>
 readTry.P();//Indicate a reader is trying to enter
   rmutex.P();//lock entry section to avoid race condition with other readers
     readcount++;//report yourself as a reader
     if (readcount == 1)//checks if you are first reader
         resource.P();//if you are first reader, lock  the resource
   rmutex.V();//release entry section for other readers
 readTry.V();//indicate you are done trying to access the resource

<CRITICAL Section>
// reading is performed

<EXIT Section>
 rmutex.P();//reserve exit section - avoids race condition with readers
   readcount--;//indicate you're leaving
   if (readcount == 0)//checks if you are last reader leaving
       resource.V();//if last, you must release the locked resource
 rmutex.V();//release exit section for other readers
*/