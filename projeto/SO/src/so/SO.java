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
    static Quadro[] MP = new Quadro[TAM_MAX_QUADROS_MP];
    static ArrayList<Tab_Pag> Tab_Pag_Master = new ArrayList();//limitaremos para seu len n passar de 32
    static ArrayList<Tab_Pag> Tab_Pag_Master_R = new ArrayList();
    static ArrayList<Processo> Tab_Processos = new ArrayList();
    static ArrayList<Processo> Tab_Processos_R = new ArrayList();
    static ArrayList<int[]> Mem_Vazia = new ArrayList<int[]>();
    static ArrayList<int[]> Mem_Vazia_R = new ArrayList<int[]>();
    static Pagina[] Mem_Sec = new Pagina[TAM_MAX_PAGINAS_MS];
    static int clock_stack = 0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Mem_Vazia.add(new int[2]);
        Mem_Vazia.get(0)[0] = 0;
        Mem_Vazia.get(0)[1] = TAM_MAX_QUADROS_MP;
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
                int Tabela = Integer.parseInt(Description.substring(0, 5), 2);
                int Pagina = Integer.parseInt(Description.substring(6, 14), 2);
                //fazendo por enderecamento de tabelas:
                for(Processo P: Tab_Processos){
                    if(P.getNome().equals(Process_Name)){
                        if(P.getEstado().equals("Suspenso-Pronto")){
                            P.setEstado("Executando");
                            for(int i = 0; i<3;i++){
                                int Quadro = aloca();
                                int Count = 0;
                                for(int j = 0; i<TAM_MAX_PAGINAS_MS && Count<3 ;j++){
                                    if(Mem_Sec[j] != null && Mem_Sec[j].getProcesso().equals(Process_Name)){
                                        MP[Quadro].setConteudo(Mem_Sec[j].getConteudo()); Count ++;//carrega 3 paginas iniciais do processo caso ele estivesse suspenso
                                    }
                                }
                            }
                        }
                        if(P.getEstado().equals("Pronto")){
                        P.setEstado("Executando");
                        
                            if(! (Tab_Pag_Master.get(Tabela).getPaginas()[Pagina].isP())){
                                System.out.println("processo nao esta em MP");
                                int Quadro = aloca();
                                for(int i = 0; i<TAM_MAX_PAGINAS_MS;i++){
                                    if(Mem_Sec[i] != null && Mem_Sec[i].getProcesso().equals(Process_Name) &&Mem_Sec[i].getPagina() == Integer.parseInt(Description.substring(0, 14), 2)){
                                        MP[Quadro].setConteudo(Mem_Sec[i].getConteudo());
                                    }
                                }
                                MP[Quadro].setBit_U(true);
                                MP[Quadro].setLRU(System.currentTimeMillis());
                                Tab_Pag_Master.get(Tabela).getPaginas()[Pagina].setP(true);
                                Tab_Pag_Master.get(Tabela).getPaginas()[Pagina].setM(false);}
                        }

                //
                //busca processo na tabela de paginas
                /*for(Processo P: Tab_Processos){
                    if(P.getNome().equals(Process_Name)){
                        P.setEstado("Executando");
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
                                clock_stack = (Quadro+1)%TAM_MAX_QUADROS_MP;

                                //politica de substituicao LRU ou Relogio
                                //retorna quadro disponivel
                                for(int i = 0; i<TAM_MAX_PAGINAS_MS;i++){
                                if(Mem_Sec[i] != null && Mem_Sec[i].getProcesso().equals(Process_Name) &&Mem_Sec[i].getPagina() == Pagina){
                                    Mem_Sec[i].setConteudo(MP[Endereco].getConteudo());
                                }

                                aux.setP(true);
                                aux.setQuadro(Quadro);
                                }
                            }
                        }
                    }*/
                    }
                }



            }


            else if(Command.equals("I")){
                System.out.println("E/S");
                Description = entrada.split(" ")[2];
                //seta estado pra bloqueado
                 for(Processo P: Tab_Processos){
                    if(P.getNome().equals(Process_Name)){
                        if(P.getEstado().equals("Suspenso-Pronto")) P.setEstado("Suspenso-Bloqueado");
                        else P.setEstado("Bloqueado");
                    }
                 }
                //

                //solicita ES, altera estado do processo quando concluido
                Thread Request = new Thread(new ES(Integer.parseInt(Description, 2), Process_Name));
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
                            if(C.isP()) MP[C.getQuadro()] = new Quadro();
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
                            else Mem_Sec[i] = null;
                        }
                    }
                    else i = TAM_MAX_PAGINAS_MS;
                }
                //
            }
                
                
            
            System.out.println(Tab_Processos);
       
        }
    }
    
    
    
     static public int Relogio(){
        int Endereco = -1;
        while(MP[clock_stack%TAM_MAX_QUADROS_MP].isBit_U()){
            MP[clock_stack%TAM_MAX_QUADROS_MP].setBit_U(false);
            clock_stack++;
        }
        clock_stack = clock_stack%TAM_MAX_QUADROS_MP;
        Endereco = clock_stack;
        Contexto(Endereco);
        return Endereco;
    }
     
     
    static public int LRU(){
        //procura o menos recente
        long min = System.currentTimeMillis();
        int Endereco = -1;
        for(int i = 0; i<TAM_MAX_QUADROS_MP;i++){
            if(MP[i] != null && MP[i].getLRU() < min){
                Endereco = i;
                min = MP[i].getLRU();
            }
        }
        //
        Contexto(Endereco);
        return Endereco;
    }
    
    
    static public void Contexto(int Endereco){
         //atualiza tabela do processo e salva em MS se foi modificado
        if(MP[Endereco] != null){
            String Processo = MP[Endereco].getProcesso();
            int Pagina = MP[Endereco].getPagina();
            for(Tab_Pag T: Tab_Pag_Master){
                if(T.getNome().equals(Processo) && T.getTab_Count() == Pagina/TAM_MAX_TABELA){
                    T.getPaginas()[Pagina%TAM_MAX_TABELA].setP(false);
                    if(T.getPaginas()[Pagina%TAM_MAX_TABELA].isM()){
                        for(int i = 0; i<TAM_MAX_PAGINAS_MS;i++){
                            if(Mem_Sec[i] != null && Mem_Sec[i].getProcesso().equals(Processo) &&Mem_Sec[i].getPagina() == Pagina){
                                Mem_Sec[i].setConteudo(MP[Endereco].getConteudo());
                            }
                        }
                    T.getPaginas()[Pagina%TAM_MAX_TABELA].setM(false);
                    }
                    T.getPaginas()[Pagina%TAM_MAX_TABELA].setQuadro(-1);
                }
            }
        }
    }
    static public int aloca(){
    int Quadro;
    if(Mem_Vazia.isEmpty()){
        Random gerador = new Random(19700621);
        if(gerador.nextInt(2)== 0)Quadro = LRU();
        else Quadro = Relogio();
        }
    else {Quadro = Mem_Vazia.get(0)[0];
        if (Mem_Vazia.get(0)[1] > 1){
            Mem_Vazia.get(0)[0] += 1;
            Mem_Vazia.get(0)[1] -= 1;
            }
        else Mem_Vazia.remove(0);
        }
    clock_stack = (Quadro+1)%TAM_MAX_QUADROS_MP;
    return Quadro;
}
}




