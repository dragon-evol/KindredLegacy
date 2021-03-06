package fuzzyacornindustries.kindredlegacy.client.renderer.entity;

import fuzzyacornindustries.kindredlegacy.client.renderer.entity.layers.LayerInfestedDeerlingInfestations;
import fuzzyacornindustries.kindredlegacy.entity.mob.hostile.EntityInfestedDeerling;
import fuzzyacornindustries.kindredlegacy.reference.ModInfo;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderInfestedDeerling extends RenderLiving<EntityInfestedDeerling>
{
	private static final ResourceLocation mainTexture = new ResourceLocation(ModInfo.MOD_ID + ":" +"textures/mobs/infested_deerling.png");

	public RenderInfestedDeerling(RenderManager renderManager, ModelBase modelBase, float shadowSize)
    {
        super(renderManager, modelBase, shadowSize);
        this.addLayer(new LayerInfestedDeerlingInfestations(this));
    }
	
	@Override
    protected ResourceLocation getEntityTexture(EntityInfestedDeerling entity)
    {
        return mainTexture;
    }
}