package nodes;

import visitor.visitor;

public class addOpNode extends node {

    public addOpNode(String data){
        super(data);
    }

    public addOpNode(String data, node parent){
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
