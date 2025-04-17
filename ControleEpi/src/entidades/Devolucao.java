package entidades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Devolucao {
    private Emprestimo emprestimo;
    private LocalDate dataDevolucao;

    public Devolucao(Emprestimo emprestimo, LocalDate dataDevolucao) {
        this.emprestimo = emprestimo;
        this.dataDevolucao = dataDevolucao;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    @Override
    public String toString() {
        return "Empréstimo: " + emprestimo.getEpi().getNome() +
                ", data de devolução: " + dataDevolucao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}

