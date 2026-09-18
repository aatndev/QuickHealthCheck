package assessment;

import model.RiskResult;

public abstract class HealthAssessment {

    // Common method for all health assessments
    public abstract RiskResult assess();

    // Common method for displaying the assessment name
    public abstract String getAssessmentName();
}