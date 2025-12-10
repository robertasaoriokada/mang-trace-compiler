package ll1;

import java.util.List;

public class Production {
    public final String lhs;
    public final List<String> rhs;

    public Production(String lhs, List<String> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return lhs + " -> " + String.join(" ", rhs);
    }
}
