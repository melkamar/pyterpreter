package cz.melkamar.pyterpreter.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class PyAssignNode extends PyExpressionNode {
    protected abstract FrameSlot getSlot();

    @Specialization(guards = "isLongOrBlank(frame)")
    protected long writeLong(VirtualFrame frame, long value) {
        getSlot().setKind(FrameSlotKind.Long);
        frame.setLong(getSlot(), value);
        return value;
    }

    protected boolean isLongOrBlank(VirtualFrame frame) { // Parameter must be here, even though it is not used
        return getSlot().getKind() == FrameSlotKind.Long || getSlot().getKind() == FrameSlotKind.Illegal;
    }

    @Override
    public void print(int indent) {
        printIndented(":= "+getSlot().toString(), indent);
    }
}
