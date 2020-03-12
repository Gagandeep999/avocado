package nodes;

import visitor.visitor;

public class assignStatNode extends node {

    public assignStatNode(String data){
        super(data);
    }

    public assignStatNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
