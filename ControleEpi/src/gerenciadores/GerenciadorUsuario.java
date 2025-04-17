package gerenciadores;

import entidades.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GerenciadorUsuario {
    Scanner scanner = new Scanner(System.in);
    private List<Usuario> usuarios;
    private final String CAMINHO_ARQUIVO = "usuarios.txt";

    public GerenciadorUsuario() {
        usuarios = new ArrayList<>();
        carregarDoArquivo();
    }

    public void cadastrarUsuario() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        Usuario usuario = new Usuario(nome, email);
        usuarios.add(usuario);
        salvarNoArquivo();

        System.out.println("Usuário cadastrado com sucesso!");
    }

    public void listarUsuarios() {
        if (usuarios.isEmpty()) System.out.println("Não há usuários cadastrados");
        else usuarios.forEach(usuario -> System.out.println((usuarios.indexOf(usuario) + 1) + ": " + usuario.toString()));
    }

    public Usuario buscarUsuario() {
        while (true) {
            try {
                listarUsuarios();
                if (usuarios.isEmpty()) return null;

                System.out.print("Digite o índice do usuário: ");
                int indice = scanner.nextInt();

                Usuario usuario = usuarios.get(indice - 1);
                scanner.nextLine();

                return usuario;
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

    public void atualizarUsuario() {
        Usuario usuario = buscarUsuario();

        while (true) {
            try {
                if (usuario == null) break;

                System.out.print("1. Atualizar nome\n2. Atualizar e-mail\n3. Voltar\nDigite uma opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine();

                if (opcao == 1) {
                    System.out.print("Novo nome: ");
                    usuario.setNome(scanner.nextLine());
                } else if (opcao == 2) {
                    System.out.print("Novo e-mail: ");
                    usuario.setEmail(scanner.nextLine());
                } else if (opcao == 3) break;
                else {
                    throw new IllegalArgumentException("Opção inválida. Tente novamente!");
                }

                salvarNoArquivo();
                System.out.println("Usuário atualizado com sucesso!\n");
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número válido.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void removerUsuario() {
        try {
            Usuario usuario = buscarUsuario();
            if (usuario != null) {
                usuarios.remove(usuario);
                salvarNoArquivo();
                System.out.println("Usuário removido com sucesso!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void salvarNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (Usuario usuario : usuarios) {
                writer.write(usuario.getNome() + ": " + usuario.getEmail());
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
                    String email = partes[1];
                    usuarios.add(new Usuario(nome, email));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados do arquivo: " + e.getMessage());
        }
    }
}
