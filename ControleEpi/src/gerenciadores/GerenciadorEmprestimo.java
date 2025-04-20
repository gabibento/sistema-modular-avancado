package gerenciadores;

import entidades.Emprestimo;
import entidades.Epi;
import entidades.Usuario;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GerenciadorEmprestimo {
    Scanner scanner = new Scanner(System.in);
    private List<Emprestimo> emprestimos;
    private List<String> logs;
    private final String CAMINHO_ARQUIVO = "emprestimos.txt";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static String log;

    GerenciadorUsuario gerenciadorUsuario;
    GerenciadorEpi gerenciadorEpi;

    public GerenciadorEmprestimo(GerenciadorUsuario gerenciadorUsuario, GerenciadorEpi gerenciadorEpi, List<String> logs) {
        emprestimos = new ArrayList<>();
        this.gerenciadorUsuario = gerenciadorUsuario;
        this.gerenciadorEpi = gerenciadorEpi;
        this.logs = logs;
        carregarDoArquivo();
    }

    public void criarEmprestimo() {
        try {
            System.out.println("Colaboradores: ");
            Usuario usuario = gerenciadorUsuario.buscarUsuario();

            System.out.println("EPIs");
            Epi epi = gerenciadorEpi.buscarEpi();

            if (usuario != null && epi != null) {
                if (!validarEmprestimo(epi)) throw new RuntimeException("Não há " + epi.getNome() + " suficientes");

                System.out.print("Digite a data de devolução prevista (DD/MM/AAAA): ");
                Emprestimo novoEmprestimo = new Emprestimo(epi, usuario, buscarData());
                emprestimos.add(novoEmprestimo);
                epi.setQuantidade(epi.getQuantidade() - 1);
                salvarNoArquivo();
                log = "Empréstimo criado com sucesso!";
                System.out.println(log);
                logs.add(log);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean validarEmprestimo(Epi epi) {
        return epi.getQuantidade() > 0;
    }

    public void listarEmprestimos() {
        if (emprestimos.isEmpty()) System.out.println("Não há empréstimos cadastrados");
        else emprestimos.forEach(emprestimo -> System.out.println((emprestimos.indexOf(emprestimo) + 1) + ": " + emprestimo.toString()));
    }

    public Emprestimo buscarEmprestimo() {
        while (true) {
            try {
                listarEmprestimos();
                if (emprestimos.isEmpty()) return null;

                System.out.print("Digite o índice do empréstimo: ");
                int indice = scanner.nextInt();

                Emprestimo emprestimo = emprestimos.get(indice - 1);
                scanner.nextLine();

                if (emprestimo == null) throw new IllegalArgumentException("Erro ao encontrar empréstimo. Tente novamente!");
                return emprestimo;
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

    public LocalDate buscarData() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Entrada vazia. Tente novamente.");
                    continue;
                }
                return LocalDate.parse(input, formatter);
            } catch (Exception e) {
                System.out.print("Formato inválido. Use o formato DD/MM/AAAA. Digite novamente: ");
            }
        }
    }

    public void atualizarEmprestimo() {
        Emprestimo emprestimo = buscarEmprestimo();
        while (true) {
            try {
                if (emprestimo == null) break;

                System.out.print("1. Atualizar usuário\n" +
                        "2. Atualizar EPI\n" +
                        "3. Atualizar data de empréstimo\n" +
                        "4. Atualizar data de devolução\n" +
                        "5. Voltar\nDigite uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine();

                if (opcao == 5) break;

                switch (opcao) {
                    case 1 -> emprestimo.setUsuario(gerenciadorUsuario.buscarUsuario());
                    case 2 -> {
                        Epi epiAtual = emprestimo.getEpi();
                        Epi novaEpi = gerenciadorEpi.buscarEpi();

                        if (!validarEmprestimo(novaEpi)) throw new RuntimeException("Não há " + novaEpi.getNome() + " suficientes");

                        novaEpi.setQuantidade(novaEpi.getQuantidade() - 1);
                        epiAtual.setQuantidade(epiAtual.getQuantidade() + 1);

                        emprestimo.setEpi(novaEpi);
                    }
                    case 3 -> {
                        System.out.print("Digite a data de empréstimo (DD/MM/AAAA): ");
                        emprestimo.setDataEmprestimo(buscarData());
                    }
                    case 4 -> {
                        System.out.print("Digite a data de devolução prevista (DD/MM/AAAA): ");
                        emprestimo.setDataDevolucao(buscarData());
                    }
                    default -> throw new IllegalArgumentException("Opção inválida. Tente novamente!");
                }

                salvarNoArquivo();
                log = "Empréstimo atualizado com sucesso!";
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

    public void removerEmprestimo() {
        try {
            Emprestimo emprestimo = buscarEmprestimo();
            if (emprestimo != null) {
                emprestimos.remove(emprestimo);
                emprestimo.getEpi().setQuantidade(emprestimo.getEpi().getQuantidade() + 1);
                salvarNoArquivo();
                log = "Empréstimo removido com sucesso!";
                System.out.println(log);
                logs.add(log);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public Emprestimo buscarEmprestimoPorNomeEpi(String nomeEpi) {
        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getEpi().getNome().equals(nomeEpi)) {
                return emprestimo;
            }
        }
        return null;
    }

    private void salvarNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (Emprestimo emprestimo : emprestimos) {
                writer.write(
                        emprestimo.getEpi().getNome() + ";" +
                                emprestimo.getUsuario().getNome() + ";" +
                                emprestimo.getUsuario().getEmail() + ";" +
                                emprestimo.getDataEmprestimo().format(formatter) + ";" +
                                emprestimo.getDataDevolucao().format(formatter)
                );
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
                String[] partes = linha.split(";");
                if (partes.length == 5) {
                    String nomeEpi = partes[0];
                    String nomeUsuario = partes[1];
                    String emailUsuario = partes[2];
                    LocalDate dataDevolucao = LocalDate.parse(partes[4], formatter);

                    Epi epi = new Epi(nomeEpi, 0);
                    Usuario usuario = new Usuario(nomeUsuario, emailUsuario);

                    emprestimos.add(new Emprestimo(epi, usuario, dataDevolucao));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados do arquivo: " + e.getMessage());
        }
    }
}
