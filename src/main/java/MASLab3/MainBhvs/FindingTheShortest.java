package MASLab3.MainBhvs;

import MASLab3.Etc.Link;
import MASLab3.Etc.Setting;
import MASLab3.Etc.WorkWithConfigFiles;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;

public class FindingTheShortest extends WakerBehaviour {

    private Agent agent;
    private List<AID> receivers;
    private boolean endFlag = false;
    private List<String> ways;


    public FindingTheShortest(Agent agent, long timeout, DataStore dataStore) {
        super(agent, timeout);
        this.agent = agent;
        setDataStore(dataStore);
        receivers = (List<AID>) dataStore.get("receivers");
    }

    @Override
    protected void onWake() {
        super.onWake();
        Setting setting = WorkWithConfigFiles.unMarshalAny(Setting.class, agent.getName() + ".xml");
        List<Link> links = setting.getLinks();
        ways = new ArrayList<String>();
        ArrayList<Double> pathsWeights = new ArrayList<Double>();

        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
               MessageTemplate.MatchProtocol("InfBack"));

        while (endFlag == false){
            ACLMessage message = agent.receive(messageTemplate);
            if (message != null){
                ways.add(message.getContent())
            }else endFlag = true;
        }
        for (int i = 0; i <= ways.size(); i++){

        }

    }
}
