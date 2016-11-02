package objetos;

public class Main {
	public static void main(String[] args){
		int qtdEscritores,qtdLeitores,qtdTestes;
		qtdTestes=50;
		Testador testador = new Testador();
		double m;
		System.out.println("CONCORRENTE:");
		for(qtdEscritores=0;qtdEscritores<=100;qtdEscritores++){
			qtdLeitores=100-qtdEscritores;
			testador.setup(qtdLeitores, qtdEscritores, qtdTestes, true);
			m = testador.runTest();
			System.out.println(qtdEscritores+" "+qtdLeitores+" "+m);
		}
		System.out.println("SEM CONCORRENCIA (LINEAR):");
		for(qtdEscritores=0;qtdEscritores<=100;qtdEscritores++){
			qtdLeitores=100-qtdEscritores;
			testador.setup(qtdLeitores, qtdEscritores, qtdTestes, false);
			m = testador.runTest();
			System.out.println(qtdEscritores+" "+qtdLeitores+" "+m);
		}
	}
}
