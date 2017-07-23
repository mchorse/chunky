package mchorse.chunky.transformers;

import java.util.Iterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import mchorse.chunky.ChunkyClassTransformer;
import mchorse.chunky.ClassTransformer;

public class RenderGlobalTransformer extends ClassTransformer
{
    @Override
    public void process(String name, ClassNode node)
    {
        Iterator<MethodNode> methods = node.methods.iterator();

        while (methods.hasNext())
        {
            MethodNode method = methods.next();

            if (method.name.equals("setupTerrain"))
            {
                this.processMethodSetupTerrain(name, method);
            }
        }
    }

    private void processMethodSetupTerrain(String name, MethodNode method)
    {
        AbstractInsnNode target = method.instructions.get(363);

        if (target != null && target instanceof LabelNode)
        {
            InsnList list = new InsnList();
            LabelNode jumpTo = new LabelNode();

            list.add(new FieldInsnNode(Opcodes.GETSTATIC, "mchorse/chunky/ChunkyState", "stopChunkUpdate", "Z"));
            list.add(new JumpInsnNode(Opcodes.IFEQ, jumpTo));
            list.add(new InsnNode(Opcodes.RETURN));
            list.add(jumpTo);

            method.instructions.insert(target, list);

            ChunkyClassTransformer.LOGGER.info("RenderGlobal.setupTerrain patched successfully!");
        }

        ChunkyClassTransformer.debugInstructions(method.instructions, 400);
    }
}