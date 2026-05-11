package dados;

import java.util.Random;

public class GerenciarDados {

    private double[][] entradas = {
            {0.20, 0.15}, {0.30, 0.25}, {0.10, 0.50}, {0.50, 0.10},
            {0.25, 0.35}, {0.40, 0.40}, {0.55, 0.55}, {0.65, 0.60},
            {0.70, 0.75}, {0.80, 0.85}, {0.90, 0.90}, {0.85, 0.70},
            {0.60, 0.65}, {0.75, 0.80}, {0.95, 0.95}, {0.15, 0.80},
            {0.80, 0.20}, {0.35, 0.30}, {0.45, 0.50}, {0.50, 0.55}
    };

    private double[][] saidas = {
            {0.0}, {0.0}, {0.0}, {0.0},
            {0.0}, {0.0}, {1.0}, {1.0},
            {1.0}, {1.0}, {1.0}, {1.0},
            {1.0}, {1.0}, {1.0}, {0.0},
            {0.0}, {0.0}, {0.0}, {1.0}
    };

    public GerenciarDados() {
        embaralharDados();
    }

    
    private void embaralharDados() {
        Random rnd = new Random();
        for (int i = entradas.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            
            // troca as entradas
            double[] tempEntrada = entradas[index];
            entradas[index] = entradas[i];
            entradas[i] = tempEntrada;
            
            // troca as saídas
            double[] tempSaida = saidas[index];
            saidas[index] = saidas[i];
            saidas[i] = tempSaida;
        }
    }

    public double[][] getEntradasTreino() {
        return copiarFatia(entradas, 0, 14);
    }

    public double[][] getSaidasTreino() {
        return copiarFatia(saidas, 0, 14);
    }

    public double[][] getEntradasTeste() {
        return copiarFatia(entradas, 14, 20);
    }

    public double[][] getSaidasTeste() {
        return copiarFatia(saidas, 14, 20);
    }

    private double[][] copiarFatia(double[][] origem, int inicio, int fim) {
        int tamanho = fim - inicio;
        double[][] destino = new double[tamanho][origem[0].length];
        for (int i = 0; i < tamanho; i++) {
            destino[i] = origem[inicio + i];
        }
        return destino;
    }
}