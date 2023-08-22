package service;

import domain.Producer;
import lombok.extern.log4j.Log4j2;
import repository.ProducerRepository;

import java.util.List;
import java.util.Scanner;

import static repository.ProducerRepository.createProducer;
import static repository.ProducerRepository.delete;

@Log4j2
public class ProducerService {
    private static final Scanner SCANNER = new Scanner(System.in);
    public static void buildMenu(int op){
        switch (op) {
            case 1 -> findByName();
            case 2 -> delet();
            case 3 -> save();
            default -> throw new IllegalArgumentException("Not a valid option");
        }
    }

    private static void findByName(){
        System.out.print("Type the name or empty to all: ");
        String name = SCANNER.nextLine();
        ProducerRepository.findByName(name)
                .forEach(p ->System.out.printf("ID: %d \nName: %s \n ------------------------\n",
                p.getId(),p.getName()));
    }
    private static void delet(){
        System.out.print("Type the id: ");
        int id = SCANNER.nextInt();
        if (id <= 0 ) return;
        delete(id);
    }

    private static void save(){
        System.out.print("Type the name of producer: ");
        String name = SCANNER.nextLine();
        Producer producer = Producer.builder().name(name).build();
        createProducer(producer);
    }
}
