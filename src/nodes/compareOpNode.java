package nodes;

import visitor.visitor;

public class compareOpNode extends node {

    public compareOpNode(String data){
        super(data);
    }

    public compareOpNode(String data, node parent){
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
