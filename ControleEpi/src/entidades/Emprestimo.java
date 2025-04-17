package entidades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Emprestimo {

    private Epi epi;
    private Usuario usuario;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;

    public Emprestimo(Epi epi, Usuario usuario, LocalDate dataDevolucao) {
        this.epi = epi;
        this.usuario = usuario;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucao = dataDevolucao;
    }

    public Epi getEpi() {
        return epi;
    }

    public void setEpi(Epi epi) {
        this.epi = epi;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    @Override
    public String toString() {
        return "EPI: " + epi.getNome() +
                ", usuário: " + usuario.getNome() +
                ", data de empréstimo: " + dataEmprestimo.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ", data de devolução prevista: " + dataDevolucao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

}
