package MASLab3.Etc;

import MASLab3.Etc.Link;

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
