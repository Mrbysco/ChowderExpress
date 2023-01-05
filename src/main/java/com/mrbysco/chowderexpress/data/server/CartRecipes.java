package com.mrbysco.chowderexpress.data.server;

import com.mrbysco.chowderexpress.registry.CartRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class CartRecipes extends RecipeProvider {
	public CartRecipes(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> recipeConsumer) {
		ShapelessRecipeBuilder.shapeless(CartRegistry.SOUP_CART_ITEM.get())
				.requires(Items.MINECART).requires(Items.BOWL)
				.unlockedBy("has_bowl", has(Items.BOWL))
				.save(recipeConsumer);
	}
}
