package gerenciadores;

import entidades.Devolucao;
import entidades.Emprestimo;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GerenciadorDevolucao {
    Scanner scanner = new Scanner(System.in);
    private List<Devolucao> devolucoes;
    private List<String> logs;
    private static String log;
    private final String CAMINHO_ARQUIVO = "devolucoes.txt";

    GerenciadorEmprestimo gerenciadorEmprestimo;

    public GerenciadorDevolucao(GerenciadorEmprestimo gerenciadorEmprestimo, List<String> logs) {
        devolucoes = new ArrayList<>();
        this.gerenciadorEmprestimo = gerenciadorEmprestimo;
        this.logs = logs;
        carregarDoArquivo();
    }

    public void criarDevolucao(){
        System.out.println("Empréstimo: ");
        Emprestimo emprestimo = gerenciadorEmprestimo.buscarEmprestimo();

        try{
            System.out.print("Digite a data de devolução (DD/MM/AAAA): ");
            LocalDate data = gerenciadorEmprestimo.buscarData();

            Devolucao devolucao = new Devolucao(emprestimo, data);
            devolucoes.add(devolucao);
            salvarNoArquivo();
            log = "Devolução criada com sucesso!";
            System.out.println(log);
            logs.add(log);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void listarDevolucoes() {
        if(devolucoes.isEmpty()) {
            System.out.println("Não há devoluções cadastradas");
        } else {
            devolucoes.forEach(devolucao ->
                    System.out.println((devolucoes.indexOf(devolucao) + 1) + ": " + devolucao.toString()));
        }
    }

    public Devolucao buscarDevolucao(){
        while(true){
            try {
                listarDevolucoes();
                if(devolucoes.isEmpty()) return null;

                System.out.print("Digite o índice da devolução: ");
                int indice = scanner.nextInt();

                Devolucao devolucao = devolucoes.get(indice - 1);
                scanner.nextLine();

                if (devolucao == null) throw new IllegalArgumentException("Erro ao encontrar a devolução. Tente novamente!");
                return devolucao;
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

    public void atualizarDevolucao(){
        Devolucao devolucao = buscarDevolucao();
        while(true) {
            try {
                if (devolucao == null) break;

                System.out.print("1. Atualizar empréstimo\n2. Atualizar data de devolução\n3. Voltar\nDigite uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine();

                if (opcao == 1) {
                    devolucao.setEmprestimo(gerenciadorEmprestimo.buscarEmprestimo());
                } else if (opcao == 2) {
                    System.out.print("Digite a data de devolução (DD/MM/AAAA): ");
                    devolucao.setDataDevolucao(gerenciadorEmprestimo.buscarData());
                } else if (opcao == 3) break;
                else {
                    throw new IllegalArgumentException("Opção inválida. Tente novamente!");
                }
                salvarNoArquivo();
                log = "Devolução atualizada com sucesso!";
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

    public void removerDevolucao(){
        try {
            Devolucao devolucao = buscarDevolucao();
            if(devolucao != null){
                devolucoes.remove(devolucao);
                salvarNoArquivo();
                log = "Devolução removida com sucesso!";
                System.out.println(log);
                logs.add(log);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void salvarNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (Devolucao devolucao : devolucoes) {
                String linha = devolucao.getEmprestimo().getEpi().getNome() + ": " +
                        devolucao.getDataDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                writer.write(linha);
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
                    String nomeEpi = partes[0];
                    LocalDate dataDevolucao = LocalDate.parse(partes[1], DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                    Emprestimo emprestimo = gerenciadorEmprestimo.buscarEmprestimoPorNomeEpi(nomeEpi);

                    if (emprestimo != null) {
                        devolucoes.add(new Devolucao(emprestimo, dataDevolucao));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados do arquivo: " + e.getMessage());
        }
    }
}
