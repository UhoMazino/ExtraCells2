package extracells.block;

import appeng.api.AEApi;
import appeng.api.networking.IGridNode;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extracells.Extracells;
import extracells.container.ContainerVibrationChamberFluid;
import extracells.gui.GuiVibrationChamberFluid;
import extracells.network.GuiHandler;
import extracells.tileentity.TileEntityVibrationChamberFluid;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockVibrationChamberFluid extends BlockContainer implements TGuiBlock {

    private IIcon[] icons = new IIcon[3];

    public BlockVibrationChamberFluid()
    {
        super(Material.iron);
        setCreativeTab(Extracells.ModTab());
        setHardness(2.0F);
        setResistance(10.0F);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float p_149727_7_,
                                    float p_149727_8_, float p_149727_9_) {
        if (world.isRemote)
            return false;
        GuiHandler.launchGui(0, player, world, x, y, z);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityVibrationChamberFluid();
    }

    @Override
    public void registerBlockIcons(IIconRegister IIconRegister) {
        icons[0] = IIconRegister.registerIcon("extracells:VibrationChamberFluid");
        icons[1] = IIconRegister.registerIcon("extracells:VibrationChamberFluidFront");
        icons[2] = IIconRegister.registerIcon("extracells:VibrationChamberFluidFrontOn");
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        switch (side)
        {
            case 2:
                if(((TileEntityVibrationChamberFluid)world.getTileEntity(x, y, z)).getBurnTime() > 0 && ((TileEntityVibrationChamberFluid)world.getTileEntity(x, y, z)).getBurnTime() < ((TileEntityVibrationChamberFluid)world.getTileEntity(x, y, z)).getBurnTimeTotal())
                return icons[2];
                else
                    return icons[1];
            default:
                return icons[0];
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z){
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if(tileEntity != null && tileEntity instanceof  TileEntityVibrationChamberFluid)
        return new GuiVibrationChamberFluid(player, (TileEntityVibrationChamberFluid)tileEntity);
        return null;
    }

    @Override
    public Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z){
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if(tileEntity != null && tileEntity instanceof  TileEntityVibrationChamberFluid)
            return new ContainerVibrationChamberFluid(player.inventory, (TileEntityVibrationChamberFluid)tileEntity);
        return null;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);

        if (world.isRemote)
            return;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null) {
            if (tile instanceof TileEntityVibrationChamberFluid) {
                IGridNode node = ((TileEntityVibrationChamberFluid) tile).getGridNode(ForgeDirection.UNKNOWN);
                if (node != null) {
                    node.destroy();
                }
            }
        }
    }

}
