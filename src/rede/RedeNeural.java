package rede;

import java.util.Random;

public class RedeNeural {

    // 2 - 3 - 1 
    private double[][] pesosOculta = new double[2][3];
    private double[] biasOculta = new double[3];

    private double[][] pesosSaida = new double[3][1];
    private double[] biasSaida = new double[1];

     double[] saidaOculta = new double[3];

    public RedeNeural() {
        inicializarPesos();
    }

    // -0.5 ~ 0.5
    public void inicializarPesos() {
        Random random = new Random();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                pesosOculta[i][j] = random.nextDouble() - 0.5;
            }
        }

        for (int i = 0; i < 3; i++) {
            biasOculta[i] = random.nextDouble() - 0.5;
        }

        for (int i = 0; i < 3; i++) {
            pesosSaida[i][0] = random.nextDouble() - 0.5;
        }

        biasSaida[0] = random.nextDouble() - 0.5;
    }

    public double sigmoide(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public double sigmoideDerivada(double x) {
        return sigmoide(x) * (1 - sigmoide(x));
    }

    public double feedforward(double[] entrada) {

        // entrada -> camada oculta
        for (int i = 0; i < 3; i++) {
            double net = 0;

            // soma ponderada
            for (int j = 0; j < 2; j++) {
                net += entrada[j] * pesosOculta[j][i];
            }

            net += biasOculta[i];

            // ativação
            saidaOculta[i] = sigmoide(net);
        }

        // camada oculta -> camada de saída
        double net = 0;
        for (int i = 0; i < 3; i++) {
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

                // feedforward
                double saidaObtida = feedforward(entrada);

                // cálculo do erro
                double erroAmostra = saidaEsperada - saidaObtida;
                erroGlobal += 0.5 * Math.pow(erroAmostra, 2);

                // backpropagation
                double deltaSaida = erroAmostra * sigmoideDerivada(saidaObtida);

                double[] deltaOculta = new double[3];

                for (int j = 0; j < 3; j++) {
                    deltaOculta[j] = sigmoideDerivada(saidaOculta[j]) * deltaSaida * pesosSaida[j][0];
                }

                // atualização dos pesos e bias da camada de saída
                for (int j = 0; j < 3; j++) {
                    pesosSaida[j][0] += taxaAprendizado * deltaSaida * saidaOculta[j];
                }
                biasSaida[0] += taxaAprendizado * deltaSaida;


                // atualização dos pesos e bias da camada oculta
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 3; k++) {
                        pesosOculta[j][k] += taxaAprendizado * deltaOculta[k] * entrada[j];
                    }
                }

                for (int j = 0; j < 3; j++) {
                    biasOculta[j] += taxaAprendizado * deltaOculta[j];
                }
            }

            // média do erro global
            erroGlobal /= entradas.length;
            epocaAtual++;


            // situação a cada 100 épocas
            if (epocaAtual % 100 == 0) {
                System.out.println("Época: " + epocaAtual + " | Erro Global: " + String.format("%.6f", erroGlobal));
            }
        }

        System.out.println("Treinamento finalizado em " + epocaAtual + " épocas com erro global: " + String.format("%.6f", erroGlobal));
    }
}
