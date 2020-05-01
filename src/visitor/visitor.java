package visitor;

import nodes.*;

public abstract class visitor {
    public abstract void visit(addOpNode p_node);
    public abstract void visit(assignStatNode p_node);
    public abstract void visit(classListNode p_node);
    public abstract void visit(classNode p_node);
    public abstract void visit(funcDeclNode p_node);
    public abstract void visit(funcDefListNode p_node);
    public abstract void visit(funcDefNode p_node);
    public abstract void visit(generalNode p_node);
    public abstract void visit(idNode p_node);
    public abstract void visit(multOpNode p_node);
    public abstract void visit(node p_node);
    public abstract void visit(numNode p_node);
    public abstract void visit(progNode p_node);
    public abstract void visit(statBlockNode p_node);
    public abstract void visit(typeNode p_node);
    public abstract void visit(varDeclNode p_node);
    public abstract void visit(mainNode p_node);
    public abstract void visit(fparamNode p_node);
}
