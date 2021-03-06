package mchorse.chunky.transformers;

import java.util.Iterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;

import mchorse.chunky.ChunkyClassTransformer;
import mchorse.chunky.ClassMethodTransformer;

public class MinecraftServerTransformer extends ClassMethodTransformer
{
    public MinecraftServerTransformer(String method)
    {
        super(method);
    }

    @Override
    public void processMethod(String name, MethodNode method)
    {
        Iterator<AbstractInsnNode> nodes = method.instructions.iterator();
        AbstractInsnNode prev = null;

        while (nodes.hasNext())
        {
            AbstractInsnNode node = nodes.next();

            if (node instanceof LdcInsnNode)
            {
                LdcInsnNode ldc = (LdcInsnNode) node;

                if (ldc.cst instanceof Long && ((Long) ldc.cst).longValue() == 2000L && prev instanceof LineNumberNode)
                {
                    ldc.cst = Long.valueOf(51L);

                    ChunkyClassTransformer.LOGGER.info("Successfully patched MinecraftServer");
                }
            }

            prev = node;
        }
    }
}