package MASLab3.SecBhvs;

import MASLab3.Etc.BehaviourKiller;
import MASLab3.Etc.Link;
import MASLab3.Etc.Setting;
import MASLab3.Etc.WorkWithConfigFiles;
import MASLab3.MainBhvs.FindingTheShortest;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;

public class Waiting4Request extends Behaviour {

    private Agent agent;
    private boolean msgArrived = false;
    private List<AID> receivers;
    private int receiversCounter;
    private AID whoRequested;
    private boolean linkFound = false;
    private double weight;

    public Waiting4Request(Agent agent, DataStore dataStore) {
        setDataStore(dataStore);
        receivers = (List<AID>) dataStore.get("receivers");
        receiversCounter = receivers.size();
        this.agent = agent;
    }

    @Override
    public void action() {
        Setting s= WorkWithConfigFiles.unMarshalAny(Setting.class, agent.getLocalName()+".xml");
        List<Link> links=s.getLinks();

        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchProtocol("foundLink"));

        ACLMessage request = agent.receive(messageTemplate);
        if(request !=null){
            msgArrived = true;
            whoRequested = request.getSender();
            getDataStore().put("whoRequested", whoRequested);
            int i = 0;

            while (linkFound == false && i <= links.size()){
                if (links.get(i).getAgentName().equals(request.getContent())){
                    linkFound = true;
                    weight = links.get(i).getWeight();
                }else {
                    i = i + 1;
                }
            }
            if (linkFound == true){
                ACLMessage message = request.createReply();
                message.setProtocol("foundLink");
                message.setContent(agent.getLocalName() + "-" + weight + "");
                message.setPerformative(ACLMessage.INFORM);
                agent.send(message);
                System.out.println("Agent " + agent.getLocalName() + " got link with " + request.getContent() +
                        " by the request of " + request.getSender().getLocalName());
            }else{
                ACLMessage anotherRequest = new ACLMessage(ACLMessage.REQUEST);
                anotherRequest.setProtocol("foundLink");
                anotherRequest.setContent(request.getContent());
                for (AID rec: receivers){
                    for (int j =0; j <= links.size(); j++){
                        if (rec.getLocalName().equals(links.get(j).getAgentName()) && !rec.getLocalName().equals(
                                request.getSender().getLocalName())){
                            anotherRequest.addReceiver(rec);
                        }
                    }
                }
                agent.send(anotherRequest);
                System.out.println("Agent " + agent.getLocalName() + " is asking neighbours for the links...");
            }
        }else{
            block();
        }
    }

    @Override
    public boolean done() {
        return msgArrived;
    }

    @Override
    public int onEnd() {
        Waiting4Inform behaviourToKill = new Waiting4Inform(agent, getDataStore());
        agent.addBehaviour(behaviourToKill);
        agent.addBehaviour(new BehaviourKiller(agent, 1000, behaviourToKill));
        return super.onEnd();
    }
}
