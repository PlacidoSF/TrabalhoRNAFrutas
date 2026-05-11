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

    // Atualização: Recebe a semente
    public GerenciarDados(long seed) {
        embaralharDados(seed);
    }

    private void embaralharDados(long seed) {
        // Atualização: Usa a semente no gerador aleatório
        Random rnd = new Random(seed);

        int[] indicesFronteira = {5, 18, 19};

        for (int i = 0; i < indicesFronteira.length; i++) {
            int idxOriginal = indicesFronteira[i];
            
            // Troca entradas
            double[] tempE = entradas[i];
            entradas[i] = entradas[idxOriginal];
            entradas[idxOriginal] = tempE;
            
            // Troca saídas
            double[] tempS = saidas[i];
            saidas[i] = saidas[idxOriginal];
            saidas[idxOriginal] = tempS;
        }

        // Embaralhamos o resto dos dados do índice 3 em diante
        for (int i = entradas.length - 1; i > indicesFronteira.length; i--) {
            int index = rnd.nextInt(i - indicesFronteira.length + 1) + indicesFronteira.length;
            
            double[] tempEntrada = entradas[index];
            entradas[index] = entradas[i];
            entradas[i] = tempEntrada;
            
            double[] tempSaida = saidas[index];
            saidas[index] = saidas[i];
            saidas[i] = tempSaida;
        }
    }

    public double[][] getEntradasTreino() { return copiarFatia(entradas, 0, 16); }
    public double[][] getSaidasTreino() { return copiarFatia(saidas, 0, 16); }
    public double[][] getEntradasTeste() { return copiarFatia(entradas, 16, 20); }
    public double[][] getSaidasTeste() { return copiarFatia(saidas, 16, 20); }

    private double[][] copiarFatia(double[][] origem, int inicio, int fim) {
        int tamanho = fim - inicio;
        double[][] destino = new double[tamanho][origem[0].length];
        for (int i = 0; i < tamanho; i++) {
            destino[i] = origem[inicio + i];
        }
        return destino;
    }
}