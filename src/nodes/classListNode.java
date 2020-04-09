package nodes;

import lexer.token;
import visitor.visitor;

public class classListNode extends node {

    public classListNode(String data){
        super(data);
    }

    public classListNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public String toString() {
        return super.toString();
    }

//    public classListNode(String name, int num){
//        super(name, num);
//    }

//    public classListNode(token t, int num){
//        super(t, num);
//    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}

