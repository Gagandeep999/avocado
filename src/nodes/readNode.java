package nodes;

import visitor.visitor;

public class readNode extends node {

    public readNode(String data){
        super(data);
    }

    public readNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}