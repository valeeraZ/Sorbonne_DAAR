import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DFATest {

    private List<RegExTree> trees;

    @Before
    public void init(){
        String[] exprs = {"(a|b)*ab"};
        trees = Arrays.stream(exprs).map(RegEx::parse).collect(Collectors.toList());
    }

    @Test
    public void NFAtoDFATest(){
       trees.stream()
            .map(NFA::fromRegExTreeToNFA)
            .map(DFA::fromNFAtoDFA).forEach(System.out::println);
    }
}
