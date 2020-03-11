package nodes;

import visitor.visitor;

public class AddOpNode extends node {

    public AddOpNode(String data){
        super(data);
    }

    public AddOpNode(String data, node parent){
        super(data, parent);
    }

    @Override
    public void accept(visitor visitorNode) {
        super.accept(visitorNode);
    }
}
