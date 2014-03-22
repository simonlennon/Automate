import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;

/**
 * Created by simon.lennon on 06/02/14.
 */
public class JacksonTest {

    public static void main (String[] args) throws Throwable{
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        Map<String,Object> userData = mapper.readValue(new File("user.json"), Map.class);

        for(Object o: userData.keySet()){
            System.out.println(o+" "+userData.get(o));
        }

    }

}
