package cn.aurorian.ers.event;

import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.init.ErsItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FishingModifier {
    private static final float RIDING_FISH_BONUS = 0.3f; // 鱼类概率增加30%

    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        Player player = event.getEntity();
        if (player.isPassenger()
                && (player.getVehicle() instanceof DentisaurusLongirostrisEntity
                        || player.getVehicle() instanceof AquicornisDinosauriformisEntity)) {
            var sotek = (cn.aurorian.ers.entity.ErsTamableVehicle<?>) player.getVehicle();
            boolean hasBait = false;
            for (int i = 5; i <= 7; i++) {
                ItemStack itemStack = sotek.getInventory().getItem(i);
                if (itemStack.is(ErsItems.BAIT_BOX.get())) {
                    hasBait = true;
                }
            }

            if (hasBait) {
                java.util.List<ItemStack> toAdd = new java.util.ArrayList<>();
                java.util.List<ItemStack> toRemove = new java.util.ArrayList<>();
                for (ItemStack stack : event.getDrops()) {
                    if (Math.random() < RIDING_FISH_BONUS) {
                        if (stack.is(ItemTags.FISHES)) {
                            toAdd.add(stack.copy());
                        } else {
                            toRemove.add(stack);
                        }
                    }
                }
                event.getDrops().removeAll(toRemove);
                event.getDrops().addAll(toAdd);
            }
        }
    }
}
