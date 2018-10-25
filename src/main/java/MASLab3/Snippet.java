package MASLab3;

import java.util.ArrayList;
import java.util.List;
// Добавить ограничение не больше 6 агентов для связи, чтобы не закольцевалось
// Поменять Run Configuration на стандартные jade.Boot и т.д

public class Snippet {
    public static void main(String[] args) {
        List<Link> links = new ArrayList<Link>();
        links.add(new Link("Agent2", 10));
        links.add(new Link("Agent3", 25));
        Setting s = new Setting();
        s.setLinks(links);
//      создаем xml
//      WorkWithConfigFiles.marshalAny(Setting.class, s, "Agent1.xml");

        Setting s2 = WorkWithConfigFiles.unMarshalAny(Setting.class, "Agent1.xml");


    }

}
