package com.mrbysco.chowderexpress.data.server;

import com.mrbysco.chowderexpress.registry.CartRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CartRecipes extends RecipeProvider {
	public CartRecipes(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
		super(provider, recipeOutput);
	}

	@Override
	protected void buildRecipes() {
		shapeless(RecipeCategory.TRANSPORTATION, CartRegistry.SOUP_CART_ITEM.get())
				.requires(Items.MINECART).requires(Items.BOWL)
				.unlockedBy("has_bowl", has(Items.BOWL))
				.save(output);
	}

	public static class Runner extends RecipeProvider.Runner {
		public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			return new CartRecipes(provider, recipeOutput);
		}

		@Override
		public String getName() {
			return "Chowder Express Recipes";
		}
	}
}
