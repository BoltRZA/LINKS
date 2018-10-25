package MASLab3;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement (name = "Setting")

public class Setting {
    List<Link> links;

    public List<Link> getLinks(){
        return links;
    }

    public void setLinks(List<Link> links){
        this.links = links;
    }
}
