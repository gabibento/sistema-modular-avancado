package gerenciadores;

import entidades.Epi;

import java.io.*;
import java.util.*;

public class GerenciadorEpi {
    Scanner scanner = new Scanner(System.in);
    private List<Epi> epis;
    private List<String> logs;
    private static String log;
    private final String CAMINHO_ARQUIVO = "epis.txt";

    public GerenciadorEpi(List<String> logs) {
        epis = new ArrayList<>();
        this.logs = logs;
        carregarDoArquivo();
    }

    public void cadastrarEpi() {
        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            System.out.print("Quantidade: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine();

            Epi epi = new Epi(nome, quantidade);
            epis.add(epi);

            salvarNoArquivo();
            log = "EPI " + epi.getNome() + " adicionada com sucesso!";
            System.out.println(log);
            logs.add(log);
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar EPI");
            scanner.nextLine();
        }
    }

    public void listarEpis() {
        if (epis.isEmpty()) System.out.println("Não há EPIs cadastradas");
        else epis.forEach(epi -> System.out.println((epis.indexOf(epi) + 1) + ": " + epi.toString()));
    }

    public Epi buscarEpi() {
        while (true) {
            try {
                listarEpis();
                if (epis.isEmpty()) return null;

                System.out.print("Digite o índice do EPI: ");
                int indice = scanner.nextInt();
                scanner.nextLine();

                Epi epi = epis.get(indice - 1);
                return epi;
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Índice inválido. Tente novamente.");
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número válido.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void atualizarEpi() {
        Epi epi = buscarEpi();
        while (true) {
            try {
                if (epi == null) break;

                System.out.print("1. Atualizar nome \n2. Atualizar quantidade\n3. Voltar\nDigite uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine();

                if (opcao == 1) {
                    System.out.print("\nNovo nome: ");
                    epi.setNome(scanner.nextLine());
                } else if (opcao == 2) {
                    System.out.print("Nova quantidade: ");
                    epi.setQuantidade(scanner.nextInt());
                    scanner.nextLine();
                } else if (opcao == 3) break;
                else {
                    throw new IllegalArgumentException("Opção inválida. Tente novamente: ");
                }

                salvarNoArquivo();
                log = "EPI " + epi.getNome() + " atualizada com sucesso!";
                System.out.println(log);
                logs.add(log);
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número válido.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void removerEpi() {
        try {
            Epi epi = buscarEpi();
            if (epi != null) {
                epis.remove(epi);
                salvarNoArquivo();

                log = "EPI " + epi.getNome() + " removida com sucesso!";
                System.out.println(log);
                logs.add(log);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void salvarNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (Epi epi : epis) {
                writer.write(epi.getNome() + ": " + epi.getQuantidade());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar no arquivo: " + e.getMessage());
        }
    }

    private void carregarDoArquivo() {
        File file = new File(CAMINHO_ARQUIVO);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(": ");
                if (partes.length == 2) {
                    String nome = partes[0];
                    int quantidade = Integer.parseInt(partes[1]);
                    epis.add(new Epi(nome, quantidade));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar dados do arquivo: " + e.getMessage());
        }
    }
}
