package pl.edu.wat.wcy.edp.bd.formulino.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Driver {
    private String driverId;
    private String url;
    private String givenName;
    private String familyName;
    private String dateOfBirth;
    private String nationality;
    private String permanentNumber;
    private String code;

    // Getters and setters
    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getGivenName() { return givenName; }
    public void setGivenName(String givenName) { this.givenName = givenName; }

    public String getFamilyName() { return familyName; }
    public void setFamilyName(String familyName) { this.familyName = familyName; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getPermanentNumber() { return permanentNumber; }
    public void setPermanentNumber(String permanentNumber) { this.permanentNumber = permanentNumber; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getFullName() {
        return givenName + " " + familyName;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driverId='" + driverId + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}