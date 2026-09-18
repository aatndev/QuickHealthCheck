package assessment;

import model.RiskResult;
import exception.InvalidHealthDataException;

public class BPAssessment extends HealthAssessment {

    private int systolic;
    private int diastolic;


    public BPAssessment(int systolic, int diastolic)
            throws InvalidHealthDataException {

        if (systolic <= 0 || diastolic <= 0) {

            throw new InvalidHealthDataException(
                    "Blood pressure values must be greater than zero."
            );
        }

        this.systolic = systolic;
        this.diastolic = diastolic;
    }


    @Override
    public RiskResult assess() {

        int riskLevel;
        String category;
        String comment;


        if (systolic < 90 || diastolic < 60) {

            category = "Low Blood Pressure";
            riskLevel = 2;
            comment = "Blood pressure is below the normal range.";

        } else if (systolic < 120 && diastolic < 80) {

            category = "Normal";
            riskLevel = 0;
            comment = "Blood pressure is within the normal range.";

        } else if (systolic < 130 && diastolic < 80) {

            category = "Elevated";
            riskLevel = 1;
            comment = "Systolic blood pressure is elevated.";

        } else if (systolic < 140 || diastolic < 90) {

            category = "High Blood Pressure";
            riskLevel = 2;
            comment = "Blood pressure is above the normal range.";

        } else if (systolic < 180 || diastolic < 120) {

            category = "Very High Blood Pressure";
            riskLevel = 3;
            comment = "Blood pressure is considerably high.";

        } else {

            category = "Severely High";
            riskLevel = 4;
            comment = "Blood pressure is extremely high.";
        }


        // Store average BP as the numerical result
        double averageBP = (systolic + diastolic) / 2.0;


        return new RiskResult(
                averageBP,
                category,
                riskLevel,
                comment
        );
    }


    @Override
    public String getAssessmentName() {
        return "Blood Pressure Assessment";
    }
}