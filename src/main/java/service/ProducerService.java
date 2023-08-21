package service;

import domain.Producer;
import repository.ProducerRepository;

import java.util.List;
import java.util.Scanner;

import static repository.ProducerRepository.delete;

public class ProducerService {
    private static final Scanner SCANNER = new Scanner(System.in);
    public static void buildMenu(int op){
        switch (op) {
            case 1 -> findByName();
            case 2 -> delet();
            default -> throw new IllegalArgumentException("Not a valid option");
        }
    }

    private static void findByName(){
        System.out.println("Type the name or empty to all");
        String name = SCANNER.nextLine();
        List<Producer> producers = ProducerRepository.findByName(name);
        for (int i = 0; i < producers.size(); i++) {
            System.out.printf("[%d] - ID: %d \nName: %s \n ------------------------\n",
                    i, producers.get(i).getId(),producers.get(i).getName());
        }
    }
    private static void delet(){
        System.out.println("Type the id");
        int id = SCANNER.nextInt();
        delete(id);
    }
}
