package service;

import assessment.*;
import exception.DatabaseException;
import exception.InvalidHealthDataException;
import model.HealthCheckup;
import model.RiskResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckupService {


    // ----------------------------------------
    // BMI
    // ----------------------------------------

    public RiskResult checkBMI(
            double height,
            double weight
    ) throws InvalidHealthDataException {

        BMIAssessment assessment =
                new BMIAssessment(height, weight);

        return assessment.assess();
    }


    // ----------------------------------------
    // Blood Pressure
    // ----------------------------------------

    public RiskResult checkBloodPressure(
            int systolic,
            int diastolic
    ) throws InvalidHealthDataException {

        BPAssessment assessment =
                new BPAssessment(
                        systolic,
                        diastolic
                );

        return assessment.assess();
    }


    // ----------------------------------------
    // Substance Use
    // ----------------------------------------

    public SubstanceAssessment checkSubstances(
            int smokingIntensity,
            int alcoholIntensity,
            int otherDrugIntensity
    ) throws InvalidHealthDataException {

        return new SubstanceAssessment(
                smokingIntensity,
                alcoholIntensity,
                otherDrugIntensity
        );
    }


    // ----------------------------------------
    // Oxygen Saturation
    // ----------------------------------------

    public RiskResult checkOxygen(
            double oxygen
    ) throws InvalidHealthDataException {

        if (oxygen <= 0 || oxygen > 100) {

            throw new InvalidHealthDataException(
                    "Oxygen saturation must be between 0 and 100."
            );
        }


        int riskLevel;
        String category;
        String comment;


        if (oxygen >= 95) {

            riskLevel = 0;
            category = "Normal";
            comment =
                    "Oxygen saturation is within the normal range.";

        } else if (oxygen >= 92) {

            riskLevel = 2;
            category = "Below Normal";
            comment =
                    "Oxygen saturation is below the usual range.";

        } else {

            riskLevel = 4;
            category = "Low";
            comment =
                    "Oxygen saturation is significantly low.";
        }


        return new RiskResult(
                oxygen,
                category,
                riskLevel,
                comment
        );
    }


    // ----------------------------------------
    // Cholesterol
    // ----------------------------------------

    public RiskResult checkCholesterol(
            double cholesterol
    ) throws InvalidHealthDataException {

        if (cholesterol <= 0) {

            throw new InvalidHealthDataException(
                    "Cholesterol must be greater than zero."
            );
        }


        int riskLevel;
        String category;
        String comment;


        if (cholesterol < 200) {

            riskLevel = 0;
            category = "Desirable";
            comment =
                    "Total cholesterol is within the desirable range.";

        } else if (cholesterol < 240) {

            riskLevel = 2;
            category = "Borderline High";
            comment =
                    "Total cholesterol is above the desirable range.";

        } else {

            riskLevel = 4;
            category = "High";
            comment =
                    "Total cholesterol is high.";
        }


        return new RiskResult(
                cholesterol,
                category,
                riskLevel,
                comment
        );
    }


    // ----------------------------------------
    // Save basic checkup information
    // ----------------------------------------

    public int createCheckup(int userId)
            throws DatabaseException {

        String sql =
                "INSERT INTO health_checkups " +
                "(user_id) VALUES (?)";


        try (
                Connection connection =
                        database.DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                java.sql.Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setInt(1, userId);

            statement.executeUpdate();


            try (ResultSet result =
                         statement.getGeneratedKeys()) {

                if (result.next()) {
                    return result.getInt(1);
                }
            }


        } catch (SQLException e) {

            throw new DatabaseException(
                    "Unable to create health checkup.",
                    e
            );
        }
        return -1;
    }

    // ----------------------------------------
    // Save Health Record
    // ----------------------------------------

    public void saveHealthRecord(
        int userId,
        String category,
        double result,
        int riskLevel
    ) throws DatabaseException {
        String sql =
            "INSERT INTO health_records " +
            "(user_id, category, result, risk_level) " +
            "VALUES (?, ?, ?, ?)";
        try (
            Connection connection = database.DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, userId);
            statement.setString(2, category);
            statement.setDouble(3, result);
            statement.setInt(4, riskLevel);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                "Unable to save health record.",
                e
            );
        }
    }
}