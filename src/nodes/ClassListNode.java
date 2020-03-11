package nodes;

import lexer.token;
import visitor.visitor;

public class ClassListNode extends node {

    public ClassListNode(String data){
        super(data);
    }

    public ClassListNode(String data, node parent){
        super(data, parent);
    }

    public ClassListNode(String name, int num){
        super(name, num);
    }

    public ClassListNode(token t, int num){
        super(t, num);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}

