import java.util.Comparator;

/**
 * Created by tserver on 11/30/16.
 */
public class WordsCountComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        if (o1.split(" ").length != o2.split(" ").length) {
            return o2.split(" ").length - o1.split(" ").length;
        }
        else {
            if(o2.length() != o1.length()) {
                return o2.length() - o1.length();
            }
            else {
                return o1.compareTo(o2);
            }
        }
    }
}