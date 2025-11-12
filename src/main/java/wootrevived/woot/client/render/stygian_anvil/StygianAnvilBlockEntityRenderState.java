package wootrevived.woot.client.render.stygian_anvil;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;

public class StygianAnvilBlockEntityRenderState extends BlockEntityRenderState {
    Direction facing;

    public final ItemStackRenderState base = new ItemStackRenderState();
    public final ItemStackRenderState firstComplementary = new ItemStackRenderState();
    public final ItemStackRenderState secondComplementary = new ItemStackRenderState();
    public final ItemStackRenderState thirdComplementary = new ItemStackRenderState();
    public final ItemStackRenderState fourthComplementary = new ItemStackRenderState();

    boolean isBaseABlock;
    boolean isFirstComplementaryABlock;
    boolean isSecondComplementaryABlock;
    boolean isThirdComplementaryABlock;
    boolean isFourthComplementaryABlock;
}
