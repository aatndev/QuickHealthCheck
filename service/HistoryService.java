package service;

import database.DBConnection;
import exception.DatabaseException;
import model.HealthRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryService {


    public void displayHistory(int userId)
            throws DatabaseException {

        String sql =
                "SELECT * FROM health_records " +
                "WHERE user_id = ? " +
                "ORDER BY date_time DESC";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, userId);

            try (ResultSet result = statement.executeQuery()) {
                boolean found = false;
                System.out.println("\n========== HEALTH HISTORY ==========");

                while (result.next()) {
                    found = true;

                    HealthRecord record = new HealthRecord(
                        result.getInt("record_id"),
                        result.getInt("user_id"),
                        result.getString("category"),
                        result.getString("result"),
                        result.getInt("risk_level"),
                        result.getTimestamp("date_time").toLocalDateTime()
                    );

                    record.display();
                }


                if (!found) {

                    System.out.println(
                            "No health history found."
                    );
                }


                System.out.println(
                        "===================================="
                );
            }


        } catch (SQLException e) {

            throw new DatabaseException(
                    "Unable to retrieve health history.",
                    e
            );
        }
    }
    
    //looks at db and get latest result for each 7 results
    public int[] getLatestRiskLevels(int userId) throws DatabaseException {
        String sql =
                "SELECT category, risk_level FROM health_records " +
                "WHERE user_id = ? " +
                "ORDER BY date_time DESC";

        int[] risks = new int[7];
        boolean[] found = new boolean[7];

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    String category = result.getString("category");
                    int risk = result.getInt("risk_level");

                    int index = -1;

                    if (category.equals("BMI")) {
                        index = 0;
                    } else if (category.equals("Blood Pressure")) {
                        index = 1;
                    } else if (category.equals("Smoking")) {
                        index = 2;
                    } else if (category.equals("Alcohol")) {
                        index = 3;
                    } else if (category.startsWith("Other Drugs")) {
                        index = 4;
                    } else if (category.equals("Oxygen Saturation (SpO₂)")) {
                        index = 5;
                    } else if (category.equals("Cholesterol")) {
                        index = 6;
                    }

                    if (index != -1 && !found[index]) {
                        risks[index] = risk;
                        found[index] = true;
                    }
                }
            }

            return risks;

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Unable to retrieve latest health risks.",
                    e
            );
        }
    }
}