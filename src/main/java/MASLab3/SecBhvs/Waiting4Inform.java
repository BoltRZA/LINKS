package MASLab3.SecBhvs;

import MASLab3.Etc.Link;
import MASLab3.Etc.Setting;
import MASLab3.Etc.WorkWithConfigFiles;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;

public class Waiting4Inform extends Behaviour {

    private Agent agent;
    private AID whoRequested;
    private boolean linkFound = false;
    private boolean msgArrived = false;
    private double weight;

    public Waiting4Inform(Agent agent, DataStore dataStore) {
        setDataStore(dataStore);
        this.agent = agent;
    }

    @Override
    public void action() {
        Setting setting = WorkWithConfigFiles.unMarshalAny(Setting.class, agent.getLocalName()+".xml");
        List<Link> links = setting.getLinks();

        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
            MessageTemplate.MatchProtocol("InfBack"));

        ACLMessage message = agent.receive(messageTemplate);
        if (message != null){
            msgArrived = true;
            whoRequested = (AID) getDataStore().get("whoRequested");
            int i = 0;
            while (linkFound == false && i <= links.size()){
                if (links.get(i).getAgentName().equals(message.getSender().getLocalName())){
                    linkFound = true;
                    weight = links.get(i).getWeight();
                }else{
                    i = i +1;
                }
            }
            ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
            inform.setProtocol("foundLink");
            inform.setContent(agent.getLocalName() + "-" + weight + "");
            inform.addReceiver(whoRequested);
            agent.send(inform);
            System.out.println("Agent " + agent.getLocalName() + " said: The information was transmitted to " +
                    whoRequested.getLocalName());
        }else{
            block();
        }
    }
    @Override
    public boolean done() {
        return msgArrived;
    }
}
