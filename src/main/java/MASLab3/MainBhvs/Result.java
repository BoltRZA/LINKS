package MASLab3.MainBhvs;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Result extends WakerBehaviour {

    private Agent agent;
    private List<AID> receivers;
    private boolean endFlag = false;
    private List<String> paths;
    private ArrayList<Double> pathsWeights;


    public Result(Agent agent, long timeout, DataStore dataStore) {
        super(agent, timeout);
        this.agent = agent;
        setDataStore(dataStore);
        receivers = (List<AID>) dataStore.get("receivers");
    }

    @Override
    protected void onWake() {
        super.onWake();
        paths = new ArrayList<String>();
        pathsWeights = new ArrayList<Double>();

        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
               MessageTemplate.MatchProtocol("InfBack"));

        while (endFlag == false){
            ACLMessage message = agent.receive(messageTemplate);
            if (message != null){
                if(StringUtils.countMatches(message.getContent(), ";") <= 11){
                    paths.add(message.getContent());
                }
            }else
                endFlag = true;
        }
        for (int i = 0; i < paths.size(); i++){
            String path = paths.get(i);
            String [] pathSplitted = path.split(";");
            int stringCounter = 1;
            double currentPathWeight = 0;
            while (stringCounter <= pathSplitted.length){
                currentPathWeight = currentPathWeight + Double.parseDouble(pathSplitted[stringCounter]);
                stringCounter = stringCounter + 2;
            }
        pathsWeights.add(currentPathWeight);
        }
        double shortestPathWeight = pathsWeights.get(0);
        int shortestPathIndex = 0;
        for (int j = 0; j < pathsWeights.size(); j++) {
            if (pathsWeights.get(j) < shortestPathWeight){
                shortestPathWeight = pathsWeights.get(j);
                shortestPathIndex = j;
            }
        }
        System.out.println(agent.getLocalName() + " said: Here we have all the paths available: ");
        System.out.println(paths);
        System.out.println(agent.getLocalName() + " said: So the paths weights are: ");
        System.out.println(pathsWeights);
        System.out.println(agent.getLocalName() + " said: And the shortest path is: "
                + paths.get(shortestPathIndex));
        System.out.println(agent.getLocalName() + " said: and its' weight is: " + shortestPathWeight);

    }
}
