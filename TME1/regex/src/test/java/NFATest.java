import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NFATest {

    private List<RegExTree> trees;

    @Before
    public void init(){
        String[] exprs = {"a|bc*", "(abc)*|(bc)", "abcd", "(abc)*d*"};
        trees = Arrays.stream(exprs).map(RegEx::parse).collect(Collectors.toList());
    }

    @Test
    public void toNFATest(){
       trees.stream()
            .map(NFA::fromRegExTreeToNFA)
            .forEach(System.out::println);
    }
}
