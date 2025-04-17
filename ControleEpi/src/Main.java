import gerenciadores.GerenciadorDevolucao;
import gerenciadores.GerenciadorEmprestimo;
import gerenciadores.GerenciadorEpi;
import gerenciadores.GerenciadorUsuario;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static final Scanner scanner = new Scanner(System.in);
    static final GerenciadorEpi gerenciadorEpi = new GerenciadorEpi();
    static final GerenciadorUsuario gerenciadorUsuario = new GerenciadorUsuario();
    static final GerenciadorEmprestimo gerenciadorEmprestimo = new GerenciadorEmprestimo(gerenciadorUsuario, gerenciadorEpi);
    static final GerenciadorDevolucao gerenciadorDevolucao = new GerenciadorDevolucao(gerenciadorEmprestimo);

    public static void main(String[] args) {
        processarMenu();
        scanner.close();
    }

    private static void processarMenu() {
        while (true) {
            int opcao = escolherMenuPrincipal();
            if (opcao == 0) break;

            processarOpcaoPrincipal(opcao);
        }
    }

    private static int escolherMenuPrincipal() {
        while (true) {
            try {
                System.out.println("\n1. CRUD de Usuários\n" +
                        "2. CRUD de EPIs\n" +
                        "3. CRUD de Empréstimos\n" +
                        "4. CRUD de Devoluções\n" +
                        "0. Sair");
                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine();  // Limpar o buffer
                return opcao;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite um número inteiro.");
                scanner.nextLine();  // Limpar o buffer
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
                        "5. Voltar");
                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine();  // Limpar o buffer
                return opcao;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite um número inteiro.");
                scanner.nextLine();  // Limpar o buffer
            }
        }
    }

    private static void processarOpcaoPrincipal(int opcao) {
        while (true) {
            int opcaoCRUD = escolherCRUD();
            if (opcaoCRUD == 5) break;

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
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void processarCRUDEpi(int opcao) {
        switch (opcao) {
            case 1 -> gerenciadorEpi.cadastrarEpi();
            case 2 -> gerenciadorEpi.listarEpis();
            case 3 -> gerenciadorEpi.atualizarEpi();
            case 4 -> gerenciadorEpi.removerEpi();
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void processarCRUDEmprestimo(int opcao) {
        switch (opcao) {
            case 1 -> gerenciadorEmprestimo.criarEmprestimo();
            case 2 -> gerenciadorEmprestimo.listarEmprestimos();
            case 3 -> gerenciadorEmprestimo.atualizarEmprestimo();
            case 4 -> gerenciadorEmprestimo.removerEmprestimo();
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void processarCRUDDevolucao(int opcao) {
        switch (opcao) {
            case 1 -> gerenciadorDevolucao.criarDevolucao();
            case 2 -> gerenciadorDevolucao.listarDevolucoes();
            case 3 -> gerenciadorDevolucao.atualizarDevolucao();
            case 4 -> gerenciadorDevolucao.removerDevolucao();
            default -> System.out.println("Opção inválida.");
        }
    }
}
