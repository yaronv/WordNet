
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.list.PointerTargetNodeList;
import net.sf.extjwnl.dictionary.Dictionary;

import java.util.*;
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

    public String convert(String text, List<String> termsToConvers) throws JWNLException {
        logger.info("calculating generalized doc");

        Set<String> set = new HashSet<>();
        set.addAll(termsToConvers);
        termsToConvers = new ArrayList<>(set);
        Collections.sort(termsToConvers, new WordsCountComparator());

        do {

            String term = termsToConvers.remove(0);

            if(term.split(" ").length == 1) {

                IndexWord word = dictionary.getIndexWord(POS.NOUN, term);
                if (word != null) {
                    String hypernym = getWordHypernym(word);
                    text = text.replaceAll(term, hypernym);
                } else {
                    word = dictionary.getIndexWord(POS.VERB, term);
                    if (word != null) {
                        String hypernym = getWordHypernym(word);
                        text = text.replaceAll(term, hypernym);
                    } else {
                        word = dictionary.getIndexWord(POS.ADJECTIVE, term);
                        if (word != null) {
                            String hypernym = getWordHypernym(word);
                            text = text.replaceAll(term, hypernym);
                        } else {
                            word = dictionary.getIndexWord(POS.ADVERB, term);
                            if (word != null) {
                                String hypernym = getWordHypernym(word);
                                text = text.replaceAll(term, hypernym);
                            }
                        }
                    }

                }
            }

        } while(termsToConvers.size() > 0);

        return text;
    }

    private String getWordHypernym(IndexWord word) {
        try {
            PointerTargetNodeList hypernyms = PointerUtils.getDirectHypernyms(word.getSenses().get(0));
            //                    System.out.println("Direct hypernyms of \"" + word.getLemma() + "\":");
            //                    hypernyms.print();
            if(hypernyms.size() > 0 && hypernyms.get(0).getSynset().getWords().size() > 0) {
                return hypernyms.get(0).getSynset().getWords().get(0).getLemma();
            }
            else {
                return word.getLemma();
            }
        } catch (JWNLException e) {
            e.printStackTrace();
            return word.getLemma();
        }
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
