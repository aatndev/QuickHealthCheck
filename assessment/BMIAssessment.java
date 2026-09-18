package assessment;

import model.RiskResult;
import exception.InvalidHealthDataException;

public class BMIAssessment extends HealthAssessment {

    private double height;
    private double weight;


    public BMIAssessment(double height, double weight)
            throws InvalidHealthDataException {

        if (height <= 0) {
            throw new InvalidHealthDataException(
                    "Height must be greater than zero."
            );
        }

        if (weight <= 0) {
            throw new InvalidHealthDataException(
                    "Weight must be greater than zero."
            );
        }

        this.height = height;
        this.weight = weight;
    }


    @Override
    public RiskResult assess() {

        double bmi = weight / (height * height);

        int riskLevel;
        String category;
        String comment;


        if (bmi < 18.5) {

            category = "Underweight";
            riskLevel = 2;
            comment = "BMI is below the normal range.";

        } else if (bmi < 25) {

            category = "Normal";
            riskLevel = 0;
            comment = "BMI is within the normal range.";

        } else if (bmi < 30) {

            category = "Overweight";
            riskLevel = 2;
            comment = "BMI is above the normal range.";

        } else {

            category = "Obese";
            riskLevel = 4;
            comment = "BMI is considerably above the normal range.";
        }


        return new RiskResult(
            bmi,
            category,
            riskLevel,
            comment
        );
    }


    @Override
    public String getAssessmentName() {
        return "BMI Assessment";
    }
}