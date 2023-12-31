package com.mrbysco.chowderexpress.data.server;

import com.mrbysco.chowderexpress.registry.CartRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class CartRecipes extends RecipeProvider {
	public CartRecipes(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> recipeConsumer) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, CartRegistry.SOUP_CART_ITEM.get())
				.requires(Items.MINECART).requires(Items.BOWL)
				.unlockedBy("has_bowl", has(Items.BOWL))
				.save(recipeConsumer);
	}
}
