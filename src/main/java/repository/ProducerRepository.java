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
             PreparedStatement ps = findByNamePreparedStatement(con, name);
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
    private static PreparedStatement findByNamePreparedStatement(Connection con, String name)
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

    public static void createProducer(List<Producer> p){
        try (Connection con = ConnectionFactory.getConnection()){
            con.setAutoCommit(false);
            saveTransactionPrepareStatement(con, p);
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e){
            log.error("Error while trying to update producer '{}' ", p, e);
        }
    }
    private static void saveTransactionPrepareStatement(Connection con, List<Producer> producers)
            throws SQLException {
        String sql = "INSERT INTO `animeStore`.`producer` (`name`) VALUES ( ? );";
        boolean shoudRollback = false;
        for (Producer p:producers) {
            try (PreparedStatement ps = con.prepareStatement(sql)){
                log.info("Saving producer '{}' ", p.getName());
                ps.setString(1, p.getName());
                ps.execute();
            } catch (SQLException e){
                e.printStackTrace();
                shoudRollback = true;
            }
        }
        if (shoudRollback){
            log.warn("Transaction is going be rollback");
            con.rollback();
        }
    }

    public static void update(Producer producer){
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = PreparedStatementUpdate(con, producer)){
            int rowsAffect = ps.executeUpdate();
            log.info("Updated producer '{}' in db rows affected {}",producer.getId() ,rowsAffect);
        } catch (SQLException e){
            log.error("Error while trying to update producer '{}' ", producer.getId(), e);
        }
    }
    private static PreparedStatement PreparedStatementUpdate(Connection con, Producer producer)
            throws SQLException {
        String sql = "UPDATE `animeStore`.`producer` SET `name` = ? WHERE (`id` = ?);";

        var ps = con.prepareStatement(sql);
        ps.setString(1, producer.getName());
        ps.setInt(2, producer.getId());
        return ps;
    }

}
