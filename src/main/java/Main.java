import net.sf.extjwnl.JWNLException;

/**
 * Created by tserver on 11/27/16.
 */
public class Main {
    public static void main(String[] args) {
        try {
            String newStr = DocConverter.getInstance().convert("appointed ceo");
            System.out.println(newStr);
        } catch (JWNLException e) {
            e.printStackTrace();
        }
    }
}
