package cz.melkamar.pyterpreter.nodes.variables;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.*;
import cz.melkamar.pyterpreter.nodes.PyNode;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class PyReadVarNode extends PyNode {
    protected abstract FrameSlot getSlot(); // Generated by truffle

    @Specialization(guards = "isLong(frame)")
    protected long readLong(VirtualFrame frame) {
        return FrameUtil.getLongSafe(frame, getSlot());
    }

    protected boolean isLong(VirtualFrame frame) {
        return getSlot().getKind() == FrameSlotKind.Long;
    }

    @Specialization(guards = "isDouble(frame)")
    protected double readDouble(VirtualFrame frame) {
        return FrameUtil.getDoubleSafe(frame, getSlot());
    }

    protected boolean isDouble(VirtualFrame frame) {
        return getSlot().getKind() == FrameSlotKind.Double;
    }

    @Specialization(guards = "isBoolean(frame)")
    protected boolean readBoolean(VirtualFrame frame) {
        return FrameUtil.getBooleanSafe(frame, getSlot());
    }

    protected boolean isBoolean(VirtualFrame frame) {
        return getSlot().getKind() == FrameSlotKind.Boolean;
    }

    @Specialization(replaces = {"readLong, readDouble, readBoolean"})
    protected Object readObject(VirtualFrame frame) {
        if (!frame.isObject(getSlot())) {
            CompilerDirectives.transferToInterpreter();
            Object result = frame.getValue(getSlot());
            frame.setObject(getSlot(), result);
            return result;
        }

        return FrameUtil.getObjectSafe(frame, getSlot());
    }
}
