package rede;

import java.util.Random;

public class RedeNeural {

    private int numEntrada = 2;
    private int numOculta = 3;
    private int numSaida = 1;

    private double[][] pesosOculta = new double[numEntrada][numOculta];
    private double[] biasOculta = new double[numOculta];
    private double[][] pesosSaida = new double[numOculta][numSaida];
    private double[] biasSaida = new double[numSaida];
    double[] saidaOculta = new double[numOculta];

    
    public RedeNeural(long seed) {
        inicializarPesos(seed);
    }

    public int getNumEntrada() {
        return numEntrada;
    }

    public int getNumOculta() {
        return numOculta;
    }

    public int getNumSaida() {
        return numSaida;
    }

    public void inicializarPesos(long seed) {
        
        Random random = new Random(seed);

        for (int i = 0; i < numEntrada; i++) {
            for (int j = 0; j < numOculta; j++) {
                pesosOculta[i][j] = random.nextDouble() - 0.5;
            }
        }
        for (int i = 0; i < numOculta; i++) {
            biasOculta[i] = random.nextDouble() - 0.5;
        }
        for (int i = 0; i < numOculta; i++) {
            pesosSaida[i][0] = random.nextDouble() - 0.5;
        }
        biasSaida[0] = random.nextDouble() - 0.5;
    }

    public double sigmoide(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public double sigmoideDerivada(double saidaAtivada) {
        return saidaAtivada * (1 - saidaAtivada);
    }

    public double feedforward(double[] entrada) {
        for (int i = 0; i < numOculta; i++) {
            double net = 0;
            for (int j = 0; j < numEntrada; j++) {
                net += entrada[j] * pesosOculta[j][i];
            }
            net += biasOculta[i];
            saidaOculta[i] = sigmoide(net);
        }

        double net = 0;
        for (int i = 0; i < numOculta; i++) {
            net += saidaOculta[i] * pesosSaida[i][0];
        }
        net += biasSaida[0];

        return sigmoide(net);
    }

    public void treinar(double[][] entradas, double[][] saidasEsperadas, int maxEpocas, double taxaAprendizado, double erroMinimo) {
        int epocaAtual = 0;
        double erroGlobal = 1.0;

        while (epocaAtual < maxEpocas && erroGlobal > erroMinimo) {
            erroGlobal = 0;

            for (int i = 0; i < entradas.length; i++) {
                double[] entrada = entradas[i];
                double saidaEsperada = saidasEsperadas[i][0];

                double saidaObtida = feedforward(entrada);
                double erroAmostra = saidaEsperada - saidaObtida;
                erroGlobal += 0.5 * Math.pow(erroAmostra, 2);

                double deltaSaida = erroAmostra * sigmoideDerivada(saidaObtida);

                double[] deltaOculta = new double[numOculta]; 
                for (int j = 0; j < numOculta; j++) {
                    deltaOculta[j] = sigmoideDerivada(saidaOculta[j]) * (deltaSaida * pesosSaida[j][0]);
                }

                for (int j = 0; j < numOculta; j++) {
                    pesosSaida[j][0] += taxaAprendizado * deltaSaida * saidaOculta[j];
                }
                biasSaida[0] += taxaAprendizado * deltaSaida;

                for (int j = 0; j < numEntrada; j++) { 
                    for (int k = 0; k < numOculta; k++) { 
                        pesosOculta[j][k] += taxaAprendizado * deltaOculta[k] * entrada[j];
                    }
                }
                for (int j = 0; j < numOculta; j++) {
                    biasOculta[j] += taxaAprendizado * deltaOculta[j];
                }
            }

            erroGlobal /= entradas.length;
            epocaAtual++;

            if (epocaAtual % 500 == 0) {
                System.out.printf("Época: %d | Erro Médio: %.6f\n", epocaAtual, erroGlobal);
            }
        }
        
        
        if (erroGlobal <= erroMinimo) {
            System.out.println("---------------------------------------------");
            System.out.printf(">>> PARADA ANTECIPADA: A rede atingiu o erro alvo na época %d <<<\n", epocaAtual);
        }
        
        System.out.printf("Treino finalizado! Épocas rodadas: %d | Erro Final: %.6f\n", epocaAtual, erroGlobal);
    }
}