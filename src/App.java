import rede.RedeNeural;
import dados.GerenciarDados;

public class App {
    public static void main(String[] args) throws Exception {
        
        // sementes para garantir a reprodutibilidade
        long seedDosDados = 70L;
        long seedDaRede = 40L;

        System.out.println("=============================================");
        System.out.println(" INICIANDO TREINAMENTO DA REDE NEURAL (MLP)");
        System.out.println("=============================================\n");

        GerenciarDados dados = new GerenciarDados(seedDosDados);
        double[][] entradasTreino = dados.getEntradasTreino();
        double[][] saidasTreino = dados.getSaidasTreino();
        double[][] entradasTeste = dados.getEntradasTeste();
        double[][] saidasTeste = dados.getSaidasTeste();

        RedeNeural rede = new RedeNeural(seedDaRede);

        // dados de treinamento
        rede.treinar(entradasTreino, saidasTreino, 20000, 0.2, 0.001);

        System.out.println("\n=============================================");
        System.out.println(" FASE DE TESTE (GENERALIZAÇÃO)");
        System.out.println("=============================================");

        int verdadeiroPositivo = 0;
        int verdadeiroNegativo = 0;
        int falsoPositivo = 0;
        int falsoNegativo = 0;

        for (int i = 0; i < entradasTeste.length; i++) {
            double[] entrada = entradasTeste[i];
            double saidaEsperada = saidasTeste[i][0];

            double predicaoContinua = rede.feedforward(entrada);
            double predicao = (predicaoContinua >= 0.5) ? 1.0 : 0.0;
            
            String status = (predicao == saidaEsperada) ? "ACERTOU" : "ERROU";
            System.out.printf("Fruta [%.2f, %.2f] -> Esperado: %.1f | Previsto: %.1f (Cru: %.4f) [%s]\n", 
                              entrada[0], entrada[1], saidaEsperada, predicao, predicaoContinua, status);

            if (predicao == 1.0 && saidaEsperada == 1.0) verdadeiroPositivo++;
            else if (predicao == 0.0 && saidaEsperada == 0.0) verdadeiroNegativo++;
            else if (predicao == 1.0 && saidaEsperada == 0.0) falsoPositivo++;
            else if (predicao == 0.0 && saidaEsperada == 1.0) falsoNegativo++;
        }

        System.out.println("\n--- Matriz de Confusão ---");
        System.out.println("                | Predito 0   | Predito 1   |");
        System.out.println("----------------|-------------|-------------|");
        System.out.println(" Verdadeiro 0   | VN: " + verdadeiroNegativo + "       | FP: " + falsoPositivo + "       |");
        System.out.println(" Verdadeiro 1   | FN: " + falsoNegativo + "       | VP: " + verdadeiroPositivo + "       |");
        System.out.println("---------------------------------------------");
        
        int acertos = verdadeiroPositivo + verdadeiroNegativo;
        double taxaAcerto = ((double) acertos / entradasTeste.length) * 100;
        System.out.printf(">>> TAXA DE ACERTO NA GENERALIZAÇÃO: %.2f%%\n", taxaAcerto);
    }
}