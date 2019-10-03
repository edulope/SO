/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so;

/**
 *
 * @author Joâo Pedro
 */
public class Processo {
    private final String nome;
    private final int tamanho;
    private String estado;

    public Processo(String nome, int tamanho) {
        this.nome = nome;
        this.tamanho = tamanho;
        this.estado = "iniciando";
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Processo{" + "nome=" + nome + ", tamanho=" + tamanho + ", estado=" + estado + '}';
    }

    public String getNome() {
        return nome;
    }

    public int getTamanho() {
        return tamanho;
    }
    
}
