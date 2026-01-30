package com.mrbysco.chowderexpress.datagen.data;

import com.mrbysco.chowderexpress.registration.CartRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CartRecipeProvider extends RecipeProvider {
	public CartRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
		super(provider, recipeOutput);
	}

	@Override
	public void buildRecipes() {
		shapeless(RecipeCategory.TRANSPORTATION, CartRegistry.SOUP_CART_ITEM.get())
				.requires(Items.MINECART).requires(Items.BOWL)
				.unlockedBy("has_bowl", has(Items.BOWL))
				.save(output);
	}

	public static class Runner extends FabricRecipeProvider {
		public Runner(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			return new CartRecipeProvider(provider, recipeOutput);
		}

		@Override
		public String getName() {
			return "Chowder Express recipes";
		}
	}
}
