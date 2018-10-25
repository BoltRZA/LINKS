package MASLab3;

public class Link {

    private String agentName;
    private double weight;

    public Link(String agentName, double weight){
        this.agentName = agentName;
        this.weight = weight;
    }

    public Link() {

    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
