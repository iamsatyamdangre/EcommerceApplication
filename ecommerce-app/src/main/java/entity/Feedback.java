package entity;

public class Feedback {
    private int feedbackId;
    private int customerId;
    private String feedbackMessage;

    public Feedback() {}

    public Feedback(int feedbackId, int customerId, String feedbackMessage) {
        this.feedbackId = feedbackId;
        this.customerId = customerId;
        this.feedbackMessage = feedbackMessage;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    public void setFeedbackMessage(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }
}
