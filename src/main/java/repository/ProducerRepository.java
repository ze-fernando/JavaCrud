package repository;

import conn.ConnectionFactory;
import domain.Producer;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Log4j2
public class ProducerRepository {
    public static List<Producer> findByName(String name){
        List<Producer> producers = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatement(con, name);
             ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                Producer producer = Producer.builder()
                        .name(rs.getString("name"))
                        .id(rs.getInt("id"))
                        .build();
                producers.add(producer);
            }
            System.out.println("\nProducer added successful\n");
        } catch (SQLException e){
            System.out.println("Error while trying to find producer");
            e.printStackTrace();
        }
        return producers;
    }

    private static PreparedStatement createPreparedStatement(Connection con, String name)
            throws SQLException {
        String sql = "SELECT * FROM animeStore.producer WHERE name like ?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", name));
        return ps;
    }

    public static void delete(int id){
        String sql = "DELETE FROM `animeStore`.`producer` WHERE (`id` = '%d');".formatted(id);
        try (Connection con = ConnectionFactory.getConnection();
             Statement stmt = con.createStatement()){
            int rowsAffect = stmt.executeUpdate(sql);
            System.out.printf("\nDeleted producer %d in db rows affected %d\n",id ,rowsAffect);
        } catch (SQLException e) {
            System.out.printf("Error while trying to delete producer %d\n", id);
            e.printStackTrace();
        }
    }

    public static void createProducer(Producer p){
        String sql = "INSERT INTO `animeStore`.`producer` (name) VALUES ('%s');".formatted(p.getName());
        try (Connection con = ConnectionFactory.getConnection();
             Statement stmt = con.createStatement()){
            int rowsAffect = stmt.executeUpdate(sql);
            System.out.printf("\nInsert producer %s in db rows affected %d\n",p.getName(),rowsAffect);
        } catch (SQLException e){
            System.out.printf("Error while trying to insert producer %s\n", p.getName());
            e.printStackTrace();
        }
    }

    public static void update(Producer producer){
        String sql = "UPDATE `animeStore`.`producer` SET `name` = '%s ' WHERE (`id` = '%d');"
                .formatted(producer.getName(), producer.getId());
        try (Connection con = ConnectionFactory.getConnection();
             Statement stmt = con.createStatement()){
            int rowsAffect = stmt.executeUpdate(sql);
            System.out.printf("\nUpdated producer %d in db rows affected %d\n",producer.getId() ,rowsAffect);
        } catch (SQLException e){
            System.out.printf("\nError while trying to update producer %d\n", producer.getId());
            e.printStackTrace();
        }
    }

}
