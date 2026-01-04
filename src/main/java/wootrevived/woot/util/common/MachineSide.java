package wootrevived.woot.util.common;

import com.google.common.base.CaseFormat;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

import static wootrevived.woot.util.render.WootStyles.DIRECTION_STYLE;

public enum MachineSide implements StringRepresentable {
    FRONT,
    BACK,
    TOP,
    BOTTOM,
    RIGHT,
    LEFT;

    public static MachineSide getMachineSide(Direction facing, Direction side){
        if(side == BACK.getRealDirection(facing)) return BACK;
        if(side == TOP.getRealDirection(facing)) return TOP;
        if(side == BOTTOM.getRealDirection(facing)) return BOTTOM;
        if(side == LEFT.getRealDirection(facing)) return LEFT;
        if(side == RIGHT.getRealDirection(facing)) return RIGHT;
        return FRONT;
    }

    public Direction getRealDirection(Direction facing){
        if(this == BACK) return facing.getOpposite();
        if(this == TOP) return Direction.UP;
        if(this == BOTTOM) return Direction.DOWN;
        if(this == LEFT) return facing.getClockWise();
        if(this == RIGHT) return facing.getCounterClockWise();
        return facing;
    }

    public MutableComponent getDirectionComponent(Direction facing){
        Direction realDirection = getRealDirection(facing);
        return Component.literal("[" + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, realDirection.getSerializedName()) + "]").setStyle(DIRECTION_STYLE);
    }

    public MutableComponent getComponent(){
        return Component.literal(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.toString()));
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
