package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HealthRecord {

    private int recordId;
    private int userId;
    private String category;
    private String result;
    private int riskLevel;
    private LocalDateTime dateTime;


    public HealthRecord(
        int recordId,
        int userId,
        String category,
        String result,
        int riskLevel,
        LocalDateTime dateTime
    ) {
        this.recordId = recordId;
        this.userId = userId;
        this.category = category;
        this.result = result;
        this.riskLevel = riskLevel;
        this.dateTime = dateTime;
    }


    public int getRecordId() {
        return recordId;
    }

    public int getUserId() {
        return userId;
    }

    public String getCategory() {
        return category;
    }

    public String getResult() {
        return result;
    }

    public int getRiskLevel() {
        return riskLevel;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void display() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        System.out.println("Category: " + category);
        System.out.println("Date    : " + dateTime.format(formatter));
        System.out.println("Result  : " + result);
        System.out.println("Risk    : " + riskLevel + "/4");
        System.out.println("Risk %  : " + (riskLevel * 25) + "%");
        System.out.println("----------------------------------");
    }
}