package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class PyAssignNode extends PyStatementNode {
    protected abstract FrameSlot getSlot();
    protected abstract PyStatementNode getValueNode();

    @Specialization(guards = "isLongOrBlank(frame)")
    protected Object writeLong(VirtualFrame frame, long value) {
        getSlot().setKind(FrameSlotKind.Long);
        frame.setLong(getSlot(), value);
        return null; // Declaring function return type void causes annotation processor error
    }

    @Specialization(guards = "isBoolOrBlank(frame)")
    protected Object writeBoolean(VirtualFrame frame, boolean value) {
        getSlot().setKind(FrameSlotKind.Boolean);
        frame.setBoolean(getSlot(), value);
        return null; // Declaring function return type void causes annotation processor error
    }

    @Specialization(replaces = {"writeLong", "writeBoolean"})
    protected Object writeGeneric(VirtualFrame frame, Object value) {
        getSlot().setKind(FrameSlotKind.Object);
        frame.setObject(getSlot(), value);
        return null; // Declaring function return type void causes annotation processor error
    }

    protected boolean isLongOrBlank(VirtualFrame frame) { // Parameter must be here, even though it is not used
        return getSlot().getKind() == FrameSlotKind.Long || getSlot().getKind() == FrameSlotKind.Illegal;
    }

    protected boolean isBoolOrBlank(VirtualFrame frame) { // Parameter must be here, even though it is not used
        return getSlot().getKind() == FrameSlotKind.Boolean || getSlot().getKind() == FrameSlotKind.Illegal;
    }

    @Override
    public void print(int indent) {
        printIndented(":= "+getSlot().toString(), indent);
        getValueNode().print(indent+1);
    }
}
