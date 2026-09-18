package model;

public class RiskResult {

    private double value;
    private String category;
    private int riskLevel;
    private double riskPercentage;
    private String comment;


    public RiskResult(
            double value,
            String category,
            int riskLevel,
            String comment
    ) {

        this.value = value;
        this.category = category;
        this.riskLevel = riskLevel;
        this.riskPercentage = riskLevel * 25.0;
        this.comment = comment;
    }


    public double getValue() {
        return value;
    }

    public String getCategory() {
        return category;
    }

    public int getRiskLevel() {
        return riskLevel;
    }

    public double getRiskPercentage() {
        return riskPercentage;
    }

    public String getComment() {
        return comment;
    }


    public void display() {

        System.out.println("----------------------------------");
        System.out.println("Result     : " + value);
        System.out.println("Category   : " + category);
        System.out.println("Risk Level : " + riskLevel + "/4");
        System.out.println(
                "Risk %     : " + riskPercentage + "%"
        );
        System.out.println("Comment    : " + comment);
        System.out.println("----------------------------------");
    }
}