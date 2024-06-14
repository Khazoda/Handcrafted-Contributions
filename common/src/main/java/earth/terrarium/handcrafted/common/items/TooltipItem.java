package earth.terrarium.handcrafted.common.items;

import earth.terrarium.handcrafted.common.utils.TooltipUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class TooltipItem extends Item {
    private final Component tooltip;

    public TooltipItem(Component tooltip, Properties properties) {
        super(properties);
        this.tooltip = tooltip;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        TooltipUtils.addDescriptionComponent(tooltipComponents, this.tooltip);
    }
}
