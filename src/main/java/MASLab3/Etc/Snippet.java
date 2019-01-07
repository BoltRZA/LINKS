package MASLab3.Etc;

import MASLab3.Etc.Link;

import java.util.ArrayList;
import java.util.List;
// Добавить ограничение не больше 6 агентов для связи, чтобы не закольцевалось
// Поменять Run Configuration на стандартные jade.Boot и т.д

public class Snippet {
    public static void main(String[] args) {
        List<Link> links = new ArrayList<Link>();
       links.add(new Link("Agent7", 10));
       links.add(new Link("Agent8", 20));
//       links.add(new Link("Agent6", 15));
//       links.add(new Link("Agent10", 20));

        Setting s = new Setting();
        s.setLinks(links);
////      создаем xml
      WorkWithConfigFiles.marshalAny(Setting.class, s, "Agent10.xml");
//
//    Setting s2 = WorkWithConfigFiles.unMarshalAny(Setting.class, "Agent1.xml");


}

}
