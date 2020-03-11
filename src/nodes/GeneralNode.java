package nodes;

import lexer.token;
import visitor.visitor;

public class GeneralNode extends node {

    public GeneralNode(String data){
        super(data);
    }

//    public GeneralNode(String name, int num){
//        super(name, num);
//    }

//    public GeneralNode(token t, int num){
//        super(t, num);
//    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
