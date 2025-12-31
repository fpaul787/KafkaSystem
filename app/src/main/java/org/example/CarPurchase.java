package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CarPurchase {
    private String transactionId;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String vin;
    private String make;
    private String model;
    private int year;
    private String color;
    private BigDecimal purchasePrice;
    private String dealershipId;
    private String dealershipName;
    private String paymentMethod;
    private LocalDateTime purchaseDate;
    private String salesRepId;

    // Default constructor
    public CarPurchase() {
    }

    // Constructor with all fields
    public CarPurchase(String transactionId, String customerId, String customerName, String customerEmail,
                      String vin, String make, String model, int year, String color, BigDecimal purchasePrice,
                      String dealershipId, String dealershipName, String paymentMethod, 
                      LocalDateTime purchaseDate, String salesRepId) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.purchasePrice = purchasePrice;
        this.dealershipId = dealershipId;
        this.dealershipName = dealershipName;
        this.paymentMethod = paymentMethod;
        this.purchaseDate = purchaseDate;
        this.salesRepId = salesRepId;
    }

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getDealershipId() {
        return dealershipId;
    }

    public void setDealershipId(String dealershipId) {
        this.dealershipId = dealershipId;
    }

    public String getDealershipName() {
        return dealershipName;
    }

    public void setDealershipName(String dealershipName) {
        this.dealershipName = dealershipName;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getSalesRepId() {
        return salesRepId;
    }

    public void setSalesRepId(String salesRepId) {
        this.salesRepId = salesRepId;
    }

    @Override
    public String toString() {
        return "CarPurchase{" +
                "transactionId='" + transactionId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", vin='" + vin + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", color='" + color + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", dealershipId='" + dealershipId + '\'' +
                ", dealershipName='" + dealershipName + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", salesRepId='" + salesRepId + '\'' +
                '}';
    }
}