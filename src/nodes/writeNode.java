package nodes;

import visitor.visitor;

public class writeNode extends node {

    public writeNode(String data){
        super(data);
    }

    public writeNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}