package nodes;

import visitor.visitor;

public class multOpNode extends node {

    public multOpNode(String data){
        super(data);
    }

    public multOpNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void accept(visitor visitorNode) {
        visitorNode.visit(this);
    }
}
