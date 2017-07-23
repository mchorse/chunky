package mchorse.chunky.transformers;

import java.util.Iterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import mchorse.chunky.ClassMethodTransformer;

public class RenderListTransformer extends ClassMethodTransformer
{
    public RenderListTransformer(String method)
    {
        super(method);
    }

    @Override
    public void processMethod(String name, MethodNode method)
    {
        Iterator<AbstractInsnNode> nodes = method.instructions.iterator();
        AbstractInsnNode invoke = null;

        while (nodes.hasNext())
        {
            AbstractInsnNode node = nodes.next();

            if (node.getOpcode() == Opcodes.INVOKEINTERFACE && node instanceof MethodInsnNode)
            {
                MethodInsnNode methodNode = (MethodInsnNode) node;

                if (methodNode.name.equals("clear"))
                {
                    invoke = node;

                    break;
                }
            }
        }

        if (invoke != null)
        {
            InsnList list = new InsnList();

            list.add(new VarInsnNode(Opcodes.ALOAD, 0));
            list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/RenderList", "renderChunks", "Ljava/util/List;"));
            list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "mchorse/chunky/ChunkyState", "clearRenderContainer", "(Ljava/util/List;)V", false));

            int index = method.instructions.indexOf(invoke);

            method.instructions.remove(invoke);
            method.instructions.remove(method.instructions.get(index - 1));
            method.instructions.remove(method.instructions.get(index - 2));

            method.instructions.insert(method.instructions.get(index - 3), list);
        }
    }
}