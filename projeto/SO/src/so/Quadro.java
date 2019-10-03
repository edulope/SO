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
public class Quadro {
    private String Processo;
    private String Conteudo;
    private int Pagina;
    private boolean Bit_U;
    private long LRU;

    public long getLRU() {
        return LRU;
    }

    public void setLRU(long LRU) {
        this.LRU = LRU;
    }

    public boolean isBit_U() {
        return Bit_U;
    }

    public void setBit_U(boolean Bit_U) {
        this.Bit_U = Bit_U;
    }

    public Quadro() {
        this.Pagina = -1;
        this.Conteudo = "";
        this.Processo = "";
    }

    public String getProcesso() {
        return Processo;
    }

    public void setProcesso(String Processo) {
        this.Processo = Processo;
    }

    public String getConteudo() {
        return Conteudo;
    }

    public void setConteudo(String Conteudo) {
        this.Conteudo = Conteudo;
    }

    public int getPagina() {
        return Pagina;
    }

    public void setPagina(int Pagina) {
        this.Pagina = Pagina;
    }

    @Override
    public String toString() {
        return "Quadro{" + "Processo=" + Processo + ", Conteudo=" + Conteudo + ", Pagina=" + Pagina + '}';
    }
    
}
