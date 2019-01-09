package MASLab3.MainBhvs;

import MASLab3.Etc.Link;
import MASLab3.Etc.Setting;
import MASLab3.Etc.WorkWithConfigFiles;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.apache.commons.lang.math.IntRange;

import java.util.Scanner;

import java.util.List;

public class Begin extends OneShotBehaviour {

    private Agent agent;
    private List<AID> receivers;
    private boolean linkFound = false;
    private String agentTemplate;

    public Begin(Agent agent, DataStore dataStore) {
        setDataStore(dataStore);
        this.agent = agent;
    }

    @Override
    public void action() {
        receivers = (List<AID>) getDataStore().get("receivers");
        Setting setting = WorkWithConfigFiles.unMarshalAny(Setting.class, agent.getLocalName() + ".xml");
        List<Link> links = setting.getLinks();
        Scanner scanner = new Scanner(System.in);
        System.out.println(agent.getLocalName() + " said: Enter the tag of agent to be found (INTEGER): ");
        int input = scanner.nextInt();
        agentTemplate = "Agent" + input; //Mb +""
        if (new IntRange(0, 10).containsInteger(input)) {
                for (AID rec : receivers) {
                    for (int j = 0; j < links.size(); j++) {
                        if (rec.getLocalName().equals(links.get(j).getAgentName())) {
                            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
                            message.setProtocol(agentTemplate);
                            message.setContent(agent.getLocalName()+";"+links.get(j).getWeight()+"");
                            message.addReceiver(rec);
                            System.out.println(agent.getLocalName() + " said: I've sent a request to " +
                                    rec.getLocalName());
                            agent.send(message);
                        }
                    }
                }
        } else {
            throw new RuntimeException("Error! What the fuck you've entered?");
        }
    }

    @Override
    public int onEnd() {
            Result behaviour = new Result(agent, 5000, getDataStore());
            agent.addBehaviour(behaviour);
        return super.onEnd();
    }
}
