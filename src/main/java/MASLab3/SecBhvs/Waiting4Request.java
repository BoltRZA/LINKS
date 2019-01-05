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
    private String[] reqContent;

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

        MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

        ACLMessage request = agent.receive(messageTemplate);
        if(request !=null){
            msgArrived = true;
            whoRequested = request.getSender();
            getDataStore().put("whoRequested", whoRequested);
            int i = 0;
            reqContent = request.getContent().split(";");

            while (linkFound == false && i <= reqContent.length){
                if (myAgent.getLocalName().equals(reqContent[i])){
                    linkFound = true;
                }else {
                    i = i + 1;
                }
            }
            if (agent.getLocalName().equals(request.getProtocol())){
                System.out.println("Agent " + agent.getLocalName() + " said: Connection by the request of " +
                        reqContent[0] + " was found! Informing back!");
                ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                message.setProtocol("InfBack");
                message.setContent(request.getContent());
                message.addReceiver(new AID(reqContent[0],false));
                agent.send(message);
                System.out.println("Agent " + agent.getLocalName() + " got link with " + request.getContent() +
                        " by the request of " + request.getSender().getLocalName());
            }else if (linkFound == false){
                ACLMessage anotherRequest = new ACLMessage(ACLMessage.REQUEST);
                anotherRequest.setProtocol(request.getProtocol());
                for (AID rec: receivers){
                    for (Link link:links){
                        if (rec.getLocalName().equals(link.getAgentName()) && !rec.equals(whoRequested)){
                            anotherRequest.setContent(request.getContent()+ ";" + agent.getLocalName() + ";" +
                                    link.getWeight() + "");
                            anotherRequest.addReceiver(rec);
                        }
                    }
                }
                agent.send(anotherRequest);
                System.out.println("Agent " + agent.getLocalName() + " is asking neighbours for the links...");
            }else {
                System.out.println("Agent " + agent.getLocalName() + " said: I'm already in the circle");
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
