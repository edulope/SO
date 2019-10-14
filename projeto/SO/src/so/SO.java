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
import static java.lang.Math.pow;
import java.util.Collections;

/*
Paginas: 1 mb
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
    static final int TAM_MAX_PAGINAS_MS = 4*1024;
    static final int TAM_MAX_TABELA = 256;
    static Quadro[] MP = new Quadro[TAM_MAX_QUADROS_MP];
    static ArrayList<Tab_Pag> Tab_Pag_Master = new ArrayList();//limitaremos para seu len n passar de 32
    static ArrayList<Tab_Pag> Tab_Pag_Master_R = new ArrayList();
    static ArrayList<Processo> Tab_Processos = new ArrayList();
    static ArrayList<Processo> Tab_Processos_R = new ArrayList();
    /*static ArrayList<int[]> Mem_Vazia = new ArrayList<int[]>();
    static ArrayList<int[]> Mem_Vazia_R = new ArrayList<int[]>();*/
    static Pagina[] Mem_Sec = new Pagina[TAM_MAX_PAGINAS_MS];
    static int clock_stack = 0;

    /*static public void Mem_List_Opt(){
        for(int i = 0;i<Mem_Vazia.size();i++){
            if(Mem_Vazia.get(i)[0] == -1) Mem_Vazia_R.add(Mem_Vazia.get(i));
            for(int j = i+1;j<Mem_Vazia.size();j++){
                if( (!Mem_Vazia_R.contains(Mem_Vazia.get(j))) && Mem_Vazia.get(i)[0] + Mem_Vazia.get(i)[1] == Mem_Vazia.get(j)[0]){
                    int[] aux = new int[2];
                    aux[0] = Mem_Vazia.get(i)[0];
                    aux[1] = Mem_Vazia.get(i)[1] + Mem_Vazia.get(j)[1];
                    Mem_Vazia.set(i, aux);
                    Mem_Vazia_R.add(Mem_Vazia.get(j));
                }
           }
        }
        Mem_Vazia.removeAll(Mem_Vazia_R);
   }*/
    
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
    int Quadro = -1;
    for(int j = 0; j<TAM_MAX_QUADROS_MP;j++){
    if(MP[j] == null || MP[j].getPagina() == -1) {Quadro = j; j=TAM_MAX_QUADROS_MP;}
    }
    if(Quadro == -1){
    Random gerador = new Random(19700621);
    if(gerador.nextInt(2)== 0)Quadro = LRU();
    else Quadro = Relogio();
    }
    /*Quadro = Mem_Vazia.get(0)[0];
           if (Mem_Vazia.get(0)[1] > 1){
               int[] aux = new int[2];       
               aux[0] = Mem_Vazia.get(0)[0] + 1;
               aux[1] = Mem_Vazia.get(0)[1] - 1;
               Mem_Vazia.set(0, aux);
               }
           else Mem_Vazia.remove(0);
    */
    clock_stack = (Quadro+1)%TAM_MAX_QUADROS_MP;
    return Quadro;
   }
   
   
   static public void bota_em_MP(int quadro, int pagina, String Process_Name){
       for(int j = 0; j<TAM_MAX_PAGINAS_MS ;j++){
           if(SO.Mem_Sec[j] != null && SO.Mem_Sec[j].getProcesso().equals(Process_Name) && pagina == SO.Mem_Sec[j].getPagina()){
               Quadro q = new Quadro();
               q.setBit_U(true);
               q.setLRU(System.currentTimeMillis());
               q.setConteudo(Mem_Sec[j].getConteudo());
               q.setPagina(Mem_Sec[j].getPagina());
               q.setProcesso(Mem_Sec[j].getProcesso());
               SO.MP[quadro] = q;
           }
       }
   }
   
   static public void atualiza_tab(int Quadro, int pagina, String Process_Name){
       for(Tab_Pag T: Tab_Pag_Master){
           if(T.getNome().equals(Process_Name) && T.getTab_Count() == pagina/TAM_MAX_TABELA){
               T.getPaginas()[pagina%TAM_MAX_TABELA] = new Componente_TP();
               T.getPaginas()[pagina%TAM_MAX_TABELA].setM(false);
               T.getPaginas()[pagina%TAM_MAX_TABELA].setP(true);
               T.getPaginas()[pagina%TAM_MAX_TABELA].setQuadro(Quadro);
           }
       }
   }

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*Mem_Vazia.add(new int[2]);
        Mem_Vazia.get(0)[0] = 0;
        Mem_Vazia.get(0)[1] = TAM_MAX_QUADROS_MP;*/
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
            if(Process_Name.equals("E"))break;
            Command = entrada.split(" ")[1];
            
            


            if(Command.equals("P")){
                System.out.println("EXECUTANDO");
                Description = entrada.split(" ")[2];
/*                int Tabela = Integer.parseInt(Description.substring(0, 5), 2);
                int Pagina = Integer.parseInt(Description.substring(6, 14), 2);
*/                //fazendo por enderecamento de tabelas:
                for(Processo P: Tab_Processos){
                    if(P.getNome().equals(Process_Name)){
                        if(P.getEstado().equals("Suspenso-Pronto")){
                            P.setEstado("Pronto");
                            for(int i = 0; i<3;i++){
                                int Quadro = -1;
                                /*if(! Mem_Vazia.isEmpty()){
                                    Quadro = Mem_Vazia.get(0)[0];
                                    int[] aux = Mem_Vazia.get(0);
                                    aux[0] += 1;
                                    aux[1] -= 1;
                                    if(0 == aux[1]) Mem_Vazia.remove(0);
                                    else Mem_Vazia.set(0, aux);
                                }*/
                                for(int j = 0; j<TAM_MAX_QUADROS_MP;j++){
                                    if(MP[j] == null || MP[j].getPagina() == -1) {Quadro = j; j=TAM_MAX_QUADROS_MP;}
                                }   
                                if(Quadro == -1) Quadro = aloca();
                                bota_em_MP(Quadro, i, Process_Name);
                                atualiza_tab(Quadro, i, Process_Name);
                            }
                        }
                        if(P.getEstado().equals("Pronto")){
                            P.setEstado("Executando");
                            for(Tab_Pag T: Tab_Pag_Master){
                                if(T.getNome().equals(Process_Name) && T.getTab_Count() == Integer.parseInt(Description)/TAM_MAX_TABELA){
                                    if(T.getPaginas()[Integer.parseInt(Description)%TAM_MAX_TABELA] != null && T.getPaginas()[Integer.parseInt(Description)%TAM_MAX_TABELA].isP()) System.out.println("EM MEMORIA, EXECUTANDO");
                                    else{
                                        int Quadro = -1;
                                        for(int j = 0; j<TAM_MAX_QUADROS_MP;j++){
                                            if(MP[j] == null || MP[j].getPagina() == -1) {Quadro = j; j=TAM_MAX_QUADROS_MP;}
                                        }
                                        if(Quadro == -1) Quadro = aloca();
                                        bota_em_MP(Quadro, Integer.parseInt(Description), Process_Name);
                                        atualiza_tab(Quadro, Integer.parseInt(Description), Process_Name);
                                        System.out.println("vindo da MP, agora em memoria");
                                    }
                                }
                            }
                        }
                    }
                }
            }
                        /*  if(!(Tab_Pag_Master.get(Tabela).getNome().equals(Process_Name)){ System.out.println("processo nao tem permissao para acessar tal pagina); break;}
                            else{if(! (Tab_Pag_Master.get(Tabela).getPaginas()[Pagina].isP())){
                                System.out.println("processo nao esta em MP");
                                int Quadro = aloca();
                                MP[Quadro] = new Quadro();
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
                        

                        for(Tab_Pag T: Tab_Pag_Master){
                            if(T.getNome().equals(Process_Name) && T.getTab_Count()== Integer.parseInt(Description, 2)/TAM_MAX_TABELA){
                                try {Componente_TP aux = T.getPaginas()[Integer.parseInt(Description, 2)%TAM_MAX_TABELA];
                                    int Quadro;    
                                    if(aux.isP())Quadro = aux.getQuadro();

                                    else{
                                        System.out.println("processo nao esta em MP");
                                        Random gerador = new Random(19700621);
                                        if(gerador.nextInt(2)== 0)Quadro = LRU();
                                        else Quadro = Relogio();
                                        clock_stack = (Quadro+1)%TAM_MAX_QUADROS_MP;

                                        //politica de substituicao LRU ou Relogio
                                        //retorna quadro disponivel
                                        for(int i = 0; i<TAM_MAX_PAGINAS_MS;i++){
                                            if(Mem_Sec[i] != null && Mem_Sec[i].getProcesso().equals(Process_Name) &&Mem_Sec[i].getPagina() == Integer.parseInt(Description, 2)/TAM_MAX_TABELA){
                                                MP[Quadro].setConteudo(Mem_Sec[i].getConteudo());
                                            }
                                        }
                                    }
                                MP[Quadro].setBit_U(true);
                                MP[Quadro].setLRU(System.currentTimeMillis());
                                aux.setP(true);
                                aux.setM(false);
                                }
                                catch(Exception e){System.out.println("endereco logico inexistente");}
                            }
                        }*/

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





            else if(Command.equals("C")){//cria processo em ms, adiciona na tabela de processos, adiciona na tabela de paginas
                System.out.println("CRIANDO");
                Description = entrada.split(" ")[2];
                int Tamanho =  Integer.parseInt(Description);
                Description = entrada.split(" ")[3];
                if(Description.equals("B")) Tamanho = 1;
                else if (Description.equals("KB")) Tamanho = 1;
                else if (Description.equals("MB")) Tamanho = Tamanho * 1;
                else if (Description.equals("GB")) Tamanho = Tamanho * 1024;
                int aux = 0;
                while(aux<TAM_MAX_PAGINAS_MS && Mem_Sec[aux] != null)aux++;
                int N_Tab = Tamanho/TAM_MAX_TABELA;
                if(Tamanho%TAM_MAX_TABELA != 0)N_Tab ++;
                System.out.println("tamanho" + Tamanho +  "ponto inicial" + aux + "MAX MS" + TAM_MAX_PAGINAS_MS+ "paginas " + N_Tab + "TAM_PAG M" + Tab_Pag_Master.size()   );
                if(aux + Tamanho <= TAM_MAX_PAGINAS_MS && Tab_Pag_Master.size() + N_Tab <= 32){
                    for(int i = aux; i<aux+Tamanho;i++){
                        Mem_Sec[i] = new Pagina();
                        Mem_Sec[i].setPagina(i-aux);
                        Mem_Sec[i].setProcesso(Process_Name);
                    }
                    Tab_Processos.add(new Processo(Process_Name, Tamanho));
                    for(int i = 0; i<N_Tab;i++){
                        if(i + 1 == N_Tab){
                            Tab_Pag_Master.add(new Tab_Pag(Process_Name,Tamanho%TAM_MAX_TABELA,i));
                            for(int j = 0; j<Tamanho%TAM_MAX_TABELA;j++)Tab_Pag_Master.get(Tab_Pag_Master.size()-1).getPaginas()[j] = new Componente_TP();
                        }
                        else {Tab_Pag_Master.add(new Tab_Pag(Process_Name,TAM_MAX_TABELA,i));
                            for(int j = 0; j<TAM_MAX_TABELA;j++)Tab_Pag_Master.get(Tab_Pag_Master.size()-1).getPaginas()[j] = new Componente_TP();
                        }
                    }
                    ///if(Mem_Vazia.get(0)[1] < 3)Tab_Processos.get(Tab_Processos.size()-1).setEstado("Suspenso-Pronto");
                    int count = 0;
                    for(int j = 0; j<TAM_MAX_QUADROS_MP;j++){
                        if(MP[j] == null || MP[j].getPagina() == -1) count ++;
                    }
                    if(count < 3)Tab_Processos.get(Tab_Processos.size()-1).setEstado("Suspenso-Pronto");
                    else {Tab_Processos.get(Tab_Processos.size()-1).setEstado("Pronto");
                    
                        for(int i = 0; i<3;i++){
                            int Quadro = -1;
                            for(int j = 0; j<TAM_MAX_QUADROS_MP;j++){
                                if(MP[j] == null || MP[j].getPagina() == -1) {Quadro = j; j=TAM_MAX_QUADROS_MP;}
                            }
                            /*Quadro = Mem_Vazia.get(0)[0];
                            int[] auxX = Mem_Vazia.get(0);
                            auxX[0] += 1;
                            auxX[1] -= 1;
                            if(0 == auxX[1]) Mem_Vazia.remove(0);
                            else Mem_Vazia.set(0, auxX);*/
                            bota_em_MP(Quadro, i, Process_Name);
                            atualiza_tab(Quadro, i, Process_Name);
                        }
                    }
                }
                else System.out.println("NAO HA ESPACO EM DISCO");     
            }


            else if(Command.equals("R")){
                Description = entrada.split(" ")[2];
                int Tabela = Integer.parseInt(Description.substring(0, 5), 2);
                int Pagina = Integer.parseInt(Description.substring(6, 14), 2);
                int offset = Integer.parseInt(Description.substring(15, 29), 2);

                
                

            }
            else if(Command.equals("W")){
                System.out.println("ESCRITA");
            }




            else if(Command.equals("T")){
                System.out.println("TERMINANDO");
                //APAGA PROCESSO DA TABELA DE PROCESSOS
                int tamanho = 0;
                 for(Processo P: Tab_Processos){
                    if(P.getNome().equals(Process_Name)){Tab_Processos_R.add(P);tamanho += P.getTamanho();}
                }
                System.out.println(tamanho);
                Tab_Processos.removeAll(Tab_Processos_R);
                Tab_Processos_R.clear();
                //

                //APAGA TABELA DE PAGINAS DO PROCESSO E SEUS QUADROS EM MEMORIA
               for(Tab_Pag T: Tab_Pag_Master){
                    if(T.getNome().equals(Process_Name)){Tab_Pag_Master_R.add(T);
                        for(Componente_TP C: T.getPaginas()){
                            if(C != null && C.isP()) MP[C.getQuadro()] = new Quadro();
                            /*int[] aux = new int[2];
                            aux[0] = C.getQuadro();
                            aux[1] = 1;
                            Mem_Vazia.add(aux);*/
                        }
                    }
                }
                Tab_Pag_Master.removeAll(Tab_Pag_Master_R);
                Tab_Pag_Master_R.clear();
                //
                

                //APAGA PROCESSO DA MEMORIA SECUNDARIA
                boolean b = false;
                for(int i = 0; i<TAM_MAX_PAGINAS_MS; i++){
                    if(Mem_Sec[i] != null){
                        if(Mem_Sec[i].getProcesso().equals(Process_Name)){Mem_Sec[i] = null; b = true;}
                        if(b && i+tamanho<TAM_MAX_PAGINAS_MS) Mem_Sec[i] = Mem_Sec[i+tamanho];
                        }
                    else i = TAM_MAX_PAGINAS_MS;
                }
            }
            
            
           for(Pagina P: Mem_Sec)System.out.println(P);
            /*Mem_List_Opt();
            for(int i = 0; i<Mem_Vazia.size();i++)System.out.println(Mem_Vazia.get(i)[0] + " " + Mem_Vazia.get(i)[1]);*/
            System.out.println(Tab_Processos);
            for(Quadro Q: MP)System.out.println(Q);
       
        }
    }
    
    

    
}




