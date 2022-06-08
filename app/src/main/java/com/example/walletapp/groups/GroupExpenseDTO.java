package com.example.walletapp.groups;

public class GroupExpenseDTO {
    public String title;
    public String cost;
    public String category;
    public String paidBy;
    public String splitMethod;
    public String description;
    public Long createdAt;

    public GroupExpenseDTO() {
        // empty constructor required
    }

    public GroupExpenseDTO(String title, String cost, String category, String paidBy, String splitMethod, String description) {
        this.title = title;
        this.cost = cost;
        this.category = category;
        this.paidBy = paidBy;
        this.splitMethod = splitMethod;
        this.description = description;
        createdAt = System.currentTimeMillis();
    }

    // LocalDateTime date =
    //    LocalDateTime.ofInstant(Instant.ofEpochMilli(longValue), ZoneId.systemDefault());

    @Override
    public String toString() {
        return "GroupExpenseDTO{" +
                "title='" + title + '\'' +
                ", cost='" + cost + '\'' +
                ", category='" + category + '\'' +
                ", paidBy='" + paidBy + '\'' +
                ", splitMethod='" + splitMethod + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
