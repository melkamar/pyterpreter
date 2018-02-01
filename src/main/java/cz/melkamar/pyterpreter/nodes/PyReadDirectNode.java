package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.NodeFields;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.melkamar.pyterpreter.exceptions.NotImplementedException;

@NodeFields(value = {
        @NodeField(name = "scope", type = Frame.class),
        @NodeField(name = "directParentScope", type = Frame.class),
        @NodeField(name = "originalNode", type = PyReadVarNode.class),
})
public abstract class PyReadDirectNode extends PyReadVarNode {
    protected abstract Frame getScope();
    protected abstract Frame getDirectParentScope();
    protected abstract PyReadVarNode getOriginalNode();

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected long readLong(VirtualFrame virtualFrame) throws FrameSlotTypeException {
        if (this.getDirectParentScope() == virtualFrame) return this.getScope().getLong(this.getSlot());
        else {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            this.replace(getOriginalNode());
            return getOriginalNode().readLong(virtualFrame);
        }
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected boolean readBoolean(VirtualFrame virtualFrame) throws FrameSlotTypeException {
        if (this.getDirectParentScope() == virtualFrame) return this.getScope().getBoolean(this.getSlot());
        else {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            this.replace(getOriginalNode());
            return getOriginalNode().readBoolean(virtualFrame);
        }
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected Object readObject(VirtualFrame virtualFrame) throws FrameSlotTypeException {
        if (this.getDirectParentScope() == virtualFrame) return this.getScope().getObject(this.getSlot());
        else {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            this.replace(getOriginalNode());
            return getOriginalNode().readObject(virtualFrame);
        }
    }

    @Specialization(replaces = {"readLong", "readBoolean", "readObject"})
    public Object read(VirtualFrame virtualFrame) {
        if (this.getDirectParentScope() == virtualFrame) return this.getScope().getValue(this.getSlot());
        else {
            this.replace(getOriginalNode());
            try {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                return getOriginalNode().readObject(virtualFrame);
            } catch (FrameSlotTypeException e) {
                e.printStackTrace();
                throw new NotImplementedException();
            }
        }
    }

    @Override
    public void print(int indent) {
        printIndented("READ-DIRECT", indent);
    }
}
