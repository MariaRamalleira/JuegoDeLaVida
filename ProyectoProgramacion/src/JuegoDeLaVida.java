

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class JuegoDeLaVida {

	static final int TAMANIO = 50;

	static final char IMPRIME_MUERTA = ' ';
	static final char IMPRIME_VIVA = '*';
	static final int VIVA = 1;
	static final int MUERTA = 0;
	

	static int[][] juego = new int[TAMANIO][TAMANIO];

	public static void leerMatriz(String ruta) {

		try {
			FileReader fr = new FileReader(ruta);
			BufferedReader br = new BufferedReader(fr);

			String linea;
			String[] fila;
			int numFila = 0;

			while ((linea = br.readLine()) != null) {

				fila = linea.split(",");
				for (int numCol = 0; numCol < TAMANIO; numCol++) {
					if (numCol < fila.length) {
						juego[numFila][numCol] = Integer.parseInt(fila[numCol].trim());
					} else {
//una vez leÃ­das todas las columnas del fichero, el resto se rellenan a ceros
						juego[numFila][numCol] = 0;
					}

				} // una vez rellenada esa fila, incrementamos el nÃºmero de fila
				numFila++;

			} // fin del while
//cuando hayamos recorrido todas las filas del fichero, el resto debemos rellenarlas con ceros
			if (numFila < TAMANIO) {
				for (int i = numFila; i < TAMANIO; i++) {
					for (int j = 0; j < TAMANIO; j++) {
						juego[i][j] = 0;
					}

				}
			} // si no es asÃ­, no hay nada que rellenar
			fr.close();
		} catch (Exception e) {
			System.out.println("Excepcion leyendo fichero " + ruta + ": " + e);

		}

	}

	public static void imprimirTablero() {

		for (int i = 0; i < TAMANIO; i++) {
			for (int j = 0; j < TAMANIO; j++) {
				System.out.format("%s ", (juego[i][j] == VIVA ? IMPRIME_VIVA : IMPRIME_MUERTA));
			}
			System.out.println();
		}

	}

	static void imprimirCuantasRodean() {

		for (int i = 0; i < TAMANIO; i++) {
			for (int j = 0; j < TAMANIO; j++) {
				System.out.format("%d ", cuantasVivasRodean(i, j));
			}
			System.out.println();
		}

	}

	public static int cuantasVivasRodean(int fila, int columna) {
		// int vivas= 0;
		// primera columna de cualquier fila menos la primera y la ultima
		// if(columna==TAMANIO-1 && (fila<TAMANIO-1 && fila>0)) {
		// if(juego[fila][columna-1]==VIVA) {
		// vivas++;
		// }
		// if(juego[fila-1][columna]==VIVA){
		// vivas++;
		// }
		// if(juego[fila+1][columna]==VIVA) {
		// vivas++;
		// }
		// }
		// primera fila de cualquier columna menos la primera y la ultima
		// if(fila==0 && columna >0 && columna <49) {
		// if(juego[fila][columna+1]==VIVA) {
		// vivas++;
		// }
		// if(juego[fila][columna-1]==VIVA) {
		// vivas++;
		// }
		// if(juego[fila+1][columna]==VIVA) {
		// vivas++;
		// }
		// }

		int numVivas = 0;

		int comienzoFila, finalFila, comienzoCol, finalCol;

		// elección del comienzo y final del recorrido de la fila
		if (fila == 0) {
			comienzoFila = 0;
			finalFila = 1;
		} else if (fila == (TAMANIO - 1)) {
			comienzoFila = TAMANIO - 2;
			finalFila = TAMANIO - 1;
		} else {
			comienzoFila = fila - 1;
			finalFila = fila + 1;
		}

		// elección del comienzo y final del recorrido de la columna
		if (columna == 0) {
			comienzoCol = 0;
			finalCol = 1;
		} else if (columna == (TAMANIO - 1)) {
			comienzoCol = TAMANIO - 2;
			finalCol = TAMANIO - 1;
		} else {
			comienzoCol = columna - 1;
			finalCol = columna + 1;
		}

		for (int i = comienzoFila; i <= finalFila; i++) {
			for (int j = comienzoCol; j <= finalCol; j++) {
				if (!(i == fila && j == columna) && juego[i][j] == VIVA) {
					numVivas++;
				}
			}

		}


		return numVivas;

	}

	public static int [][] generaciones2(int [][] juego) throws InterruptedException {
		 Scanner sc = new Scanner(System.in);
		int parada = 1;
		
		while(parada==1) {
			for(int i=0;i<TAMANIO-1;i++) {
				for(int j=0;j<TAMANIO-1;j++) {
					cuantasVivasRodean(i,j);
					if(juego[i][j]==VIVA) {
						if(cuantasVivasRodean(i,j)<2) {
							juego[i][j]=MUERTA;
						}else if(cuantasVivasRodean(i,j)>2 && cuantasVivasRodean(i,j)==3 ) {
							juego[i][j]=VIVA;
						}else {
							juego[i][j]=MUERTA;
						}
					
					}else {
						if(cuantasVivasRodean(i,j)==3) {
							juego[i][j]= VIVA;
						}
					}
				}
			}
			imprimirTablero();
			imprimirCuantasRodean();
			System.out.println("Para seguir pulse 1 y para parar pulse 2");
			parada= sc.nextInt();
			
			
		}
		sc.close();
	  return juego;	
	}

	public static void main(String[] args) throws InterruptedException {
		leerMatriz("ficheros/cargaFaro.txt");
		imprimirTablero();
		System.out.println("\n");
		imprimirCuantasRodean();
		generaciones2(juego);
      
	}
}
