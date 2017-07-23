package mchorse.chunky;

import java.util.Iterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class ClassMethodTransformer extends ClassTransformer
{
    public String method;

    public ClassMethodTransformer(String method)
    {
        this.method = method;
    }

    @Override
    public void process(String name, ClassNode node)
    {
        Iterator<MethodNode> methods = node.methods.iterator();

        while (methods.hasNext())
        {
            MethodNode method = methods.next();

            if (method.name.equals(this.method))
            {
                this.processMethod(name, method);
            }
        }
    }

    public abstract void processMethod(String name, MethodNode method);
}