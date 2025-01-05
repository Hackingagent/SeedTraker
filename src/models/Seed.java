package models;

import java.sql.Date;

public class Seed {
    private int seedID;
    private String seedType;
    private Date registrationDate;
    private Date expiringDate;
    private int quantityAvailable;
    private User addedBy;

    // Constructor
    public Seed(int seedID, String seedType, Date registrationDate, Date expiringDate, int quantityAvailable, User addedBy) {
        this.seedID = seedID;
        this.seedType = seedType;
        this.registrationDate = registrationDate;
        this.expiringDate = expiringDate;
        this.quantityAvailable = quantityAvailable;
        this.addedBy = addedBy;
    }

    // Getters and setters
    public int getSeedID() {
        return seedID;
    }

    public void setSeedID(int seedID) {
        this.seedID = seedID;
    }

    public String getSeedType() {
        return seedType;
    }

    public void setSeedType(String seedType) {
        this.seedType = seedType;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getExpiringDate() {
        return expiringDate;
    }

    public void setExpiringDate(Date expiringDate) {
        this.expiringDate = expiringDate;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }
}