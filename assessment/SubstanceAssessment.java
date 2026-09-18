package assessment;

import model.RiskResult;
import exception.InvalidHealthDataException;

public class SubstanceAssessment extends HealthAssessment {

    private int smokingIntensity;
    private int alcoholIntensity;
    private int otherDrugIntensity;


    public SubstanceAssessment(
            int smokingIntensity,
            int alcoholIntensity,
            int otherDrugIntensity
    ) throws InvalidHealthDataException {

        validateIntensity(smokingIntensity);
        validateIntensity(alcoholIntensity);
        validateIntensity(otherDrugIntensity);

        this.smokingIntensity = smokingIntensity;
        this.alcoholIntensity = alcoholIntensity;
        this.otherDrugIntensity = otherDrugIntensity;
    }


    private void validateIntensity(int intensity)
            throws InvalidHealthDataException {

        if (intensity < 0 || intensity > 4) {

            throw new InvalidHealthDataException(
                    "Substance intensity must be between 0 and 4."
            );
        }
    }


    private int calculateRisk(int intensity) {
        return intensity;
    }


    @Override
    public RiskResult assess() {

        int smokingRisk = calculateRisk(smokingIntensity);
        int alcoholRisk = calculateRisk(alcoholIntensity);
        int otherDrugRisk = calculateRisk(otherDrugIntensity);


        int overallRisk =
                Math.max(
                        smokingRisk,
                        Math.max(
                                alcoholRisk,
                                otherDrugRisk
                        )
                );

        String category;

        if (overallRisk <= 1) {
            category = "Low Substance Risk";
        } else if (overallRisk <= 3) {
            category = "Moderate Substance Risk";
        } else {
            category = "High Substance Risk";
        }

        String comment =
                "Smoking Risk: " + smokingRisk + "/4, " +
                "Alcohol Risk: " + alcoholRisk + "/4, " +
                "Other Drug Risk: " + otherDrugRisk + "/4";


        return new RiskResult(
                overallRisk,
                category,
                overallRisk,
                comment
        );
    }


    @Override
    public String getAssessmentName() {
        return "Substance Use Assessment";
    }


    public int getSmokingRisk() {
        return calculateRisk(smokingIntensity);
    }


    public int getAlcoholRisk() {
        return calculateRisk(alcoholIntensity);
    }


    public int getOtherDrugRisk() {
        return calculateRisk(otherDrugIntensity);
    }


    public int getOverallRisk() {

        return Math.max(
                getSmokingRisk(),
                Math.max(
                        getAlcoholRisk(),
                        getOtherDrugRisk()
                )
        );
    }
}