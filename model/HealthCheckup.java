package model;

import java.time.LocalDateTime;

public class HealthCheckup {

    private int checkupId;
    private int userId;
    private LocalDateTime checkupDate;

    // BMI
    private Double height;
    private Double weight;
    private Double bmi;
    private Integer bmiRisk;

    // Blood Pressure
    private Integer systolic;
    private Integer diastolic;
    private Integer bpRisk;

    // Substance Use
    private Integer smokingIntensity;
    private Integer alcoholIntensity;
    private Integer otherDrugIntensity;

    private Integer smokingRisk;
    private Integer alcoholRisk;
    private Integer otherDrugRisk;
    private Integer substanceOverallRisk;

    // Other measurements
    private Double oxygen;
    private Integer oxygenRisk;

    private Double cholesterol;
    private Integer cholesterolRisk;


    public HealthCheckup(int userId) {

        this.userId = userId;
        this.checkupDate = LocalDateTime.now();
    }


    public int getCheckupId() {
        return checkupId;
    }

    public void setCheckupId(int checkupId) {
        this.checkupId = checkupId;
    }


    public int getUserId() {
        return userId;
    }


    public LocalDateTime getCheckupDate() {
        return checkupDate;
    }


    // BMI

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getBmi() {
        return bmi;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }

    public Integer getBmiRisk() {
        return bmiRisk;
    }

    public void setBmiRisk(Integer bmiRisk) {
        this.bmiRisk = bmiRisk;
    }


    // Blood Pressure

    public Integer getSystolic() {
        return systolic;
    }

    public void setSystolic(Integer systolic) {
        this.systolic = systolic;
    }

    public Integer getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(Integer diastolic) {
        this.diastolic = diastolic;
    }

    public Integer getBpRisk() {
        return bpRisk;
    }

    public void setBpRisk(Integer bpRisk) {
        this.bpRisk = bpRisk;
    }


    // Substance Use

    public Integer getSmokingIntensity() {
        return smokingIntensity;
    }

    public void setSmokingIntensity(Integer smokingIntensity) {
        this.smokingIntensity = smokingIntensity;
    }

    public Integer getAlcoholIntensity() {
        return alcoholIntensity;
    }

    public void setAlcoholIntensity(Integer alcoholIntensity) {
        this.alcoholIntensity = alcoholIntensity;
    }

    public Integer getOtherDrugIntensity() {
        return otherDrugIntensity;
    }

    public void setOtherDrugIntensity(Integer otherDrugIntensity) {
        this.otherDrugIntensity = otherDrugIntensity;
    }


    public Integer getSmokingRisk() {
        return smokingRisk;
    }

    public void setSmokingRisk(Integer smokingRisk) {
        this.smokingRisk = smokingRisk;
    }

    public Integer getAlcoholRisk() {
        return alcoholRisk;
    }

    public void setAlcoholRisk(Integer alcoholRisk) {
        this.alcoholRisk = alcoholRisk;
    }

    public Integer getOtherDrugRisk() {
        return otherDrugRisk;
    }

    public void setOtherDrugRisk(Integer otherDrugRisk) {
        this.otherDrugRisk = otherDrugRisk;
    }

    public Integer getSubstanceOverallRisk() {
        return substanceOverallRisk;
    }

    public void setSubstanceOverallRisk(Integer substanceOverallRisk) {
        this.substanceOverallRisk = substanceOverallRisk;
    }


    // Oxygen

    public Double getOxygen() {
        return oxygen;
    }

    public void setOxygen(Double oxygen) {
        this.oxygen = oxygen;
    }

    public Integer getOxygenRisk() {
        return oxygenRisk;
    }

    public void setOxygenRisk(Integer oxygenRisk) {
        this.oxygenRisk = oxygenRisk;
    }


    // Cholesterol

    public Double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public Integer getCholesterolRisk() {
        return cholesterolRisk;
    }

    public void setCholesterolRisk(Integer cholesterolRisk) {
        this.cholesterolRisk = cholesterolRisk;
    }
}