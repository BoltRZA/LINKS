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

public class SecLogic extends Behaviour {

    private Agent agent;
    private List<AID> receivers;
    private AID whoRequested;
    private boolean linkFound = false;
    private String[] reqContent;


    public SecLogic(Agent agent, DataStore dataStore) {
        setDataStore(dataStore);
        receivers = (List<AID>) dataStore.get("receivers");
        this.agent = agent;
    }

    @Override
    public void action() {

        MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        Setting s= WorkWithConfigFiles.unMarshalAny(Setting.class, agent.getLocalName()+".xml");
        List<Link> links=s.getLinks();

        ACLMessage request = agent.receive(messageTemplate);
        if(request !=null){
            whoRequested = request.getSender();
            getDataStore().put("whoRequested", whoRequested);
            int i = 0;
            reqContent = request.getContent().split(";");

            while (linkFound == false && i < reqContent.length){
                if (agent.getLocalName().equals(reqContent[i])){
                    linkFound = true;
                }else {
                    i = i + 1;
                }
            }
            if (agent.getLocalName().equals(request.getProtocol())){
                System.out.println(agent.getLocalName() + " said: Connection by the request of " +
                        reqContent[0] + " was found! Informing back!");
                ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                message.setProtocol("InfBack");
                message.setContent(request.getContent());
                message.addReceiver(new AID(reqContent[0],false));
                agent.send(message);
            }else if (linkFound == false){
                System.out.println(agent.getLocalName() + " got a request from " +
                        whoRequested.getLocalName() + " and asked neighbours for the links...");
                for (AID rec: receivers){
                    for (Link link:links){
                        if (rec.getLocalName().equals(link.getAgentName()) && !rec.equals(whoRequested.getLocalName())){
                            ACLMessage anotherRequest = new ACLMessage(ACLMessage.REQUEST);
                            anotherRequest.setProtocol(request.getProtocol());
                            anotherRequest.setContent(request.getContent()+ ";" + agent.getLocalName() + ";" +
                                    link.getWeight() + "");
                            anotherRequest.addReceiver(rec);
                            System.out.println(agent.getLocalName() + " said: I've sent a request to "
                                    + rec.getLocalName());
                            agent.send(anotherRequest);
                        }
                    }
                }
            }else {
                System.out.println(agent.getLocalName() + " said: I'm already in the circle");
            }
        }else{
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
