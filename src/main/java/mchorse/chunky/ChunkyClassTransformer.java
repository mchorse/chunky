package mchorse.chunky;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;

import mchorse.chunky.transformers.ChunkRenderTransformer;
import mchorse.chunky.transformers.MinecraftServerTransformer;
import mchorse.chunky.transformers.RenderGlobalTransformer;
import mchorse.chunky.transformers.RenderListTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

public class ChunkyClassTransformer implements IClassTransformer
{
    public static final Logger LOGGER = LogManager.getLogger("Chunky");

    private ClassTransformer renderGlobal = new RenderGlobalTransformer();
    private ClassTransformer chunkRenderContainer = new ChunkRenderTransformer();
    private ClassMethodTransformer renderList = new RenderListTransformer("renderChunkLayer");
    private ClassMethodTransformer minecraftServer = new MinecraftServerTransformer("run");

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
        if (name.equals("net.minecraft.client.renderer.RenderGlobal"))
        {
            LOGGER.info("Chunky: Patching RenderGlobal");

            // return this.renderGlobal.transform(name, basicClass);
        }

        if (name.equals("net.minecraft.client.renderer.RenderList"))
        {
            LOGGER.info("Chunky: Patching RenderList");

            // return this.renderList.transform(name, basicClass);
        }

        if (name.equals("net.minecraft.client.renderer.ChunkRenderContainer"))
        {
            LOGGER.info("Chunky: Patching ChunkRenderContainer");

            // return this.chunkRenderContainer.transform(name, basicClass);
        }

        if (name.equals("net.minecraft.server.MinecraftServer"))
        {
            LOGGER.info("Chunky: Patching MinecraftServer");

            return this.minecraftServer.transform(name, basicClass);
        }

        return basicClass;
    }

    public static void debugInstructions(InsnList list)
    {
        debugInstructions(list, Integer.MAX_VALUE);
    }

    public static void debugInstructions(InsnList list, int max)
    {
        Iterator<AbstractInsnNode> nodes = list.iterator();

        int i = 0;

        while (nodes.hasNext())
        {
            AbstractInsnNode node = nodes.next();

            System.out.println("Offset: " + i + " " + node.getClass().getSimpleName() + " " + debugNode(node));

            if (i >= max)
            {
                break;
            }

            i++;
        }
    }

    public static String debugNode(AbstractInsnNode node)
    {
        if (node instanceof LabelNode)
        {
            return ((LabelNode) node).getLabel().toString();
        }
        else if (node instanceof LineNumberNode)
        {
            return String.valueOf(((LineNumberNode) node).line);
        }
        else if (node instanceof MethodInsnNode)
        {
            MethodInsnNode method = (MethodInsnNode) node;

            return method.getOpcode() + " " + method.owner + "." + method.name + ":" + method.desc;
        }
        else if (node instanceof FieldInsnNode)
        {
            FieldInsnNode field = (FieldInsnNode) node;

            return field.getOpcode() + " " + field.owner + "." + field.name + ":" + field.desc;
        }
        else if (node instanceof LdcInsnNode)
        {
            LdcInsnNode ldc = (LdcInsnNode) node;

            return ldc.cst.toString();
        }

        return String.valueOf(node.hashCode());
    }
}