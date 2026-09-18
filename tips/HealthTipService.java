package tips;

public class HealthTipService implements HealthTips {


    @Override
    public String getTip(
            String category,
            int riskLevel
    ) {

        category = category.toLowerCase();


        if (category.contains("bmi")) {

            if (riskLevel <= 1) {
                return "Maintain a balanced diet and regular physical activity.";
            }

            if (riskLevel <= 3) {
                return "Focus on balanced nutrition, regular exercise and maintaining a healthy weight.";
            }

            return "Consider seeking professional medical guidance regarding weight management.";
        }


        if (category.contains("blood pressure")) {

            if (riskLevel <= 1) {
                return "Continue regular physical activity and maintain a balanced diet.";
            }

            if (riskLevel <= 3) {
                return "Reduce excess salt, stay physically active and monitor your blood pressure regularly.";
            }

            return "Very high blood pressure requires prompt medical attention.";
        }


        if (category.contains("smoking")) {

            if (riskLevel <= 1) {
                return "Avoid starting or increasing tobacco use.";
            }

            return "Reducing and eventually stopping smoking can significantly improve health.";
        }


        if (category.contains("alcohol")) {

            if (riskLevel <= 1) {
                return "Continue making responsible choices regarding alcohol consumption.";
            }

            return "Consider reducing alcohol consumption and seeking support if needed.";
        }


        if (category.contains("drug")) {

            if (riskLevel <= 1) {
                return "Avoid unnecessary or harmful substance use.";
            }

            return "Consider seeking professional support to reduce harmful substance use.";
        }


        if (category.contains("oxygen")) {

            if (riskLevel <= 1) {
                return "Maintain healthy habits and monitor oxygen levels when appropriate.";
            }

            return "An unusually low oxygen level should be medically evaluated.";
        }


        if (category.contains("cholesterol")) {

            if (riskLevel <= 1) {
                return "Maintain a balanced diet and regular physical activity.";
            }

            return "Focus on heart-healthy food choices, exercise and regular monitoring.";
        }


        return getGeneralTip(riskLevel);
    }


    @Override
    public String getGeneralTip(int riskLevel) {

        switch (riskLevel) {

            case 0:
                return "Your current risk level is low. Continue maintaining healthy habits.";

            case 1:
                return "Your risk level is slightly elevated. Continue monitoring your health.";

            case 2:
                return "Your risk level is moderate. Consider improving lifestyle habits and monitoring your results.";

            case 3:
                return "Your risk level is high. Consider consulting a healthcare professional.";

            case 4:
                return "Your risk level is very high. Seek appropriate medical attention.";

            default:
                return "No tip available.";
        }
    }
}