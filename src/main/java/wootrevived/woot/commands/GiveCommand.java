package wootrevived.woot.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.TagValueOutput;
import wootrevived.api.WootFactoryMob;
import wootrevived.woot.blocks.fake_spawner.FakeSpawnerBlockEntity;
import wootrevived.woot.events.InitDropSimulator;
import wootrevived.woot.registries.WootFactoryMobsRegistry;
import wootrevived.woot.util.helper.SerializeEntityValueHelper;

public class GiveCommand {
    private static final SuggestionProvider<CommandSourceStack> suggestionProvider = (commandContext, suggestionsBuilder) -> {
        return SharedSuggestionProvider.suggestResource(InitDropSimulator.mobLocations, suggestionsBuilder);
    };

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("give")
                .requires(cs -> cs.hasPermission(2))
                .then(
                        Commands.argument("target", EntityArgument.player())
                                .then(
                                        Commands.argument("entity", ResourceLocationArgument.id()).suggests(suggestionProvider)
                                                .executes(ctx -> giveItem(
                                                        ctx.getSource(),
                                                        EntityArgument.getPlayer(ctx, "target"),
                                                        ResourceLocationArgument.getId(ctx, "entity"),
                                                        new CompoundTag()
                                                ))
                                                .then(
                                                        Commands.argument("nbt", CompoundTagArgument.compoundTag())
                                                                .executes(ctx -> giveItem(
                                                                        ctx.getSource(),
                                                                        EntityArgument.getPlayer(ctx, "target"),
                                                                        ResourceLocationArgument.getId(ctx, "entity"),
                                                                        CompoundTagArgument.getCompoundTag(ctx, "nbt")
                                                                ))
                                                )
                                )
                );
    }

    private static int giveItem(CommandSourceStack source, ServerPlayer target, ResourceLocation resourceLocation, CompoundTag tag) {
        Holder.Reference<EntityType<?>> entityTypeholder = BuiltInRegistries.ENTITY_TYPE.get(resourceLocation).orElse(null);
        if(entityTypeholder == null)
            return 1;

        EntityType<?> entityType = entityTypeholder.getDelegate().value();

        if(!WootFactoryMobsRegistry.hasFactoryMob(entityType))
            return 1;

        WootFactoryMob<?> mob = WootFactoryMobsRegistry.getFactoryMob(entityType);
        if(mob.isBlacklisted())
            return 1;

        tag.putString("id", resourceLocation.toString());

        Entity entity = entityType.create(source.getLevel(), EntitySpawnReason.SPAWNER);
        if(entity instanceof LivingEntity){
            TagValueOutput output = TagValueOutput.createWithContext(SerializeEntityValueHelper.REPORTER, source.getLevel().registryAccess());
            mob.saveTag(SerializeEntityValueHelper.serialize(entity, source.getLevel().registryAccess()), output);
            ItemStack fakeSpawner = FakeSpawnerBlockEntity.getItemStack(output.buildResult());
            target.getInventory().placeItemBackInInventory(fakeSpawner);
        }

        return 1;
    }
}