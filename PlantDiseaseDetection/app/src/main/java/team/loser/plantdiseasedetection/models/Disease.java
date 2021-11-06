package team.loser.plantdiseasedetection.models;

public class Disease {
    private String response_class;
    private String response_confident;
    private String response_time;

    public String getResponse_class() {
        return response_class;
    }

    public void setResponse_class(String response_class) {
        this.response_class = response_class;
    }

    public String getResponse_confident() {
        return response_confident;
    }

    public void setResponse_confident(String response_confident) {
        this.response_confident = response_confident;
    }

    public String getResponse_time() {
        return response_time;
    }

    public void setResponse_time(String response_time) {
        this.response_time = response_time;
    }
}
