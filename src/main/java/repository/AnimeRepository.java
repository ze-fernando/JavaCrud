package repository;

import conn.ConnectionFactory;
import domain.Anime;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Log4j2
public class AnimeRepository {
    public static List<Anime> findByName(String name){
        List<Anime> animes = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = findByNamePreparedStatement(con, name);
             ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                Anime anime = Anime.builder()
                        .name(rs.getString("name"))
                        .id(rs.getInt("id"))
                        .build();
                animes.add(anime);
            }
            System.out.println("\nanime added successful\n");
        } catch (SQLException e){
            System.out.println("Error while trying to find anime");
            e.printStackTrace();
        }
        return animes;
    }
    private static PreparedStatement findByNamePreparedStatement(Connection con, String name)
            throws SQLException {
        String sql = "SELECT * FROM animeStore.anime WHERE name like ?;";
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

    public static void createanime(Anime a){
        try (Connection con = ConnectionFactory.getConnection()){
            con.setAutoCommit(false);
            saveTransactionPrepareStatement(con, a);
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e){
            log.error("Error while trying to update anime '{}' ", a, e);
        }
    }
    private static void saveTransactionPrepareStatement(Connection con, Anime a)
            throws SQLException {
        String sql = "INSERT INTO `animeStore`.`anime` (`name`) VALUES ( ? );";
            try (PreparedStatement ps = con.prepareStatement(sql)){
                log.info("Saving anime '{}' ", a.getName());
                ps.setString(1, a.getName());
                ps.execute();
            } catch (SQLException e){
                e.printStackTrace();
            }
    }

    public static void update(Anime anime){
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = PreparedStatementUpdate(con, anime)){
            int rowsAffect = ps.executeUpdate();
            log.info("Updated anime '{}' in db rows affected {}",anime.getId() ,rowsAffect);
        } catch (SQLException e){
            log.error("Error while trying to update anime '{}' ", anime.getId(), e);
        }
    }
    private static PreparedStatement PreparedStatementUpdate(Connection con, Anime anime)
            throws SQLException {
        String sql = "UPDATE `animeStore`.`anime` SET `name` = ? WHERE (`id` = ?);";

        var ps = con.prepareStatement(sql);
        ps.setString(1, anime.getName());
        ps.setInt(2, anime.getId());
        return ps;
    }

}
