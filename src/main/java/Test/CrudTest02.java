package Test;

import service.AnimeService;

import java.util.Scanner;


public class CrudTest02 {
    private static final Scanner SCANNER = new Scanner(System.in);
    public static void main(String[] args) {
        int op;
        while (true){
            menu();
            op = Integer.parseInt(SCANNER.nextLine());
            if (op == 0) break;
            AnimeService.buildMenu(op);
        }
   }

   private static void menu(){
       System.out.println("\nCrud by java");
       System.out.println("Type the number o your operation");
       System.out.println("[1] Search for anime");
       System.out.println("[2] Delete anime");
       System.out.println("[3] Create anime");
       System.out.println("[4] Update anime");
       System.out.println("[0] Exit");
   }
}
