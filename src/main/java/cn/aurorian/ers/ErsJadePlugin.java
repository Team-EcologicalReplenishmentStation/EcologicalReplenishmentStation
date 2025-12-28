package cn.aurorian.ers;

import cn.aurorian.ers.block.ArtificialNestBlock;
import cn.aurorian.ers.block.be.ArtificialNestBlockEntity;
import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.HasGender;
import cn.aurorian.ers.item.egg.ErsEgg;
import cn.aurorian.oasis.Oasis;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin(EcologicalReplenishmentStation.MODID)
public class ErsJadePlugin implements IWailaPlugin {
    public static final ResourceLocation AGE = EcologicalReplenishmentStation.prefix("age");
    public static final ResourceLocation FOOD = EcologicalReplenishmentStation.prefix("food");
    public static final ResourceLocation HATCH = EcologicalReplenishmentStation.prefix("hatch");
    public static final ResourceLocation GENDER = Oasis.prefix("gender");

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        // 注册实体组件提供者
        registration.registerEntityComponent(AgeProvider.INSTANCE, ErsTamable.class);
        registration.registerEntityComponent(FoodProvider.INSTANCE, ErsTamable.class);
        registration.registerEntityComponent(GenderProvider.INSTANCE, ErsTamable.class);
        registration.registerBlockComponent(HatchProvider.INSTANCE, ArtificialNestBlock.class);
    }

    private enum AgeProvider implements IEntityComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
            if (accessor.getEntity() instanceof ErsTamable<?> entity && entity.doAgeTick()) {
                int ageInDays = entity.getAgeInDays();
                tooltip.add(Component.translatable("tooltip.ers.age", ageInDays));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return AGE;
        }
    }

    private enum FoodProvider implements IEntityComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
            if (accessor.getEntity() instanceof ErsTamable<?> entity && entity.doHunger()) {
                float hunger = entity.getHunger();
                
                tooltip.add(Component.translatable("tooltip.ers.food", hunger));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return FOOD;
        }
    }

    private enum HatchProvider implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            if (accessor.getBlockEntity() instanceof ArtificialNestBlockEntity entity) {
                if(entity.getEgg().isEmpty())
                    return;
                ErsEgg eggItem = (ErsEgg) entity.getEgg().getItem();
                int timer = entity.getHatchingTime() * 100 / eggItem.getHatchTime();

                tooltip.add(Component.translatable("tooltip.ers.hatch","§a" + timer));
            }
        }
        @Override
        public ResourceLocation getUid() {
            return HATCH;
        }
    }

    private enum GenderProvider implements IEntityComponentProvider{
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
            if (accessor.getEntity() instanceof HasGender hasGender) {
                if(hasGender.getGender())
                    tooltip.add(Component.translatable("tooltip.oasis.gender.male"));
                else
                    tooltip.add(Component.translatable("tooltip.oasis.gender.female"));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return GENDER;
        }
    }
}