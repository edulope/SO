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
public class Tab_Pag {
    private final String nome;
    private Componente_TP[] Paginas;/* = new Componente_TP[TAM_MAX];??? possivel implementacao: criando a lista a partir do numero maximo da pagina
    final int TAM_MAX = 100;*/
    private final int Tab_Count; /*caso o processo tenha mais de uma tabela de paginas associado*/
    

    public Tab_Pag(String nome, int tamanho, int count) {
        this.nome = nome;
        Paginas = new Componente_TP[SO.TAM_MAX_TABELA];
        for(Componente_TP c: Paginas){
            c = new Componente_TP();
        }
        for(int i = tamanho; i<SO.TAM_MAX_TABELA; i++)Paginas[i] = null;
        this.Tab_Count = count;
    }

    public String getNome() {
        return nome;
    }

    public Componente_TP[] getPaginas() {
        return Paginas;
    }

    public int getTab_Count() {
        return Tab_Count;
    }

    public void setPaginas(Componente_TP[] Paginas) {
        this.Paginas = Paginas;
    }
    
    
    
    
    
}
