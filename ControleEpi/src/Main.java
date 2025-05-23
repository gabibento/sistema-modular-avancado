import gerenciadores.GerenciadorDevolucao;
import gerenciadores.GerenciadorEmprestimo;
import gerenciadores.GerenciadorEpi;
import gerenciadores.GerenciadorUsuario;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    static final Scanner scanner = new Scanner(System.in);
    static List<String> logs = new ArrayList<>();
    static final GerenciadorEpi gerenciadorEpi = new GerenciadorEpi(logs);
    static final GerenciadorUsuario gerenciadorUsuario = new GerenciadorUsuario(logs);
    static final GerenciadorEmprestimo gerenciadorEmprestimo = new GerenciadorEmprestimo(gerenciadorUsuario, gerenciadorEpi, logs);
    static final GerenciadorDevolucao gerenciadorDevolucao = new GerenciadorDevolucao(gerenciadorEmprestimo, logs);

    public static void main(String[] args) {
        processarMenu();
        scanner.close();
        exibirLogs();
    }

    private static void processarMenu() {
        while (true) {
            int opcao = escolherMenuPrincipal();
            if (opcao == 0) break;
            if(opcao == 5) {
                exibirLogs();
                continue;
            }

            processarOpcaoPrincipal(opcao);
        }
    }
    private static void exibirLogs() {
        if (logs.isEmpty()) {
            System.out.println("Nenhuma ação registrada.");
        } else {
            System.out.println("=== LOG DE AÇÕES ===");
            logs.forEach(System.out::println);
        }
    }

    private static int escolherMenuPrincipal() {
        while (true) {
            try {
                System.out.println("\n1. CRUD de Usuários\n" +
                        "2. CRUD de EPIs\n" +
                        "3. CRUD de Empréstimos\n" +
                        "4. CRUD de Devoluções\n" +
                        "5. Exibir Logs\n" +
                        "0. Sair");
                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine();
                return opcao;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite um número inteiro.");
                scanner.nextLine();
            }
        }
    }

    private static int escolherCRUD() {
        while (true) {
            try {
                System.out.println("\n1. Cadastrar\n" +
                        "2. Listar\n" +
                        "3. Atualizar\n" +
                        "4. Remover\n" +
                        "5. Buscar por nome parcial\n" +
                        "6. Voltar");
                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine();
                return opcao;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite um número inteiro.");
                scanner.nextLine();
            }
        }
    }

    private static void processarOpcaoPrincipal(int opcao) {
        while (true) {
            int opcaoCRUD = escolherCRUD();
            if (opcaoCRUD == 6) break;

            switch (opcao) {
                case 1 -> processarCRUDUsuario(opcaoCRUD);
                case 2 -> processarCRUDEpi(opcaoCRUD);
                case 3 -> processarCRUDEmprestimo(opcaoCRUD);
                case 4 -> processarCRUDDevolucao(opcaoCRUD);
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void processarCRUDUsuario(int opcao) {
        switch (opcao) {
            case 1 -> gerenciadorUsuario.cadastrarUsuario();
            case 2 -> gerenciadorUsuario.listarUsuarios();
            case 3 -> gerenciadorUsuario.atualizarUsuario();
            case 4 -> gerenciadorUsuario.removerUsuario();
            case 5 -> gerenciadorUsuario.exibirResultadoBusca();
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void processarCRUDEpi(int opcao) {
        switch (opcao) {
            case 1 -> gerenciadorEpi.cadastrarEpi();
            case 2 -> gerenciadorEpi.listarEpis();
            case 3 -> gerenciadorEpi.atualizarEpi();
            case 4 -> gerenciadorEpi.removerEpi();
            case 5 -> gerenciadorEpi.exibirResultadoBusca();
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void processarCRUDEmprestimo(int opcao) {
        switch (opcao) {
            case 1 -> gerenciadorEmprestimo.criarEmprestimo();
            case 2 -> gerenciadorEmprestimo.listarEmprestimos();
            case 3 -> gerenciadorEmprestimo.atualizarEmprestimo();
            case 4 -> gerenciadorEmprestimo.removerEmprestimo();
            case 5 -> gerenciadorEmprestimo.exibirResultadoBusca();
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void processarCRUDDevolucao(int opcao) {
        switch (opcao) {
            case 1 -> gerenciadorDevolucao.criarDevolucao();
            case 2 -> gerenciadorDevolucao.listarDevolucoes();
            case 3 -> gerenciadorDevolucao.atualizarDevolucao();
            case 4 -> gerenciadorDevolucao.removerDevolucao();
            case 5 -> gerenciadorDevolucao.exibirResultadoBusca();
            default -> System.out.println("Opção inválida.");
        }
    }

}
