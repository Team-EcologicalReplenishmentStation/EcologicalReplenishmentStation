package cn.aurorian.ers.data.loot;

import cn.aurorian.ers.init.ErsBlocks;
import cn.aurorian.ers.init.ErsItems;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

public class ErsBlockLoot extends VanillaBlockLoot {
    @Override
    protected void generate() {
        this.add(ErsBlocks.BONE_FECES.get(), block -> this.createSingleItemTable(ErsItems.FECES.get()));
        this.add(ErsBlocks.GLASSES_FECES.get(), block -> this.createSingleItemTable(ErsItems.FECES.get()));
        this.add(ErsBlocks.LARGE_FECES.get(), block -> this.createSingleItemTable(ErsItems.FECES.get()));
        this.add(ErsBlocks.SMALL_FECES.get(), block -> this.createSingleItemTable(ErsItems.FECES.get()));
        this.add(ErsBlocks.TEL_FECES.get(), block -> this.createLootTableWithRecord());
        this.add(ErsBlocks.SOUL_CUBE.get(), block -> this.createSingleItemTable(ErsItems.SOUL_CUBE.get()));
        this.add(ErsBlocks.SWAMP_DRAGON_NEST.get(),block -> LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ErsItems.SWAMP_DRAGON_EGG.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(ErsItems.SWAMP_DRAGON_EGG.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2)).when(LootItemRandomChanceCondition.randomChance(0.25f))))
                        .add(LootItem.lootTableItem(ErsItems.SWAMP_DRAGON_EGG.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3)).when(LootItemRandomChanceCondition.randomChance(0.10f))))
                )
        );
        this.add(ErsBlocks.SAEVUS_NEST.get(),block -> LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ErsItems.SAEVUS_EGG.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(ErsItems.SAEVUS_EGG.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2)).when(LootItemRandomChanceCondition.randomChance(0.15f))))
                )
        );
        this.add(ErsBlocks.ARTIFICIAL_NEST.get(), block -> this.createSingleItemTable(ErsBlocks.ARTIFICIAL_NEST.get()));
        this.add(ErsBlocks.EQUISETUM.get(), block -> this.createSingleItemTable(ErsBlocks.EQUISETUM.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ErsBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    private LootTable.Builder createLootTableWithRecord() {
        LootPool.Builder recordPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(Items.MUSIC_DISC_13).setWeight(1))
                .add(LootItem.lootTableItem(Items.MUSIC_DISC_CAT).setWeight(1))
                .add(LootItem.lootTableItem(Items.MUSIC_DISC_BLOCKS).setWeight(1))
                .add(LootItem.lootTableItem(Items.MUSIC_DISC_CHIRP).setWeight(1))
                .add(LootItem.lootTableItem(Items.MUSIC_DISC_FAR).setWeight(1))
                .add(LootItem.lootTableItem(Items.MUSIC_DISC_MALL).setWeight(1))
                .add(LootItem.lootTableItem(Items.MUSIC_DISC_MELLOHI).setWeight(1))
                .add(LootItem.lootTableItem(Items.MUSIC_DISC_STAL).setWeight(1))
                .add(LootItem.lootTableItem(Items.MUSIC_DISC_STRAD).setWeight(1))
                .add(LootItem.lootTableItem(Items.MUSIC_DISC_WARD).setWeight(1))
                .add(LootItem.lootTableItem(Items.MUSIC_DISC_11).setWeight(1))
                .add(LootItem.lootTableItem(Items.MUSIC_DISC_WAIT).setWeight(1))
                .add(LootItem.lootTableItem(Items.MUSIC_DISC_PIGSTEP).setWeight(1));

        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ErsItems.FECES.get())))
                .withPool(recordPool);
    }

}
