package nodes;

import visitor.visitor;

public class numNode extends node {

    public numNode(String data){
        super(data);
    }

    public numNode(String data, node parent){
        super(data, parent);
    }

    public numNode(String data, String type){
        super(data, type);
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
