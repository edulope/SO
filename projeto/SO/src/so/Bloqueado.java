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
public class Bloqueado {
    private String nome;
    private String pedido;
    private String endereco;
    private String escrita;

    @Override
    public String toString() {
        return "Bloqueado{" + "nome=" + nome + ", pedido=" + pedido + ", endereco=" + endereco + ", escrita=" + escrita + '}';
    }

    public Bloqueado(String nome, String pedido, String endereco, String escrita) {
        this.nome = nome;
        this.pedido = pedido;
        this.endereco = endereco;
        this.escrita = escrita;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEscrita() {
        return escrita;
    }

    public void setEscrita(String escrita) {
        this.escrita = escrita;
    }
    
    
    
}
