
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class JuegoDeLaVida {
	// TamaÃ±o de la matriz
	static final int TAMANIO = 40;
	// Imprime casilla muerta con un espacio y casilla viva con un asterisco
	static final char IMPRIME_MUERTA = '.';
	static final char IMPRIME_VIVA = '*';
	// Casilla viva es un 1 en la matriz y la muerta un 0
	static final int VIVA = 1;
	static final int MUERTA = 0;
	// Juego es el estado inicial de la matriz y generaciones es el cambio de cada
	// iteraciÃ³n
	static int[][] juego = new int[TAMANIO][TAMANIO];

	
	//numero de generaciones a producir
	static final int GENERACIONES=30;
	
	//retardo entre generaciones, en milisegundos
	static final int RETARDO=2000;
	

	/**
	 * @param ruta al fichero que contiene el estado inicial de la matriz Lee la
	 *             matriz que esta en el fichero y la copia en la variable
	 *             juego(estado inicial)
	 */
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
						// una vez leidas todas las columnas del fichero, el resto se rellenan a ceros
						juego[numFila][numCol] = 0;
					}

				} // una vez rellenada esa fila, incrementamos el numero de fila
				numFila++;

			} // fin del while
				// cuando hayamos recorrido todas las filas del fichero, el resto debemos
				// rellenarlas con ceros
			if (numFila < TAMANIO) {
				for (int i = numFila; i < TAMANIO; i++) {
					for (int j = 0; j < TAMANIO; j++) {
						juego[i][j] = 0;
					}

				}
			} // si no es asi, no hay nada que rellenar
			fr.close();
		} catch (Exception e) {
			System.out.println("Excepcion leyendo fichero " + ruta + ": " + e);

		}

	}

	/**
	 * Imprime por pantalla la matriz inicial con espacios y asteriscos
	 */
	public static void imprimirTablero() {

		for (int i = 0; i < TAMANIO; i++) {
			for (int j = 0; j < TAMANIO; j++) {
				System.out.format("%s ", (juego[i][j] == VIVA ? IMPRIME_VIVA : IMPRIME_MUERTA));
			}
			System.out.println();
		}
		

	}

	/**
	 * Imprime por pantalla una matriz con los numeros de celulas vivas que rodean
	 * la celda
	 */
	static void imprimirCuantasRodean() {
// imprime la matriz con los numeros de celulas vecinas de cada celda
		for (int i = 0; i < TAMANIO; i++) {
			for (int j = 0; j < TAMANIO; j++) {
				System.out.format("%d ", cuantasVivasRodean(i, j));
			}
			System.out.println();
		}

	}

	/**
	 * @param fila
	 * @param columna
	 * @return numVivas, es el numeros de celulas vivas que tiene una celda
	 *         alrededor
	 */
	public static int cuantasVivasRodean(int fila, int columna) {

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

	/**
	 * @param generaciones
	 * @return generaciones, matriz resultado despues de aplicar las reglas del
	 *         juego
	 * @throws InterruptedException
	 */
	public static int[][] generacionesConDialogo(int[][] generaciones) throws InterruptedException {

		Scanner sc = new Scanner(System.in);
		int parada = 1;
		// Si la persona escribe 1 va a permanecer en el bucle, en cuanto escriba 2
		// saldra del bucle

		while (parada == 1) {
			for (int i = 0; i < TAMANIO - 1; i++) {
				for (int j = 0; j < TAMANIO - 1; j++) {
					cuantasVivasRodean(i, j);
					// si la celda estÃ¡ viva
					if (generaciones[i][j] == VIVA) {
						if (cuantasVivasRodean(i, j) < 2) {
							generaciones[i][j] = MUERTA;
						} else if (cuantasVivasRodean(i, j) >= 2 && cuantasVivasRodean(i, j) <= 3) {
							generaciones[i][j] = VIVA;
						} else {
							generaciones[i][j] = MUERTA;
						}
						// si la celda esta muerta
					} else {
						if (cuantasVivasRodean(i, j) == 3) {
							generaciones[i][j] = VIVA;
						}
					}
				}
			}
			imprimirTablero();
			System.out.println("Para seguir pulse 1 y para parar pulse 2");
			parada = sc.nextInt();

		}
		sc.close();
		return generaciones;
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		leerMatriz("ficheros/cargaNaveGrande.txt");


		for (int i = 0; i <GENERACIONES ; i++) {
		
			System.out.println("\t\t  GENERACIÓN "+ (i+1));
			imprimirTablero();
			Thread.sleep(RETARDO);
			
			//generamos la siguiente generación y la guardamos en el tablero
			juego = sigienteGeneracion (juego);

			
		}

	}

	/**
	 * @param generaciones
	 * @return generaciones, matriz resultado despues de aplicar las reglas del
	 *         juego
	 * @throws InterruptedException
	 */
	public static int[][] sigienteGeneracion (int[][] generaciones) throws InterruptedException {

		// Si la persona escribe 1 va a permanecer en el bucle, en cuanto escriba 2
		// saldra del bucle
		int [][] nuevoTablero= new int[TAMANIO][TAMANIO];
		for (int i = 0; i < TAMANIO ; i++) {
			for (int j = 0; j < TAMANIO ; j++) {
				//cuantasVivasRodean(i, j);
				// si la celda estÃ¡ viva
				if (generaciones[i][j] == VIVA) {
					if (cuantasVivasRodean(i, j) < 2) {
						nuevoTablero[i][j] = MUERTA;
					} else if (cuantasVivasRodean(i, j) < 4) {
						nuevoTablero[i][j] = VIVA;
					} else {
						nuevoTablero[i][j] = MUERTA;
					}
					// si la celda esta muerta
				} else {
					if (cuantasVivasRodean(i, j) == 3) {
						nuevoTablero[i][j] = VIVA;
					}
				}
			}
		}


		return nuevoTablero;
	}
	
	

}
