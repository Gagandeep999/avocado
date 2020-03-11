package nodes;

import visitor.visitor;

public class NumNode extends node {

    public NumNode(String data){
        super(data);
    }

    public NumNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
