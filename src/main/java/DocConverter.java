
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.dictionary.Dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by tserver on 11/24/16.
 */
public class DocConverter {
    private static DocConverter ourInstance = new DocConverter();

    public static DocConverter getInstance() {
        return ourInstance;
    }

    private Dictionary dictionary = null;

    Logger logger = Logger.getLogger(DocConverter.class.getName());

    private DocConverter() {
        try {
            dictionary = Dictionary.getDefaultResourceInstance();
        } catch (JWNLException e) {
            e.printStackTrace();
        }
    }

    public String convert(String doc) throws JWNLException {
        logger.info("calculating generalized doc");
        String[] tokens = doc.split(" ");
        List<String> newTokens = new ArrayList<>();

        for(String token : tokens) {
            try {
                IndexWord word = dictionary.getIndexWord(POS.NOUN, token);
                if(word == null) {
                    newTokens.add(token);
                }
                else {
                    PointerTargetNodeList hypernyms = PointerUtils.getDirectHypernyms(word.getSenses().get(0));
//                    System.out.println("Direct hypernyms of \"" + word.getLemma() + "\":");
//                    hypernyms.print();
                    if(hypernyms.size() > 0 && hypernyms.get(0).getSynset().getWords().size() > 0) {
                        newTokens.add(hypernyms.get(0).getSynset().getWords().get(0).getLemma());
                    }
                    else {
                        newTokens.add(token);
                    }
                }
            } catch (JWNLException e) {
                e.printStackTrace();
                newTokens.add(token);
            }
        }
        return join(newTokens, " ");
    }

    private String join(List<String> list, String conjunction) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String item : list)
        {
            if (first)
                first = false;
            else
                sb.append(conjunction);
            sb.append(item);
        }
        return sb.toString();
    }

    private Dictionary getDictionary() {
        return dictionary;
    }
}
