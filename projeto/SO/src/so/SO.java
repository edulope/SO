/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;



/**
 *
 * @author Joâo Pedro
 */
public class SO {
    static Scanner teclado = new Scanner(System.in);
    static final int TAM_MAX_QUADROS_MP = 1024;
    static final int TAM_MAX_QUADROS_MS = 4096;
    static final int TAM_MAX_TABELA = 256;
    static Quadro[] Quadros = new Quadro[TAM_MAX_QUADROS_MP];
    static ArrayList<Tab_Pag> Tab_Pag_Master = new ArrayList();
    static ArrayList<Tab_Pag> Tab_Pag_Master_R = new ArrayList();
    static ArrayList<Processo> Tab_Processos = new ArrayList();
    static ArrayList<Processo> Tab_Processos_R = new ArrayList();
    static Pagina[] Mem_Sec = new Pagina[TAM_MAX_QUADROS_MS];
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String Process_Name;
        String Command;
        String Description;
        String entrada = ""; 
        while(!(entrada.equals("E"))){
        entrada = teclado.nextLine();
        Process_Name = entrada.split(" ")[0];
        Command = entrada.split(" ")[1];
        
        
        
        if(Command.equals("P")){
            System.out.println("EXECUTANDO");
            Description = entrada.split(" ")[2];
            //busca processo na tabela de paginas
            for(Processo P: Tab_Processos){
                if(P.getNome().equals(Process_Name)){
                    P.setEstado("Executando");
                    int tabela = (int) (P.getTamanho()/TAM_MAX_TABELA);
                    for(Tab_Pag T: Tab_Pag_Master){
                        if(T.getNome().equals(Process_Name) && T.getTab_Count()== tabela){
                            Componente_TP aux = T.getPaginas()[P.getTamanho()%TAM_MAX_TABELA];
                            if(aux.isP()) System.out.println("executa instrucao do quadro " + aux.getQuadro());
                            
                            else{
                            System.out.println("processo nao esta em MP");
                            Random gerador = new Random(19700621);
                            int Quadro;
                            if(gerador.nextInt(2)== 0)Quadro = LRU();
                            else Quadro = Relogio();
                            
                            //politica de substituicao LRU ou Relogio
                            //retorna quadro disponivel
                            
                            }
                        }
                    }
                }
             }
            //
            
            
            //seta estado pra executando
            for(Processo P: Tab_Processos){
                if(P.getNome().equals(Process_Name)){
                    P.setEstado("Executando");
                }
             }
            //
            
        }
        
        
        else if(Command.equals("I")){
            System.out.println("E/S");
            //seta estado pra bloqueado
             for(Processo P: Tab_Processos){
                if(P.getNome().equals(Process_Name)){
                    if(P.getEstado().equals("Suspenso-Pronto")) P.setEstado("Suspenso-Bloqueado");
                    else P.setEstado("Bloqueado");
                }
             }
            //
            
            //solicita ES, altera estado do processo quando concluido
            Thread Request = new Thread(new ES(0, Process_Name));
            Request.start();   
            //
            
        }
        
        
        
        
        
        else if(Command.equals("C")){
            System.out.println("CRIANDO");
            Tab_Processos.add(new Processo("P1", 2));
            Tab_Processos.add(new Processo("P2", 3));
            Tab_Processos.add(new Processo("P1", 4));
        }
        
        
        else if(Command.equals("R")){
            System.out.println("LEITURA");
            
        }
        else if(Command.equals("W")){
            System.out.println("ESCRITA");
        }
        
        
        
        
        else if(Command.equals("T")){
            System.out.println("TERMINADO");
            //APAGA PROCESSO DA TABELA DE PROCESSOS
            for(Processo P: Tab_Processos){
                if(P.getNome().equals(Process_Name))Tab_Processos_R.add(P);
            }
            Tab_Processos.removeAll(Tab_Processos_R);
            Tab_Processos_R.clear();
            //
            
            //APAGA TABELA DE PAGINAS DO PROCESSO E SEUS QUADROS EM MEMORIA
            for(Tab_Pag T: Tab_Pag_Master){
                if(T.getNome().equals(Process_Name)){Tab_Pag_Master_R.add(T);
                    for(Componente_TP C: T.getPaginas()){
                        if(C.isP()) Quadros[C.getQuadro()] = new Quadro();
                    }
                }
            }
            Tab_Pag_Master.removeAll(Tab_Pag_Master_R);
            Tab_Pag_Master_R.clear();
            //
            
            //APAGA PROCESSO DA MEMORIA SECUNDARIA
            for(int i = 0; i<TAM_MAX_QUADROS_MS; i++){
                if(Mem_Sec[i] != null){
                    if(Mem_Sec[i].getProcesso().equals(Process_Name))
                        {if(i+1 < TAM_MAX_QUADROS_MS){
                            Mem_Sec[i] = Mem_Sec[i+1];
                            i--;
                        }
                        else Mem_Sec[i] = null;}
                }
                else i = TAM_MAX_QUADROS_MS;
            }
            //
        }
        System.out.println(Tab_Processos);
       }
    }
}




