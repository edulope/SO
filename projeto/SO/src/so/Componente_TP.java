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
public class Componente_TP {
    private boolean P;
    private boolean M;
    private int Quadro;
    private boolean usado;
    /*P e M comecam com zero, quando processo e criado ele geralmente nao esta
    com todas as paginas em memoria
    alem disso, quadro = -1, indica que nao esta em memoria*/
    public Componente_TP(){
        this.P = false;
        this.M = false;
        this.Quadro = -1;
        usado = true;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }

    public boolean isP() {
        return P;
    }

    public void setP(boolean P) {
        this.P = P;
    }

    public boolean isM() {
        return M;
    }

    public void setM(boolean M) {
        this.M = M;
    }

    public int getQuadro() {
        return Quadro;
    }

    public void setQuadro(int Quadro) {
        this.Quadro = Quadro;
    }

    @Override
    public String toString() {
        return "Componente_TP{" + "P=" + P + ", M=" + M + ", Quadro=" + Quadro + '}';
    }
    
    
}
