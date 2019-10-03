/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so;
import java.util.ArrayList;
import java.util.Scanner;



/**
 *
 * @author Joâo Pedro
 */
public class SO {
    static Scanner teclado = new Scanner(System.in);
    static final int TAM_MAX_QUADROS_MP = 1024;
    static final int TAM_MAX_QUADROS_MS = 4096;
    static public Quadro[] Quadros = new Quadro[TAM_MAX_QUADROS_MP];
    static ArrayList<Tab_Pag> Tab_Pag_Master = new ArrayList();
    static ArrayList<Processo> Tab_Processos = new ArrayList();
    static public Quadro[] Mem_Sec = new Quadro[TAM_MAX_QUADROS_MS];
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String entrada = teclado.nextLine();
        String Process_Name;
        String Command;
        String Description; 
        Process_Name = entrada.split(" ")[0];
        Command = entrada.split(" ")[1];
        if(Command.equals("P")){
            System.out.println("EXECUTANDO");
        }
        else if(Command.equals("I")){
            System.out.println("E/S");
        }
        else if(Command.equals("C")){
            System.out.println("CRIANDO");
        }
        else if(Command.equals("R")){
            System.out.println("LEITURA");
        }
        else if(Command.equals("W")){
            System.out.println("ESCRITA");
        }
        else if(Command.equals("T")){
            System.out.println("TERMINADO");
        }
    }
    
}
