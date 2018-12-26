package MASLab3.MainBhvs;

import MASLab3.Etc.Link;
import MASLab3.Etc.Setting;
import MASLab3.Etc.WorkWithConfigFiles;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;

public class FindingTheShortest extends Behaviour {

    private Agent agent;

    public FindingTheShortest(Agent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {
        Setting setting = WorkWithConfigFiles.unMarshalAny(Setting.class, agent.getName() + ".xml");
        List<Link> links = setting.getLinks();
        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchProtocol("foundLink"),
                MessageTemplate.MatchPerformative(ACLMessage.INFORM));

    }

    @Override
    public boolean done() {
        return false;
    }
}
