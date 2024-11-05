package Repository.DbRepos;

import Domain.Prietenie;
import Domain.Tuple;
import Domain.Utilizator;
import Domain.Validators.PrietenieValidator;
import Domain.Validators.ValidationException;
import Domain.Validators.Validator;
import Repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class PrietenieDbRepo implements Repository<Tuple<Long,Long>, Prietenie> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Prietenie> validator;

    public PrietenieDbRepo(String url,String username,String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        validator = new PrietenieValidator();
    }


    @Override
    public Optional<Prietenie> findOne(Tuple<Long,Long> id)
    {
        Prietenie prietenie;
        try(Connection connection = DriverManager.getConnection(url,username,password);
            ResultSet resultSet = connection.createStatement().executeQuery(String.format("select * from prietenie P where P.User1 = '%d' and P.User2 = '%d'",id.getLeft(),id.getRight()))){
            if (resultSet.next()) {
                prietenie = createUserFromResultSet(resultSet);
                return Optional.ofNullable(prietenie);
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Prietenie createUserFromResultSet(ResultSet resultSet)
    {
        try {
            Long user1  = resultSet.getLong("User1");
            Long user2 = resultSet.getLong("User2");

            LocalDateTime date = resultSet.getDate("DataPrietenie").toLocalDate().atStartOfDay();
            Prietenie friends = new Prietenie(date);
            friends.setId(new Tuple<>(user1,user2));
            return friends;
        } catch (SQLException e) {
            return null;
        }
    }



    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> friendShips = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from prietenie");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long user1  = resultSet.getLong("User1");
                Long user2 = resultSet.getLong("User2");
                LocalDateTime date = resultSet.getDate("DataPrietenie").toLocalDate().atStartOfDay();

                Prietenie friends = new Prietenie(date);
                friends.setId(new Tuple<>(user1,user2));
                friendShips.add(friends);
            }
            return friendShips;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendShips;
    }


    @Override
    public Optional<Prietenie> save(Prietenie entity) {
        String sql = "insert into prietenie (User1,User2,DataPrietenie) values (?, ?, ?)";
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getId().getLeft());
            ps.setLong(2, entity.getId().getRight());
            ps.setDate(3,Date.valueOf(entity.getDate().toLocalDate()));

            ps.executeUpdate();
        } catch (SQLException e) {
            return Optional.ofNullable(entity);
        }
        return Optional.empty();
    }


    @Override
    public Optional<Prietenie> delete(Tuple<Long,Long> id) {
        String sql = "delete from prietenie where User1 = ? and User2 = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            Optional<Prietenie> prietenie = findOne(id);
            if(prietenie.isPresent()) {
                ps.setLong(1, id.getLeft());
                ps.setLong(2, id.getRight());
                ps.executeUpdate();
            }
            return prietenie;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Prietenie> update(Prietenie prietenie) {
        if(prietenie == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(prietenie);
        String sql = "update prietenie set DataPrietenie = ? where User1 = ? and User2 = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, prietenie.getId().getLeft());
            ps.setLong(2, prietenie.getId().getRight());
            ps.setDate(3,Date.valueOf("2010-10-10"));

            if( ps.executeUpdate() > 0 )
                return Optional.empty();
            return Optional.ofNullable(prietenie);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int findLength()
    {
        final int[] ret = {0};
        findAll().forEach(x-> ret[0] += 1);
        return ret[0];
    }

}
