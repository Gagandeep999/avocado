package nodes;

import lexer.token;
import visitor.visitor;

public class ProgNode extends node {

    public ProgNode(String data){
        super(data);
    }

    public ProgNode(String data, node parent){
        super(data, parent);
    }

//    public ProgNode(String name, int num){
//        super(name, num);
//    }

//    public ProgNode(token t, int num){
//        super(t, num);
//    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
