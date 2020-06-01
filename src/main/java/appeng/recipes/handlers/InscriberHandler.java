
package appeng.recipes.handlers;


import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;

import appeng.api.AEApi;
import appeng.api.features.IInscriberRecipeBuilder;
import appeng.api.features.IInscriberRegistry;
import appeng.api.features.InscriberProcessType;
import appeng.recipes.IAERecipeFactory;
import appeng.recipes.factories.recipes.PartRecipeFactory;


public class InscriberHandler implements IAERecipeFactory
{

	@Override
	public void register( JsonObject json, JsonContext ctx )
	{
		ItemStack result = PartRecipeFactory.getResult( json, ctx );
		String mode = JSONUtils.getString( json, "mode" );

		JsonObject ingredients = JSONUtils.getJsonObject( json, "ingredients" );

		List<ItemStack> middle = Arrays.asList( CraftingHelper.getIngredient( ingredients.get( "middle" ), ctx ).getMatchingStacks() );
		ItemStack[] top = new ItemStack[] { null };
		if( ingredients.has( "top" ) )
		{
			top = CraftingHelper.getIngredient( JSONUtils.getJsonObject( ingredients, "top" ), ctx ).getMatchingStacks();
		}

		ItemStack[] bottom = new ItemStack[] { null };
		if( ingredients.has( "bottom" ) )
		{
			bottom = CraftingHelper.getIngredient( JSONUtils.getJsonObject( ingredients, "bottom" ), ctx ).getMatchingStacks();
		}

		final IInscriberRegistry reg = Api.INSTANCE.registries().inscriber();
		for( int i = 0; i < top.length; ++i )
		{
			for( int j = 0; j < bottom.length; ++j )
			{
				final IInscriberRecipeBuilder builder = reg.builder();
				builder.withOutput( result );
				builder.withProcessType( "press".equals( mode ) ? InscriberProcessType.PRESS : InscriberProcessType.INSCRIBE );
				builder.withInputs( middle );

				if( top[i] != null )
				{
					builder.withTopOptional( top[i] );
				}
				if( bottom[j] != null )
				{
					builder.withBottomOptional( bottom[j] );
				}

				reg.addRecipe( builder.build() );
			}
		}
	}

}
