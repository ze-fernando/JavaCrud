package service;

import domain.Anime;
import lombok.extern.log4j.Log4j2;
import repository.AnimeRepository;

import java.util.Scanner;

import static repository.AnimeRepository.delete;

@Log4j2
public class AnimeService {
    private static final Scanner SCANNER = new Scanner(System.in);
    public static void buildMenu(int op){
        switch (op) {
            case 1 -> findByName();
            case 2 -> delet();
            case 3 -> save();
            case 4 -> update();
            default -> throw new IllegalArgumentException("Not a valid option");
        }
    }

    private static void findByName(){
        System.out.print("Type the name or empty to all: ");
        String name = SCANNER.nextLine();
        AnimeRepository.findByName(name)
                .forEach(a ->System.out.printf("ID: %d \nName: %s \n ------------------------\n",
                a.getId(),a.getName()));
    }
    private static void delet(){
        System.out.print("Type the id: ");
        int id = SCANNER.nextInt();
        if (id <= 0 ) return;
        delete(id);
    }

    private static void save(){
        System.out.print("Type the name of anime: ");
        String name = SCANNER.nextLine();
        Anime anime = Anime.builder().name(name).build();
        AnimeRepository.createProducer(anime);
    }

    private static void update(){
        System.out.print("Type the id of anime: ");
        int id = SCANNER.nextInt();
        System.out.print("Type the new name of anime: ");
        SCANNER.nextLine();
        String name = SCANNER.nextLine();
        Anime anime = Anime.builder().name(name).id(id).build();
        AnimeRepository.update(anime);
    }
}
