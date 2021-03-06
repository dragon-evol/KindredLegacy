package fuzzyacornindustries.kindredlegacy.client.renderer.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import fuzzyacornindustries.kindredlegacy.client.model.mob.tool.ModelOkamiMoonGlaive;
import fuzzyacornindustries.kindredlegacy.client.renderer.entity.layers.LayerOkamiUmbreonMarkings;
import fuzzyacornindustries.kindredlegacy.client.renderer.entity.layers.LayerOkamiUmbreonMoonGlaive;
import fuzzyacornindustries.kindredlegacy.entity.mob.tamable.EntityOkamiUmbreon;
import fuzzyacornindustries.kindredlegacy.reference.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderOkamiUmbreon extends RenderLiving<EntityOkamiUmbreon>
{
	private static final Logger LOGGER9 = LogManager.getLogger();

	private static final ResourceLocation mainTexture = new ResourceLocation(ModInfo.MOD_ID + ":" +"textures/mobs/tamables/okami_umbreon.png");
	private static final ResourceLocation hurtTexture = new ResourceLocation(ModInfo.MOD_ID + ":" +"textures/mobs/tamables/okami_umbreon_hurt.png");
	private static final ResourceLocation happyTexture = new ResourceLocation(ModInfo.MOD_ID + ":" +"textures/mobs/tamables/okami_umbreon_happy.png");

	private static final ResourceLocation moonGlaiveTexture = new ResourceLocation(ModInfo.MOD_ID + ":" +"textures/mobs/tamables/okami_weapon/moon_glaive.png");
	private static final ResourceLocation moonGlaiveTextureGlows = new ResourceLocation(ModInfo.MOD_ID + ":" +"textures/mobs/tamables/okami_weapon/moon_glaive_glows.png");

	public ModelOkamiMoonGlaive modelMoonGlaive;

	public RenderOkamiUmbreon(RenderManager renderManager, ModelBase modelBase, float shadowSize)
	{
		super(renderManager, modelBase, shadowSize);

		this.modelMoonGlaive = new ModelOkamiMoonGlaive();

        this.addLayer(new LayerOkamiUmbreonMarkings(this));
        this.addLayer(new LayerOkamiUmbreonMoonGlaive(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityOkamiUmbreon tamableEntity)
	{
		switch (tamableEntity.getMainTextureType())
		{
		case 0:
			return mainTexture;
		case 1:
			return hurtTexture;
		}

		return happyTexture;
	}

	@Override
	public void doRender(EntityOkamiUmbreon entity, double xCoord, double yCoord, double zCoord, float entityYaw, float partialTicks)
	{
		super.doRender(entity, xCoord, yCoord, zCoord, entityYaw, partialTicks);

		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		this.modelMoonGlaive.swingProgress = this.getSwingProgress(entity, partialTicks);
		boolean shouldSit = entity.isRiding() && (entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit());
		this.modelMoonGlaive.isRiding = shouldSit;
		this.modelMoonGlaive.isChild = entity.isChild();

		try
		{
			float yawOffset = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
			float yawHead = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
			float yawHeadOffsetDifference = yawHead - yawOffset;

			if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase)
			{
				EntityLivingBase entitylivingbase = (EntityLivingBase)entity.getRidingEntity();
				yawOffset = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
				float yawRotationDifference = MathHelper.wrapDegrees(yawHeadOffsetDifference);

				if (yawRotationDifference < -85.0F)
				{
					yawRotationDifference = -85.0F;
				}

				if (yawRotationDifference >= 85.0F)
				{
					yawRotationDifference = 85.0F;
				}

				yawOffset = yawHead - yawRotationDifference;

				if (yawRotationDifference * yawRotationDifference > 2500.0F)
				{
					yawOffset += yawRotationDifference * 0.2F;
				}
			}

			float pitchRotation = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
			this.renderLivingAt(entity, xCoord, yCoord, zCoord);
			float yawHandleRotationDifference = this.handleRotationFloat(entity, partialTicks);
			this.applyRotations(entity, yawHandleRotationDifference, yawOffset, partialTicks);
			float modelSize = this.prepareScale(entity, partialTicks);
			float horzVelocity = 0.0F;
			float distanceMoved = 0.0F;

			if (!entity.isRiding())
			{
				horzVelocity = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
				distanceMoved = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);

				if (entity.isChild())
				{
					distanceMoved *= 3.0F;
				}

				if (horzVelocity > 1.0F)
				{
					horzVelocity = 1.0F;
				}
			}

			GlStateManager.enableAlpha();
			this.modelMoonGlaive.setLivingAnimations(entity, distanceMoved, horzVelocity, partialTicks);
			this.renderMoonGlaive(entity, distanceMoved, horzVelocity, yawHandleRotationDifference, yawHeadOffsetDifference, pitchRotation, modelSize, partialTicks);

			if (this.renderOutlines)
			{
				boolean flag1 = this.setScoreTeamColor(entity);
				GlStateManager.enableColorMaterial();
				GlStateManager.enableOutlineMode(this.getTeamColor(entity));

				if (!this.renderMarker)
				{
					this.renderModel(entity, distanceMoved, horzVelocity, yawHandleRotationDifference, yawHeadOffsetDifference, pitchRotation, modelSize);
				}

				if (!((EntityLivingBase)entity instanceof EntityPlayer))
				{
					this.renderLayers(entity, distanceMoved, horzVelocity, partialTicks, yawHandleRotationDifference, yawHeadOffsetDifference, pitchRotation, modelSize);
				}

				GlStateManager.disableOutlineMode();
				GlStateManager.disableColorMaterial();

				if (flag1)
				{
					this.unsetScoreTeamColor();
				}
			}
			else
			{
				boolean flag = this.setDoRenderBrightness(entity, partialTicks);
				this.renderModel(entity, distanceMoved, horzVelocity, yawHandleRotationDifference, yawHeadOffsetDifference, pitchRotation, modelSize);

				if (flag)
				{
					this.unsetBrightness();
				}

				GlStateManager.depthMask(true);

				this.renderLayers(entity, distanceMoved, horzVelocity, partialTicks, yawHandleRotationDifference, yawHeadOffsetDifference, pitchRotation, modelSize);
			}

			GlStateManager.disableRescaleNormal();
		}
		catch (Exception exception)
		{
			LOGGER9.error((String)"Couldn\'t render entity", (Throwable)exception);
		}

		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.enableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

	protected void renderMoonGlaive(EntityLivingBase entity, float distanceMoved, float horzVelocity, float yawRotationDifference, float yaw, float pitchRotation, float modelSize, float partialTicks)
	{
		this.bindTexture(this.moonGlaiveTexture);

		if (!entity.isInvisible())
		{
			this.modelMoonGlaive.render(entity, distanceMoved, horzVelocity, yawRotationDifference, yaw, pitchRotation, modelSize);
		}
		else if (!entity.isInvisibleToPlayer(Minecraft.getMinecraft().player))
		{
			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
			this.modelMoonGlaive.render(entity, distanceMoved, horzVelocity, yawRotationDifference, yaw, pitchRotation, modelSize);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glPopMatrix();
			GL11.glDepthMask(true);
		}
		else
		{
			this.modelMoonGlaive.setRotationAngles(distanceMoved, horzVelocity, yawRotationDifference, yaw, pitchRotation, modelSize, entity);
		}
	}

	@Override
	public void renderName(EntityOkamiUmbreon tamableEntity, double xCoord, double yCoord, double zCoord)
	{
		String name = tamableEntity.getName();

		if (!name.equals("") && !name.equals("Okami Umbreon"))
		{
			renderLivingLabel(tamableEntity, name, xCoord, yCoord, zCoord, 64);
		}

		super.renderName(tamableEntity, xCoord, yCoord, zCoord);
	}
}