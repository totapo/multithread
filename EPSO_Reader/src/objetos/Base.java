package objetos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class Base {
	private ArrayList<String> texto;
	private static Base base;
	
	public Base(){
		texto = new ArrayList<String>();
		//texto = new ArrayList<String>();
		//texto.ensureCapacity(36242);
		File f = new File("./pasta/bd.txt");
		String linha=null;
		try {
			BufferedReader r = new BufferedReader(new FileReader(f));
			while((linha = r.readLine())!=null){
				texto.add(linha);
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String ler(int posicao){
		if(posicao < texto.size() && posicao>=0)
			return texto.get(posicao);
		return null;
	}
	
	public void escrever(String valor, int posicao){
		if(posicao < texto.size() && posicao>=0)
			texto.set(posicao, valor);
	}
	
	public int getTamanho(){
		return texto.size();
	}
	
}
