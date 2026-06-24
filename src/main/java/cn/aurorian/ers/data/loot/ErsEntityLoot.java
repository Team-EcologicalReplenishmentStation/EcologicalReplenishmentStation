package cn.aurorian.ers.data.loot;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.init.ErsEntities;
import cn.aurorian.ers.init.ErsItems;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.data.loot.packs.VanillaEntityLoot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class ErsEntityLoot extends VanillaEntityLoot {
    @Override
    public void generate() {
        this.add(
                ErsEntities.DENTISAURUS_LONGIROSTRIS.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.SWAMP_DRAGON_MEAT.get()))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.FLUORESCENCE_LENS.get())))
                        .withPool(LootPool.lootPool()
                                .when(LootItemRandomChanceCondition.randomChance(0.3F))
                                .add(LootItem.lootTableItem(ErsItems.SWAMP_DRAGON_CLAW.get()))
                                .apply(LootingEnchantFunction.lootingMultiplier(
                                        UniformGenerator.between(0.0F, 1.0F)))));
        this.add(
                ErsEntities.AQUICORNIS_DINOSAURIFORMIS.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.DINOSAURIFORMIS_MEAT.get()))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))
                                .apply(LootingEnchantFunction.lootingMultiplier(
                                        UniformGenerator.between(0.0F, 1.0F)))));
        this.add(
                ErsEntities.LATIMERIA_PERCOIDES.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.PERCH.get()))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))
                                .apply(LootingEnchantFunction.lootingMultiplier(
                                        UniformGenerator.between(0.0F, 1.0F)))));
        this.add(
                ErsEntities.LATIMERIA_SUCHOMIMUS.get(),
                LootTable.lootTable()
                        //
                        // .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        //                        .add(LootItem.lootTableItem(ErsItems.SUCHOMIMUS_TOOTH.get()))
                        //                        .when(LootItemRandomChanceCondition.randomChance(0.3F))
                        //
                        // .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F,
                        // 1.0F))))

                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.SUCHOMIMUS.get()))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))));
        this.add(
                ErsEntities.TACHYPLEUS_GLADIUS.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.HORSESHOE_CRAB_STINGER.get()))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.HORSESHOE_CRAB_MEAT.get()))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))));
        this.add(
                ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.CHLAMYDOSELACHOIDES_TOOTH.get()))
                                .when(LootItemRandomChanceCondition.randomChance(0.3F))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.CHLAMYDOSELACHOIDES.get()))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))));
        this.add(
                ErsEntities.TERRIDENSAURUS_SAEVUS.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.SAEVUS_MEAT.get()))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))));

        this.add(
                ErsEntities.MAGNIDISCUMYZON_SARCOPTERUS.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.SARCOPETERUS.get()))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))));

        this.add(
                ErsEntities.ARGENTUMNISCUS_ACICULAULAR.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.ACICULABULAR.get()))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))));

        this.add(
                ErsEntities.MANDGEMARE_LABIUM.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.LABIUM.get()))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))));

        this.add(
                ErsEntities.BENTHOSUCHUS_PLANIDENS.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.BENTHOSUCHUS_PLANIDENS.get()))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))));

        this.add(
                ErsEntities.TACHYCARIS_GUSTATUS.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.TACHYCARIS_GUSTATUS_MEAT.get()))));

        this.add(
                ErsEntities.REMIPES_SICARIUS.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.REMIPES_SICARIUS_MEAT.get()))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))
                                .apply(LootingEnchantFunction.lootingMultiplier(
                                        UniformGenerator.between(0.0F, 1.0F)))));

        this.add(
                ErsEntities.EOSUCHOSAURUS_ANTIQUUS.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.ANTIQUUS_MEAT.get()))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))));

        this.add(ErsEntities.PTEROCHIRUS_DUX.get(), LootTable.lootTable());

        this.add(ErsEntities.ECHINOMORPHUS_CONVERGENS.get(), LootTable.lootTable());

        this.add(
                ErsEntities.PLESIOCHELYS_LONGICOLLIS.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(ErsItems.PLESIOCHELYS_LONGICOLLIS.get()))
                                .apply(SmeltItemFunction.smelted()
                                        .when(LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)))));
    }

    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        return ForgeRegistries.ENTITY_TYPES.getValues().stream()
                .filter(entities -> Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entities))
                        .getNamespace()
                        .equals(EcologicalReplenishmentStation.MODID));
    }
}
