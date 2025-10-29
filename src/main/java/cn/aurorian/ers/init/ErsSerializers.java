package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.MobAttack;
import cn.aurorian.ers.entity.MobAttackSerializer;
import cn.aurorian.ers.entity.MobRotDirection;
import cn.aurorian.ers.entity.MobRotationSerializer;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ErsSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = 
        DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, EcologicalReplenishmentStation.MODID);

    public static final RegistryObject<EntityDataSerializer<MobAttack>> MOB_ATTACK_SERIALIZER =
        SERIALIZERS.register("mob_attack", MobAttackSerializer::new);
    public static final RegistryObject<EntityDataSerializer<MobRotDirection>> MOB_ROTATION_SERIALIZER =
        SERIALIZERS.register("mob_rotation", MobRotationSerializer::new);

    public static void register(IEventBus modEventBus) {
        SERIALIZERS.register(modEventBus);
    }
}
