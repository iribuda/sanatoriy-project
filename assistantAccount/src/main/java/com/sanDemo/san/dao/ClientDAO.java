package com.sanDemo.san.dao;

import com.sanDemo.san.models.Client;
import com.sanDemo.san.models.Reserved;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ClientDAO {

    private static JdbcTemplate jdbcTemplate;
    private static NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ClientDAO(JdbcTemplate jdbcTemplate,
                     NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public static List<Client> clientList(){
        return jdbcTemplate.query("SELECT * FROM client", new ClientMapper());
    }

    public static Client showClient(int id){
        return jdbcTemplate.query("SELECT * FROM client WHERE IDclient=?", new Object[]{id},
            new ClientMapper()).stream().findAny().orElse(null);
    }

    public static Reserved showReservation(int clientId){
        return jdbcTemplate.query("SELECT * FROM reserved WHERE ClientID=?",
                new Object[]{clientId}, new ReservedMapper())
                .stream().findAny().orElse(null);
    }

    public static Client findClientByName(String name){
        return jdbcTemplate.query("SELECT * FROM client WHERE name=?",
                new Object[]{name}, new ClientMapper()).stream().findAny().orElse(null);
    }

    public static List<Client> getClientByProcedure(int id){
        String SQL = "SELECT * FROM client WHERE IDclient IN (SELECT ClientID FROM treatment WHERE ProcedureID=:id)";
        Map parameters = new HashMap();
        parameters.put("id", id);
        return namedParameterJdbcTemplate.query(SQL,
                parameters,
                new ClientMapper());
    }

}
