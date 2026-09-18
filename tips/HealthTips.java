package tips;

public interface HealthTips {

    String getTip(String category, int riskLevel);

    String getGeneralTip(int riskLevel);
}