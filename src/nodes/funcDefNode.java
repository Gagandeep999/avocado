package nodes;

import visitor.visitor;

public class funcDefNode extends node {

    public funcDefNode(String data){
        super(data);
    }

    public funcDefNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
