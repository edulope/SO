/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.lang.*;

/*
Paginas: 1 kb
tamanho maximo da memoria 1024*1kb
tamanho maximo da ms 4096*1kb
se um processo pode ter no maximo 1 tabela, entao tam maximo de um processo = 256*1kb
*/

/*Tab_Pag_Master(similar a tabela de segmentos, vai ser um array de tabelas de paginas)
em cada tabela existe um array de paginas(Componente_TP)todas de um mesmo processo 
os 10(1kb) bits menos significativos enderecam o deslocamento na pagina, em seguida 8 bits enderecam a pagina na tabela(max = 256), 
depois os 5 bits enderecam a tabela, logo 5 + 8 + 10 = tamanho do endereco logico
*/


/**
 *
 * @author Joâo Pedro
 */
public class SO {
    static Scanner teclado = new Scanner(System.in);
    static final int TAM_MAX_QUADROS_MP = 1024;
    static final int TAM_MAX_PAGINAS_MS = 4096;
    static final int TAM_MAX_TABELA = 256;
    static Quadro[] Quadros = new Quadro[TAM_MAX_QUADROS_MP];
    static ArrayList<Tab_Pag> Tab_Pag_Master = new ArrayList();//limitaremos para seu len n passar de 32
    static ArrayList<Tab_Pag> Tab_Pag_Master_R = new ArrayList();
    static ArrayList<Processo> Tab_Processos = new ArrayList();
    static ArrayList<Processo> Tab_Processos_R = new ArrayList();
    static Pagina[] Mem_Sec = new Pagina[TAM_MAX_PAGINAS_MS];
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String Process_Name;
        String Command;
        String Description;
        String entrada = ""; 
        while(!(entrada.equals("E"))){
        System.out.println("bem vindo ao SO, suas opcoes sao:");
        System.out.println("P\nEx. P1 P (1024)2 --> é uma instrução executada em CPU (pode ser uma soma ou subtração) que está no endereço lógico (1024)2");
        System.out.println("I\nEx. P1 I disco --> agora será executado uma instrução de entrada e saída pedido por P1 em disco");
        System.out.println("R\nEx. P1 R 1024 --> leitura no endereço lógico 1024");
        System.out.println("W\nEx. P1 W 1024 100 --> grava o valor 100 no endereço lógico 1024");
        System.out.println("C\nEx. P1 C 320 MB --> cria P1 com 320 M bytes");
        System.out.println("T\nEx. P1 T --> processo P1 termina");
        System.out.println("ou E, para desligar o SO");
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
            for(int i = 0; i<TAM_MAX_PAGINAS_MS; i++){
                if(Mem_Sec[i] != null){
                    if(Mem_Sec[i].getProcesso().equals(Process_Name))
                        {if(i+1 < TAM_MAX_PAGINAS_MS){
                            Mem_Sec[i] = Mem_Sec[i+1];
                            i--;
                        }
                        else Mem_Sec[i] = null;}
                }
                else i = TAM_MAX_PAGINAS_MS;
            }
            //
        }
        System.out.println(Tab_Processos);
       }
    }
    
    
    
    //AINDA NAO CODEI O RELOGIO
     static public int Relogio(){
        //procura o menos recente
        long min = System.currentTimeMillis();
        int Endereco = -1;
        for(int i = 0; i<TAM_MAX_QUADROS_MP;i++){
            if(Quadros[i] != null && Quadros[i].getLRU() < min){
                Endereco = i;
                min = Quadros[i].getLRU();
            }
        }
        //
        Contexto(Endereco);
        return Endereco;
    }
     
     
    static public int LRU(){
        //procura o menos recente
        long min = System.currentTimeMillis();
        int Endereco = -1;
        for(int i = 0; i<TAM_MAX_QUADROS_MP;i++){
            if(Quadros[i] != null && Quadros[i].getLRU() < min){
                Endereco = i;
                min = Quadros[i].getLRU();
            }
        }
        //
        Contexto(Endereco);
        return Endereco;
    }
    
    
    static public void Contexto(int Endereco){
         //atualiza tabela do processo e salva se foi modificado
        if(Quadros[Endereco] != null){
            String Processo = Quadros[Endereco].getProcesso();
            int Pagina = Quadros[Endereco].getPagina();
            for(Tab_Pag T: Tab_Pag_Master){
                if(T.getNome().equals(Processo) && T.getTab_Count() == Pagina/TAM_MAX_TABELA){
                    T.getPaginas()[Pagina%TAM_MAX_TABELA].setP(false);
                    if(T.getPaginas()[Pagina%TAM_MAX_TABELA].isM()){
                        for(int i = 0; i<TAM_MAX_PAGINAS_MS;i++){
                            if(Mem_Sec[i] != null && Mem_Sec[i].getProcesso().equals(Processo) &&Mem_Sec[i].getPagina() == Pagina){
                                Mem_Sec[i].setConteudo(Quadros[Endereco].getConteudo());
                            }
                        }
                    T.getPaginas()[Pagina%TAM_MAX_TABELA].setM(false);
                    }
                    T.getPaginas()[Pagina%TAM_MAX_TABELA].setQuadro(-1);
                }
            }
        }
        //
    }
}




