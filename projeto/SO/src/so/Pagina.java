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
public class Pagina implements Comparable {
    private String Processo;
    private String Conteudo;
    private int Pagina;
    
    


    public Pagina() {
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
    

    @Override
    public int compareTo(Object o) {
        if (o == null) return 1000;
        if(o instanceof Pagina){Pagina comparestu = (Pagina) o;
        if(comparestu.getProcesso().equals(this.Processo)) return this.Pagina - comparestu.getPagina();
        else return comparestu.getProcesso().compareTo(this.Processo);}
        return 1000;
    }
}
