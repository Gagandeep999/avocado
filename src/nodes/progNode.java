package nodes;

import lexer.token;
import visitor.visitor;

public class progNode extends node {

    public progNode(String data){
        super(data);
    }

    public progNode(String data, node parent){
        super(data, parent);
    }

//    public progNode(String name, int num){
//        super(name, num);
//    }

//    public progNode(token t, int num){
//        super(t, num);
//    }


    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
