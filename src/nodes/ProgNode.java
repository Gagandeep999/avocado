package nodes;

import lexer.token;
import nodes.node;
import visitor.visitor;

public class ProgNode extends node {

    public ProgNode(){
        super();
    }

    public ProgNode(String name, int num){
        super(name, num);
    }

    public ProgNode(token t, int num){
        super(t, num);
    }

    @Override
    public void accept(visitor visitorNode) {
        super.accept(visitorNode);
    }
}
