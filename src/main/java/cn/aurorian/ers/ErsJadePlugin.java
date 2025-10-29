package cn.aurorian.ers;

import cn.aurorian.ers.block.SwampDragonArtificialNestBlock;
import cn.aurorian.ers.block.be.SwampDragonArtificialNestBlockEntity;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.oasis.entity.pygopodusannulatum.PygopodusAnnulatumEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

// Jade插件注解，确保Jade能找到这个类
@WailaPlugin(EcologicalReplenishmentStation.MODID)
public class ErsJadePlugin implements IWailaPlugin {
    public static final ResourceLocation AGE = ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "age");
    public static final ResourceLocation FOOD = ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "food");
    public static final ResourceLocation HATCH = ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "hatch");

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        // 注册实体组件提供者
        registration.registerEntityComponent(AgeProvider.INSTANCE, DentisaurusLongirostrisEntity.class);
        registration.registerEntityComponent(LongirostrisFoodProvider.INSTANCE, DentisaurusLongirostrisEntity.class);
        registration.registerEntityComponent(AnnulatumFoodProvider.INSTANCE, PygopodusAnnulatumEntity.class);
        registration.registerBlockComponent(HatchProvider.INSTANCE, SwampDragonArtificialNestBlock.class);
        
        // 添加配置选项
        registration.addConfig(AGE, true);
        registration.addConfig(FOOD, true);
        registration.addConfig(HATCH, true);
    }

    private enum AgeProvider implements IEntityComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
            // 检查配置是否启用
            if (!config.get(ErsJadePlugin.AGE)) return;
            
            if (accessor.getEntity() instanceof DentisaurusLongirostrisEntity sotek) {
                int ageInDays = sotek.getAgeInDays();
                tooltip.add(Component.translatable("tooltip.ers.age", ageInDays));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return AGE;
        }
    }

    private enum LongirostrisFoodProvider implements IEntityComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
            if (!config.get(ErsJadePlugin.FOOD)) return;
            
            if (accessor.getEntity() instanceof DentisaurusLongirostrisEntity entity) {
                float hunger = entity.getHunger();
                
                tooltip.add(Component.translatable("tooltip.ers.food", hunger));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return FOOD;
        }
    }

    private enum AnnulatumFoodProvider implements IEntityComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
            if (!config.get(ErsJadePlugin.FOOD)) return;

            if (accessor.getEntity() instanceof PygopodusAnnulatumEntity entity) {
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
            if (!config.get(ErsJadePlugin.FOOD)) return;

            if (accessor.getBlockEntity() instanceof SwampDragonArtificialNestBlockEntity entity) {
                if(!entity.hasEgg())
                    return;
                int timer = entity.getHatchingTime() / 480;

                tooltip.add(Component.translatable("tooltip.ers.hatch","§a" + timer));
            }
        }
        @Override
        public ResourceLocation getUid() {
            return HATCH;
        }


    }
}