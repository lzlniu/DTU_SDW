package objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
//@author s213578 - Johannes Pedersen
@XmlRootElement // Needed for XML serialization and deserialization
@Data // Automatic getter and setters and equals etc
@NoArgsConstructor // Needed for JSON deserialization and XML serialization and deserialization
@AllArgsConstructor
public class DtuPayUser {
    private String firstName;
    private String lastName;
    private String dtuPayID;
    private String bankID;
    private String CPR;
}
