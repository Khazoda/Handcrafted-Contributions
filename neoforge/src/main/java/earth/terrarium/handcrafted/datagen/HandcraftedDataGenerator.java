package earth.terrarium.handcrafted.datagen;

import earth.terrarium.handcrafted.Handcrafted;
import earth.terrarium.handcrafted.datagen.provider.base.ModRegistryProvider;
import earth.terrarium.handcrafted.datagen.provider.client.ModBlockStateProvider;
import earth.terrarium.handcrafted.datagen.provider.client.ModHighlightBlockStateProvider;
import earth.terrarium.handcrafted.datagen.provider.client.ModItemModelProvider;
import earth.terrarium.handcrafted.datagen.provider.client.ModLangProvider;
import earth.terrarium.handcrafted.datagen.provider.server.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Handcrafted.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class HandcraftedDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new ModLangProvider(packOutput));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModHighlightBlockStateProvider(packOutput));

        generator.addProvider(event.includeServer(), new ModRegistryProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ModLootTableProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupProvider));

        generator.addProvider(event.includeServer(), new ModItemTagProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModBlockTagProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModPaintingVariantTagProvider(packOutput, lookupProvider, existingFileHelper));
    }
}
