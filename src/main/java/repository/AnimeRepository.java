package repository;

import conn.ConnectionFactory;
import domain.Anime;
import domain.Producer;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Log4j2
public class AnimeRepository {
    public static List<Anime> findByName(String name){
        List<Anime> animes = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatement(con, name);
             ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                Producer producer = Producer.builder()
                        .name(rs.getString("name"))
                        .id(rs.getInt("id"))
                        .build();
                
                Anime anime = Anime.builder()
                        .name(rs.getString("name"))
                        .id(rs.getInt("id"))
                        .episodes(rs.getInt("episodes"))
                        .producer(producer)
                        .build();
                animes.add(anime);
            }
            System.out.println("\nAnime added successful\n");
        } catch (SQLException e){
            System.out.println("Error while trying to find anime");
            e.printStackTrace();
        }
        return animes;
    }

    private static PreparedStatement createPreparedStatement(Connection con, String name)
            throws SQLException {
        String sql = """
                    SELECT a.id, a.episodes, a.name, a.producer_id, p.name as 'producer_name' FROM animeStore.anime
                    a INNER JOIN animeStore.producer p on a.producer_id = p.id WHERE a.name LIKE ?
                    """;
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", name));
        return ps;
    }

    public static void delete(int id){
        String sql = "DELETE FROM `animeStore`.`anime` WHERE (`id` = '%d');".formatted(id);
        try (Connection con = ConnectionFactory.getConnection();
             Statement stmt = con.createStatement()){
            int rowsAffect = stmt.executeUpdate(sql);
            System.out.printf("\nDeleted anime %d in db rows affected %d\n",id ,rowsAffect);
        } catch (SQLException e) {
            System.out.printf("Error while trying to delete anime %d\n", id);
            e.printStackTrace();
        }
    }

    public static void createProducer(Anime p){
        String sql = "INSERT INTO `animeStore`.`anime` (name) VALUES ('%s');".formatted(p.getName());
        try (Connection con = ConnectionFactory.getConnection();
             Statement stmt = con.createStatement()){
            int rowsAffect = stmt.executeUpdate(sql);
            System.out.printf("\nInsert anime %s in db rows affected %d\n",p.getName(),rowsAffect);
        } catch (SQLException e){
            System.out.printf("Error while trying to insert anime %s\n", p.getName());
            e.printStackTrace();
        }
    }

    public static void update(Anime anime){
        String sql = "UPDATE `animeStore`.`anime` SET `name` = '%s ' WHERE (`id` = '%d');"
                .formatted(anime.getName(), anime.getId());
        try (Connection con = ConnectionFactory.getConnection();
             Statement stmt = con.createStatement()){
            int rowsAffect = stmt.executeUpdate(sql);
            System.out.printf("\nUpdated anime %d in db rows affected %d\n",anime.getId() ,rowsAffect);
        } catch (SQLException e){
            System.out.printf("\nError while trying to update anime %d\n", anime.getId());
            e.printStackTrace();
        }
    }

}
