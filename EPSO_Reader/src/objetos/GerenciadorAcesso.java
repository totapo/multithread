package objetos;

import java.util.concurrent.Semaphore;

public class GerenciadorAcesso {
	public Semaphore rmutex, wmutex,readTry, bd;
	
	public int readCount, writeCount;
	
	private Base base;
	
	public GerenciadorAcesso(Base b){
		this.base = b;
		rmutex = new Semaphore(1); //mutex
		wmutex = new Semaphore(1); //mutex
		readTry = new Semaphore(1); //mutex
		bd = new Semaphore(1); //mutex
	}
	
	public Base getBase(){
		return base;
	}
	
}
/*int readcount, writecount; //(initial value = 0)
semaphore rmutex, wmutex, readTry, resource; //(initial value = 1)*/