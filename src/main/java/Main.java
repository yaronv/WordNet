import net.sf.extjwnl.JWNLException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tserver on 11/27/16.
 */
public class Main {
    public static void main(String[] args) {
        try {
            List list = new ArrayList<String>();
            list.add("appointed");
            list.add("ceo");
            String newStr = DocConverter.getInstance().convert("appointed ceo", list);
            System.out.println(newStr);
        } catch (JWNLException e) {
            e.printStackTrace();
        }
    }
}
