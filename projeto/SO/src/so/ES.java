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
public class ES implements Runnable {
    final int pedido;
    final String Process;
    
    public ES(int pedido, String Process){
        this.pedido = pedido;
        this.Process = Process;
    }
    
    @Override
    public void run () {
        System.out.println("ES DO PROCESSO " + Process + " CONCLUIDO");
        System.out.println(SO.Mem_Sec[pedido]);
        for(Processo P: SO.Tab_Processos){
            
                if(P.getNome().equals(Process)){
                    if(P.getEstado().equals("Suspenso-bloqueado")) P.setEstado("Suspenso-pronto");
                    else P.setEstado("pronto");
                }

        }
    }
}
