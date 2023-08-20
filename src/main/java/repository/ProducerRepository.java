package repository;

import conn.ConnectionFactory;
import domain.Producer;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        } catch (SQLException e){
            log.error("Error while trying to find producer", e);
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
}
