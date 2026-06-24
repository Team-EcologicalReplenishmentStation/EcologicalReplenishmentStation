package cn.aurorian.ers.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class ArchaeologyLootModifier extends LootModifier {

    public static final Codec<ArchaeologyLootModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst)
            .and(inst.group(
                    ItemStack.CODEC.listOf().fieldOf("additions").forGetter(m -> m.additions),
                    Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance)))
            .apply(inst, ArchaeologyLootModifier::new));

    private final List<ItemStack> additions;

    private final float chance;

    public ArchaeologyLootModifier(LootItemCondition[] conditions, List<ItemStack> additions, float chance) {
        super(conditions);
        this.additions = additions;
        this.chance = chance;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(
            ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (context.getRandom().nextFloat() < chance) {
            ItemStack chosen = additions.get(context.getRandom().nextInt(additions.size()));
            generatedLoot.clear();
            generatedLoot.add(chosen.copy());
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
