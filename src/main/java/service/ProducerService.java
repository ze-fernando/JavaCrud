package service;

import domain.Producer;
import repository.ProducerRepository;

import java.util.List;
import java.util.Scanner;

public class ProducerService {
    private static Scanner sc = new Scanner(System.in);
    public static void buildMenu(int op){
        switch (op) {
            case 1:
                findByName();
                break;
            default:
                throw new IllegalArgumentException("Not a valid option");
        }
    }

    private static void findByName(){
        System.out.println("Type the name or empty to all");
        String name = sc.nextLine();
        List<Producer> producers = ProducerRepository.findByName(name);
        for (int i = 0; i < producers.size(); i++) {
            System.out.printf("[%d] - %s \n",i,producers.get(i).getName());
        }
    }
}
