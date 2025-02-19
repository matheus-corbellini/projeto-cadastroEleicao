import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Locale;

public class ACMEVoting {
    private Candidatura candidatura;
    private CadastroPartido cadastroPartido;
    private Scanner entrada;
    private PrintStream saidaPadrao = System.out;
    private final String nomeArquivoEntrada = "input.txt";
    private final String nomeArquivoSaida = "output.txt";

    public ACMEVoting() {
        this.candidatura = new Candidatura();
        this.cadastroPartido = new CadastroPartido();
        redirecionaEntrada();
        redirecionaSaida();
    }

    private void redirecionaEntrada() {
        try {
            BufferedReader streamEntrada = new BufferedReader(new InputStreamReader(new FileInputStream(nomeArquivoEntrada), "UTF-8"));
            entrada = new Scanner(streamEntrada);
        } catch (Exception e) {
            System.out.println("Erro ao abrir o arquivo de entrada: " + e.getMessage());
        }
        Locale.setDefault(Locale.ENGLISH);
        entrada.useLocale(Locale.ENGLISH);
    }

    private void redirecionaSaida() {
        try {
            PrintStream streamSaida = new PrintStream(new File(nomeArquivoSaida));
            System.setOut(streamSaida);
        } catch (Exception e) {
            System.err.println("Erro ao redirecionar a saída: " + e.getMessage());
        }
    }

    public void executar() {
        // 1 - Cadastro de Partidos
        while (entrada.hasNextInt()) {
            int numeroPartido = entrada.nextInt();
            entrada.nextLine(); 
            if (numeroPartido == -1) break;
            String nomePartido = entrada.nextLine(); 
            Partido partido = new Partido(numeroPartido, nomePartido);
            if (cadastroPartido.cadastraPartido(partido)) {
                System.out.println("1:" + partido.getNumero() + "," + partido.getNome());
            }
        }

        // 2 - Cadastro de Candidatos
        while (entrada.hasNextInt()) {
            int numeroCandidato = entrada.nextInt();
            int numeroPartido;
            if (numeroCandidato > 100) {
                numeroPartido = numeroCandidato / 1000;
            }else {
                numeroPartido = numeroCandidato;
            }
            entrada.nextLine(); 
            if (numeroCandidato == -1) break;
            String nomeCandidato = entrada.nextLine(); 
            String municipioCandidato = entrada.nextLine(); 
            Candidato candidato = new Candidato(numeroCandidato, nomeCandidato, municipioCandidato);
            Partido partido = cadastroPartido.consultaPartido(numeroPartido);
            if (partido != null) {
                if (candidatura.cadastraCandidato(candidato)) {
                    partido.adicionaCandidato(candidato);
                    System.out.println("2:" + candidato.getNumero() + "," + candidato.getNome() + "," + candidato.getMunicipio());
                }
            }
        }

       // 3 - Adicionar Votos
        while (entrada.hasNextInt()) {
            int numeroCandidato = entrada.nextInt();
            entrada.nextLine(); 
            if (numeroCandidato == -1) break;
            String municipioCandidato = entrada.nextLine(); 
            int votos = entrada.nextInt(); 
            Candidato candidatoEncontrado = null;
            for (int i = 0; i < candidatura.getCandidatos().size(); i++) {
                Candidato c = candidatura.getCandidatos().get(i);
                if (c.getNumero() == numeroCandidato && c.getMunicipio().equals(municipioCandidato)) {
                    candidatoEncontrado = c;
                    c.adicionarVotos(votos);
                    System.out.println("3:" + c.getNumero() + "," + c.getMunicipio() + "," + c.getVotos());
                    break;
                }
            }
        }


        // 4 - Consulta de Partido por Número
        int numeroPartidoConsulta = entrada.nextInt();
        Partido partidoConsultado = cadastroPartido.consultaPartido(numeroPartidoConsulta);
        if (partidoConsultado != null) {
            System.out.println("4:" + partidoConsultado.getNumero() + "," + partidoConsultado.getNome());
        } else {
            System.out.println("4:Nenhum partido encontrado.");
        }


        // 5 - Consulta de Candidato por Número e Município
        int numeroCandidatoConsulta = entrada.nextInt();
        entrada.nextLine(); 
        String municipioCandidatoConsulta = entrada.nextLine();
        Candidato candidatoConsultado = candidatura.consultaCandidato(numeroCandidatoConsulta, municipioCandidatoConsulta);
        if (candidatoConsultado != null) {
            System.out.println("5:" + candidatoConsultado.getNumero() + "," + candidatoConsultado.getNome() + "," + candidatoConsultado.getMunicipio() + "," + candidatoConsultado.getVotos());
        } else {
            System.out.println("5:Nenhum candidato encontrado.");
        }


        // 6 - Exibir Prefeitos de um Partido
        if (entrada.hasNextLine()) {
            String nomePartidoConsulta = entrada.nextLine();
            Partido partidoPrefeitos = cadastroPartido.consultaPartido(nomePartidoConsulta);
            if (partidoPrefeitos != null) {
                boolean prefeitoEncontrado = false;
                for (int i = 0; i < partidoPrefeitos.getCandidatos().size(); i++) {
                    Candidato candidato = partidoPrefeitos.getCandidatos().get(i);
                    if (String.valueOf(candidato.getNumero()).length() == 2) { 
                        System.out.println("6:" + partidoPrefeitos.getNome() + "," + candidato.getNumero() + "," + candidato.getNome() + "," + candidato.getMunicipio() + "," + candidato.getVotos());
                        prefeitoEncontrado = true;
                    }
                }
                if (!prefeitoEncontrado) {
                    System.out.println("6:Nenhum prefeito encontrado.");
                }
            } else {

                System.out.println("6:Nenhum partido encontrado.");
            }
        }

        // 7 - Mostrar os dados do partido com mais candidatos
        if (cadastroPartido.getPartidos().size() > 0) {
            Partido partidoMaisCandidatos = null;
            int maxCandidatos = -1;
            for (int i = 0; i < cadastroPartido.getPartidos().size(); i++) {
                Partido p = cadastroPartido.getPartidos().get(i);
                int numCandidatos = p.getCandidatos().size();
    
                if (numCandidatos > maxCandidatos) {
                    partidoMaisCandidatos = p;
                    maxCandidatos = numCandidatos;
                }
            }
            if (partidoMaisCandidatos != null) {
                System.out.println("7:" + partidoMaisCandidatos.getNumero() + "," + partidoMaisCandidatos.getNome() + "," + maxCandidatos);
            } else {
                System.out.println("7:Nenhum partido com candidatos.");
            }
        } else {
            System.out.println("7:Nenhum partido cadastrado.");
        }


        // 8 - Candidatos Mais Votados
        Candidato prefeitoMaisVotado = null;
        Candidato vereadorMaisVotado = null;
        for (int i = 0; i < candidatura.getCandidatos().size(); i++) {
            Candidato candidato = candidatura.getCandidatos().get(i);
            if (String.valueOf(candidato.getNumero()).length() == 2) {  
                if (prefeitoMaisVotado == null || candidato.getVotos() > prefeitoMaisVotado.getVotos()) {
                    prefeitoMaisVotado = candidato;
                }
            } else if (String.valueOf(candidato.getNumero()).length() == 5) {  
                if (vereadorMaisVotado == null || candidato.getVotos() > vereadorMaisVotado.getVotos()) {
                    vereadorMaisVotado = candidato;
                }
            }
        }

        if (prefeitoMaisVotado != null) {
            System.out.println("8:" + prefeitoMaisVotado.getNumero() + "," + prefeitoMaisVotado.getNome() + "," + prefeitoMaisVotado.getMunicipio() + "," + prefeitoMaisVotado.getVotos());
        } else {
            System.out.println("8:Nenhum prefeito encontrado.");
        }

        if (vereadorMaisVotado != null) {
            System.out.println("8:" + vereadorMaisVotado.getNumero() + "," + vereadorMaisVotado.getNome() + "," + vereadorMaisVotado.getMunicipio() + "," + vereadorMaisVotado.getVotos());
        } else {
            System.out.println("8:Nenhum vereador encontrado.");
        }



        //9 - Mostrar o partido com mais votos de vereadores
        Partido partidoMaisVotosVereadores = null;
        int maiorQuantidadeVotosVereadores = 0;
        for(int i=0; i<cadastroPartido.getPartidos().size(); i++){
            Partido partido = cadastroPartido.getPartidos().get(i);
            int votosVereadores = 0;

            for(int j=0; j<partido.getCandidatos().size(); j++){
            Candidato candidato = partido.getCandidatos().get(j);
                if(String.valueOf(candidato.getNumero()).length()==5){
                votosVereadores += candidato.getVotos();
                }
            }

            if(votosVereadores > maiorQuantidadeVotosVereadores){
            partidoMaisVotosVereadores = partido;
            maiorQuantidadeVotosVereadores = votosVereadores;
            }
        }

        if(partidoMaisVotosVereadores!=null){
            System.out.println("9:" + partidoMaisVotosVereadores.getNumero() + "," + partidoMaisVotosVereadores.getNome() + "," + maiorQuantidadeVotosVereadores);
        }else{
            System.out.println("9:Nenhum partido com vereadores.");
        }
        

        //10 - Mostrar o município com maior quantidade de votos
        String municipioMaisVotos = null;
        int maiorQtVotos = 0;
        for(int i=0; i<candidatura.getCandidatos().size(); i++){
            Candidato cAtual = candidatura.getCandidatos().get(i);
            String mAtual = cAtual.getMunicipio();
            int votosMunicipio = 0;

            for(int j=0; j<candidatura.getCandidatos().size(); j++){
                Candidato candidatoComparando = candidatura.getCandidatos().get(i);
                if(candidatoComparando.getMunicipio().equals(mAtual)){
                    votosMunicipio += candidatoComparando.getVotos();
                }
            }

            if(votosMunicipio>maiorQtVotos){
                maiorQtVotos = votosMunicipio;
                municipioMaisVotos = mAtual; 
            }
        }

        if(municipioMaisVotos != null){
            System.out.println("10:" + municipioMaisVotos + "," + maiorQtVotos);
        } else {
            System.out.println("10:Nenhum município com votos.");
        }
    }
}
