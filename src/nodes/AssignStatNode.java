package nodes;

import visitor.visitor;

public class AssignStatNode extends node {

    public AssignStatNode(String data){
        super(data);
    }

    public AssignStatNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
