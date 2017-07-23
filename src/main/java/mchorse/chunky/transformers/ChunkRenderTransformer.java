package mchorse.chunky.transformers;

import java.util.Iterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import mchorse.chunky.ClassTransformer;

public class ChunkRenderTransformer extends ClassTransformer
{
    @Override
    public void process(String name, ClassNode node)
    {
        Iterator<MethodNode> methods = node.methods.iterator();

        while (methods.hasNext())
        {
            MethodNode method = methods.next();

            if (method.name.equals("initialize"))
            {
                this.processMethodInitialize(name, method);
            }
            else if (method.name.equals("addRenderChunk"))
            {
                this.processMethodAddRenderChunk(name, method);
            }
        }
    }

    private void processMethodInitialize(String name, MethodNode method)
    {
        Iterator<AbstractInsnNode> nodes = method.instructions.iterator();
        AbstractInsnNode invoke = null;

        while (nodes.hasNext())
        {
            AbstractInsnNode node = nodes.next();

            if (node.getOpcode() == Opcodes.INVOKEINTERFACE)
            {
                invoke = node;

                break;
            }
        }

        if (invoke != null)
        {
            InsnList list = new InsnList();

            list.add(new VarInsnNode(Opcodes.ALOAD, 0));
            list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/ChunkRenderContainer", "renderChunks", "Ljava/util/List;"));
            list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "mchorse/chunky/ChunkyState", "clearRenderContainer", "(Ljava/util/List;)V", false));

            int index = method.instructions.indexOf(invoke);

            method.instructions.remove(invoke);
            method.instructions.remove(method.instructions.get(index - 1));
            method.instructions.remove(method.instructions.get(index - 2));

            method.instructions.insert(method.instructions.get(index - 3), list);
        }
    }

    private void processMethodAddRenderChunk(String name, MethodNode method)
    {
        InsnList list = new InsnList();

        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/ChunkRenderContainer", "renderChunks", "Ljava/util/List;"));
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "mchorse/chunky/ChunkyState", "addRenderChunk", "(Ljava/util/List;Lnet/minecraft/client/renderer/chunk/RenderChunk;)V", false));

        method.instructions.remove(method.instructions.get(2));
        method.instructions.remove(method.instructions.get(2));
        method.instructions.remove(method.instructions.get(2));
        method.instructions.remove(method.instructions.get(2));
        method.instructions.remove(method.instructions.get(2));

        method.instructions.insert(method.instructions.get(1), list);
    }
}