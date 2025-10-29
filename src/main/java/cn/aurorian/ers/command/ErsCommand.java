package cn.aurorian.ers.command;


import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.VariantHolder;

public class ErsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ers")
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("age")
                .then(Commands.argument("target", EntityArgument.entity())
                    .then(Commands.argument("days", IntegerArgumentType.integer(0))
                        .executes(context -> setAge(context,
                            EntityArgument.getEntity(context, "target"),
                            IntegerArgumentType.getInteger(context, "days"))))))
            .then(Commands.literal("elite")
                    .then(Commands.argument("target", EntityArgument.entity())
                        .executes(context -> setElite(context,
                                EntityArgument.getEntity(context, "target")))))
            .then(Commands.literal("tame")
                    .then(Commands.argument("target", EntityArgument.entity())
                        .executes(context -> setTame(context,
                                EntityArgument.getEntity(context, "target")))))
            .then(Commands.literal("variant")
                    .then(Commands.literal("set")
                        .then(Commands.argument("target", EntityArgument.entity())
                        .then(Commands.argument("id", IntegerArgumentType.integer(0))
                             .executes(context -> setVariant(context,
                                EntityArgument.getEntity(context, "target"),
                                IntegerArgumentType.getInteger(context, "id"))))))
                    .then(Commands.literal("get")
                            .then(Commands.argument("target", EntityArgument.entity())
                                    .executes(context -> getVariant(context,
                                            EntityArgument.getEntity(context, "target")))))
            )
        );
    }

    private static int setElite(CommandContext<CommandSourceStack> context, Entity target) {
        if (target instanceof DentisaurusLongirostrisEntity dentisaurusLongirostrisEntity) {
            dentisaurusLongirostrisEntity.setCanBeElite(true);
            if(dentisaurusLongirostrisEntity.getAgeInDays() >= 38) {
                dentisaurusLongirostrisEntity.setElite(true);
            }
            dentisaurusLongirostrisEntity.updateFromAgeServer();
            context.getSource().sendSuccess(() ->
                Component.translatable("commands.ers.elite.success", target.getName()),
                true);
            return 1;
        } else {
            context.getSource().sendFailure(
                Component.translatable("commands.ers.elite.failed",
                    target.getName()));
            return 0;
        }
    }

    private static int setTame(CommandContext<CommandSourceStack> context, Entity target) throws CommandSyntaxException {
        if (context.getSource().isPlayer() && target instanceof ErsTamable<?> tamable) {
            tamable.tame(context.getSource().getPlayerOrException());
            return 1;
        }
        return 0;
    }

    private static int setVariant(CommandContext<CommandSourceStack> context, Entity target, int variant){
        if (context.getSource().isPlayer() && target instanceof ErsTamableVehicle<?> tamable && target instanceof VariantHolder<?>) {
            tamable.setVariantId(variant);
            return 1;
        }
        return 0;
    }

    private static int getVariant(CommandContext<CommandSourceStack> context, Entity target){
        if (context.getSource().isPlayer() && target instanceof ErsTamableVehicle<?> tamable && target instanceof VariantHolder<?>) {
            int variant = tamable.getVariantId();
            context.getSource().sendSuccess(() ->
                Component.literal("Variant ID: " + variant),
                true);
            return 1;
        }
        return 9;
    }

    private static int setAge(CommandContext<CommandSourceStack> context, Entity target, int days) {
        if (target instanceof DentisaurusLongirostrisEntity dentisaurusLongirostrisEntity) {
            dentisaurusLongirostrisEntity.setAgeInDays(days);
            if(days < 38){
                dentisaurusLongirostrisEntity.setElite(false);
                if(days < 20){
                    dentisaurusLongirostrisEntity.setMature(false);
                }
            }
            dentisaurusLongirostrisEntity.updateFromAgeServer();
            context.getSource().sendSuccess(() -> 
                Component.translatable("commands.ers.age.success", target.getName(), days),
                true);
            return 1;
        } else {
            context.getSource().sendFailure(
                Component.translatable("commands.ers.age.failed",
                    target.getName()));
            return 0;
        }
    }
}
