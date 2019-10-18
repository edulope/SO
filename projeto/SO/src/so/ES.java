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
        //Thread.sleep(5000);
        System.out.println("ES DO PROCESSO " + Process + " CONCLUIDO");
        if(SO.Mem_Sec[pedido%SO.TAM_MAX_PAGINAS_MS] != null && SO.Mem_Sec[pedido%SO.TAM_MAX_PAGINAS_MS].getPagina()!= -1)System.out.println(SO.Mem_Sec[pedido%SO.TAM_MAX_PAGINAS_MS]);
        else System.out.println("Endereco nao utilizado");
        for(Processo P: SO.Tab_Processos){
            if(P.getNome().equals(Process)){
                if(P.getEstado().equals("Suspenso-Bloqueado")) P.setEstado("Suspenso-Pronto");
                else P.setEstado("Pronto");
            }
        }
    }
}
