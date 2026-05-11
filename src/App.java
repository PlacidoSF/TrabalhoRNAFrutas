import rede.RedeNeural;
import dados.GerenciarDados;

public class App {
    public static void main(String[] args) throws Exception {
        GerenciarDados dados = new GerenciarDados();

        double[][] entradasTreino = dados.getEntradasTreino();
        double[][] saidasTreino = dados.getSaidasTreino();

        double[][] entradasTeste = dados.getEntradasTeste();
        double[][] saidasTeste = dados.getSaidasTeste();

        System.out.println("--- 1. Criando a rede neural [2-3-1] ---\n");
        RedeNeural rede = new RedeNeural();

        System.out.println("--- 2. Treinando a rede ---");
       rede.treinar(entradasTreino, saidasTreino, 50000, 0.05, 0.005);

        System.out.println("\n--- 3. Testando a rede ---");
        int verdadeiroPositivo = 0;
        int verdadeiroNegativo = 0;
        int falsoPositivo = 0;
        int falsoNegativo = 0;

        for (int i = 0; i < entradasTeste.length; i++) {

            double[] entrada = entradasTeste[i];
            double saidaEsperada = saidasTeste[i][0];

            double predicaoContinua = rede.feedforward(entrada);

            double predicao = (predicaoContinua >= 0.5) ? 1.0 : 0.0;

            if (predicao == 1.0 && saidaEsperada == 1.0) {
                verdadeiroPositivo++;
            } else if (predicao == 0.0 && saidaEsperada == 0.0) {
                verdadeiroNegativo++;
            } else if (predicao == 1.0 && saidaEsperada == 0.0) {
                falsoPositivo++;
            } else if (predicao == 0.0 && saidaEsperada == 1.0) {
                falsoNegativo++;
            }
        }

        System.out.println("\n--- 4. Matriz de Confusão ---");
        System.out.println("                | Predito 0   | Predito 1   |");
        System.out.println("----------------|-------------|-------------|");
        System.out.println(" Verdadeiro 0   | VN: " + verdadeiroNegativo + "       | FP: " + falsoPositivo + "       |");
        System.out.println(" Verdadeiro 1   | FN: " + falsoNegativo + "       | VP: " + verdadeiroPositivo + "       |");
        System.out.println("---------------------------------------------");
        
        int acertos = verdadeiroPositivo + verdadeiroNegativo;
        double taxaAcerto = ((double) acertos / entradasTeste.length) * 100;
        System.out.printf("Taxa de Acerto no Teste: %.2f%%\n", taxaAcerto);
    }
}
