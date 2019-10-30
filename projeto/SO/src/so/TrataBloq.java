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
public class TrataBloq implements Runnable {

    
    
    @Override
    synchronized public void run () {
       
           if(!(SO.ListaBloq.isEmpty())){
               Bloqueado pedido;
               pedido = SO.ListaBloq.get(0);
               if(pedido.getPedido().equals("I")){
                    //Thread.sleep(5000);
                    System.out.println("ES DO PROCESSO " + pedido.getNome() + " CONCLUIDO");
                    int end = Integer.parseInt(pedido.getEndereco().substring(0, 12), 2);
                    if(SO.Mem_Sec[end%SO.TAM_MAX_PAGINAS_MS] != null && SO.Mem_Sec[end%SO.TAM_MAX_PAGINAS_MS].getPagina()!= -1)System.out.println(SO.Mem_Sec[end%SO.TAM_MAX_PAGINAS_MS]);
                    else System.out.println("Endereco nao utilizado");
                    for(Processo P: SO.Tab_Processos){
                        if(P.getNome().equals(pedido.getNome())){
                            if(P.getEstado().equals("Suspenso-Bloqueado")) P.setEstado("Suspenso-Pronto");
                        else P.setEstado("Pronto");
                        }
                    }
                    SO.ListaBloq.remove(pedido);
               }
               else if(pedido.getPedido().equals("R")){
                        int Tabela = Integer.parseInt(pedido.getEndereco().substring(0, 5), 2); 
                        int Pagina = Integer.parseInt(pedido.getEndereco().substring(5, 13), 2); 
                        if(SO.Tab_Pag_Master.size()>Tabela && SO.Tab_Pag_Master.get(Tabela).getNome().equals(pedido.getNome())){
                            Componente_TP paginaTP = SO.Tab_Pag_Master.get(Tabela%SO.TAM_MAX_TABELA).getPaginas()[Pagina];
                            int Quadro = -1;
                            if(paginaTP != null && !paginaTP.isP()){     
                                Quadro = SO.aloca();
                                SO.bota_em_MP(Quadro, Tabela*256+Pagina, pedido.getNome());
                                SO.atualiza_tab(Quadro, Tabela*256+Pagina, pedido.getNome());
                            }
                            else if(paginaTP != null && paginaTP.isP()){
                                Quadro = paginaTP.getQuadro();
                            }
                            if(Quadro != -1){
                                System.out.println("O conteúdo lido é: " + SO.MP[Quadro].getConteudo());
                            }
                            for(Processo P: SO.Tab_Processos){
                                if(P.getNome().equals(pedido.getNome())){
                                    if(P.getEstado().equals("Bloqueado"))P.setEstado("Pronto");
                                    if(P.getEstado().equals("Suspenso-Bloqueado"))P.setEstado("Suspenso-Pronto");
                                }
                            }
                        }
                else System.out.println("O endereco solicitado nao pertence ao Processo");
                SO.ListaBloq.remove(pedido);
               }
               else if(pedido.getPedido().equals("W")){
                    int Tabela = Integer.parseInt(pedido.getEndereco().substring(0, 5), 2); 
                    int Pagina = Integer.parseInt(pedido.getEndereco().substring(5, 13), 2);
                    if(SO.Tab_Pag_Master.size()>Tabela && SO.Tab_Pag_Master.get(Tabela).getNome().equals(pedido.getNome())){
                        Componente_TP paginaTP = SO.Tab_Pag_Master.get(Tabela%SO.TAM_MAX_TABELA).getPaginas()[Pagina];
                        int Quadro = -1;
                        if(paginaTP != null && !paginaTP.isP()){     
                        Quadro = SO.aloca();
                        SO.bota_em_MP(Quadro, Tabela*256+Pagina, pedido.getNome());
                        SO.atualiza_tab(Quadro, Tabela*256+Pagina, pedido.getNome());
                        }
                        else if(paginaTP != null && paginaTP.isP()){
                            Quadro = paginaTP.getQuadro();
                        }
                        if(paginaTP != null && Quadro != -1){
                            SO.MP[Quadro].setConteudo(pedido.getEscrita());
                            paginaTP.setM(true);
                            System.out.println("O conteúdo escrito é: " + SO.MP[Quadro].getConteudo());
                        }
                        for(Processo P: SO.Tab_Processos){
                            if(P.getNome().equals(pedido.getNome())){
                                if(P.getEstado().equals("Bloqueado"))P.setEstado("Pronto");
                                if(P.getEstado().equals("Suspenso-Bloqueado"))P.setEstado("Suspenso-Pronto");
                            }
                        }
                    }
                else System.out.println("O endereco solicitado nao pertence ao Processo");
                SO.ListaBloq.remove(pedido);
               }
           }
       
    }
}