package nodes;

import visitor.visitor;

public class statBlockNode extends node {

    public statBlockNode(String data){
        super(data);
    }

    public statBlockNode(String data, node parent){
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
