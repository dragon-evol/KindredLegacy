package fuzzyacornindustries.kindredlegacy.item.tamable;

import java.util.List;

import javax.annotation.Nullable;

import fuzzyacornindustries.kindredlegacy.creativetab.KindredLegacyCreativeTabs;
import fuzzyacornindustries.kindredlegacy.item.ItemBase;
import fuzzyacornindustries.kindredlegacy.reference.ModInfo;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpeedBoost extends ItemBase implements IBoostItem
{
	public ItemSpeedBoost(String name)
	{
		super(name);
		
		this.maxStackSize = 64;
	}

	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Right click a Poketamable");
		tooltip.add("to permanently raise");
		tooltip.add("speed.");
	}
}