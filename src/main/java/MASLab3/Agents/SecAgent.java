package MASLab3.Agents;


import MASLab3.Etc.Link;
import MASLab3.Etc.Setting;
import MASLab3.Etc.WorkWithConfigFiles;
import MASLab3.SecBhvs.SecLogic;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.ArrayList;
import java.util.List;

public class SecAgent extends Agent {
    @Override
    protected void setup() {
        super.setup();
        DataStore dataStore = new DataStore();
        Setting setting = WorkWithConfigFiles.unMarshalAny(Setting.class, this.getLocalName()+".xml");
        List<Link> links = setting.getLinks();

        DFAgentDescription secRegisterTemplate = new DFAgentDescription();
        secRegisterTemplate.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("LinkFinding");
        serviceDescription.setName(getName() + "-LF");
        secRegisterTemplate.addServices(serviceDescription);
        try {
            DFService.register(this, secRegisterTemplate);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        DFAgentDescription secSearchingTemplate = new DFAgentDescription();
        ServiceDescription linkFinding = new ServiceDescription();
        linkFinding.setType("LinkFinding");
        secSearchingTemplate.addServices(linkFinding);
        List<AID> receivers = new ArrayList<AID>();
        try {
            DFAgentDescription[] result = DFService.search(this, secSearchingTemplate);
            if (result.length == 0) {
                System.out.println("There are no agents to link!");
            } else {
                for (int i = 0; i < result.length; ++i){
                    receivers.add(result[i].getName());
                }
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        dataStore.put("receivers", receivers);
        addBehaviour(new SecLogic(this, dataStore));
    }
}
