package nodes;

import lexer.token;
import visitor.visitor;

public class generalNode extends node {

    public generalNode(String data){
        super(data);
    }

//    public generalNode(String name, int num){
//        super(name, num);
//    }

//    public generalNode(token t, int num){
//        super(t, num);
//    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
