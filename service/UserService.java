package service;

import database.DBConnection;
import exception.DatabaseException;
import model.User;

import java.sql.*;

public class UserService {


    public User registerUser(User user)
            throws DatabaseException {

        String sql =
                "INSERT INTO users " +
                "(name, email, phone, password, dob_or_age, height, weight) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";


        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getDobOrAge());
            statement.setDouble(6, user.getHeight());
            statement.setDouble(7, user.getWeight());


            statement.executeUpdate();


            try (ResultSet result =
                         statement.getGeneratedKeys()) {

                if (result.next()) {

                    user.setUserId(
                            result.getInt(1)
                    );
                }
            }


            return user;

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Unable to register user.",
                    e
            );
        }
    }


    public User getUserById(int userId)
            throws DatabaseException {

        String sql =
                "SELECT * FROM users WHERE user_id = ?";


        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);


            try (ResultSet result =
                         statement.executeQuery()) {

                if (result.next()) {

                    return new User(
                            result.getInt("user_id"),
                            result.getString("name"),
                            result.getString("email"),
                            result.getString("phone"),
                            result.getString("password"),
                            result.getString("dob_or_age"),
                            result.getDouble("height"),
                            result.getDouble("weight")
                    );
                }
            }


        } catch (SQLException e) {

            throw new DatabaseException(
                    "Unable to retrieve user.",
                    e
            );
        }


        return null;
    }
    

    // UPDATE USER DETAILS
    // ========================================

    public void updateUser(User user)
            throws DatabaseException {

        String sql =
                "UPDATE users SET " +
                "name = ?, " +
                "email = ?, " +
                "phone = ?, " +
                "password = ?, " +
                "dob_or_age = ?, " +
                "height = ?, " +
                "weight = ? " +
                "WHERE user_id = ?";


        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getDobOrAge());
            statement.setDouble(6, user.getHeight());
            statement.setDouble(7, user.getWeight());
            statement.setInt(8, user.getUserId());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Unable to update user.",
                    e
            );
        }
    }

    // ========================================
    // GET ALL USERS
    // ========================================

    public java.util.List<User> getAllUsers() throws DatabaseException {
        String sql = "SELECT * FROM users";

        java.util.List<User> users = new java.util.ArrayList<>();

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery()
        ) {
            while (result.next()) {
                users.add(new User(
                    result.getInt("user_id"),
                    result.getString("name"),
                    result.getString("email"),
                    result.getString("phone"),
                    result.getString("password"),
                    result.getString("dob_or_age"),
                    result.getDouble("height"),
                    result.getDouble("weight")
                ));
            }

            return users;

        } catch (SQLException e) {
            throw new DatabaseException("Unable to retrieve users.", e);
        }
    }


    // ========================================
    // GET USER BY ID AND PASSWORD
    // ========================================

    public User getUserByIdAndPassword(int userId, String password) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE user_id = ? AND password = ?";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, userId);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new User(
                        result.getInt("user_id"),
                        result.getString("name"),
                        result.getString("email"),
                        result.getString("phone"),
                        result.getString("password"),
                        result.getString("dob_or_age"),
                        result.getDouble("height"),
                        result.getDouble("weight")
                    );
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Unable to verify user.", e);
        }

        return null;
    }
}