/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2011 Eric Lafortune (eric@graphics.cornell.edu)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.classfile.attribute.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.Field;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.ConstantValueAttribute;
import proguard.classfile.attribute.DeprecatedAttribute;
import proguard.classfile.attribute.EnclosingMethodAttribute;
import proguard.classfile.attribute.ExceptionsAttribute;
import proguard.classfile.attribute.InnerClassesAttribute;
import proguard.classfile.attribute.LineNumberTableAttribute;
import proguard.classfile.attribute.LocalVariableTableAttribute;
import proguard.classfile.attribute.LocalVariableTypeTableAttribute;
import proguard.classfile.attribute.SignatureAttribute;
import proguard.classfile.attribute.SourceDirAttribute;
import proguard.classfile.attribute.SourceFileAttribute;
import proguard.classfile.attribute.SyntheticAttribute;
import proguard.classfile.attribute.UnknownAttribute;
import proguard.classfile.attribute.annotation.AnnotationDefaultAttribute;
import proguard.classfile.attribute.annotation.RuntimeInvisibleAnnotationsAttribute;
import proguard.classfile.attribute.annotation.RuntimeInvisibleParameterAnnotationsAttribute;
import proguard.classfile.attribute.annotation.RuntimeVisibleAnnotationsAttribute;
import proguard.classfile.attribute.annotation.RuntimeVisibleParameterAnnotationsAttribute;
import proguard.classfile.attribute.preverification.StackMapAttribute;
import proguard.classfile.attribute.preverification.StackMapTableAttribute;

/**
 * This AttributeVisitor delegates all visits to each AttributeVisitor
 * in a given list.
 *
 * @author Eric Lafortune
 */
public class MultiAttributeVisitor implements AttributeVisitor
{
    private AttributeVisitor[] attributeVisitors;


    public MultiAttributeVisitor()
    {
    }


    public MultiAttributeVisitor(AttributeVisitor[] attributeVisitors)
    {
        this.attributeVisitors = attributeVisitors;
    }


    public void addAttributeVisitor(AttributeVisitor attributeVisitor)
    {
        incrementArraySize();

        attributeVisitors[attributeVisitors.length - 1] = attributeVisitor;
    }


    private void incrementArraySize()
    {
        if (attributeVisitors == null)
        {
            attributeVisitors = new AttributeVisitor[1];
        }
        else
        {
            AttributeVisitor[] newAttributeVisitors =
                new AttributeVisitor[attributeVisitors.length + 1];
            System.arraycopy(attributeVisitors, 0,
                             newAttributeVisitors, 0,
                             attributeVisitors.length);
            attributeVisitors = newAttributeVisitors;
        }
    }


    // Implementations for AttributeVisitor.


    public void visitUnknownAttribute(Clazz clazz, UnknownAttribute unknownAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitUnknownAttribute(clazz, unknownAttribute);
        }
    }


    public void visitSourceFileAttribute(Clazz clazz, SourceFileAttribute sourceFileAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitSourceFileAttribute(clazz, sourceFileAttribute);
        }
    }


    public void visitSourceDirAttribute(Clazz clazz, SourceDirAttribute sourceDirAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitSourceDirAttribute(clazz, sourceDirAttribute);
        }
    }


    public void visitInnerClassesAttribute(Clazz clazz, InnerClassesAttribute innerClassesAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitInnerClassesAttribute(clazz, innerClassesAttribute);
        }
    }


    public void visitEnclosingMethodAttribute(Clazz clazz, EnclosingMethodAttribute enclosingMethodAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitEnclosingMethodAttribute(clazz, enclosingMethodAttribute);
        }
    }


    public void visitDeprecatedAttribute(Clazz clazz, DeprecatedAttribute deprecatedAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitDeprecatedAttribute(clazz, deprecatedAttribute);
        }
    }


    public void visitSyntheticAttribute(Clazz clazz, SyntheticAttribute syntheticAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitSyntheticAttribute(clazz, syntheticAttribute);
        }
    }


    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute syntheticAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitSignatureAttribute(clazz, syntheticAttribute);
        }
    }


    public void visitDeprecatedAttribute(Clazz clazz, Field field, DeprecatedAttribute deprecatedAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitDeprecatedAttribute(clazz, field, deprecatedAttribute);
        }
    }


    public void visitSyntheticAttribute(Clazz clazz, Field field, SyntheticAttribute syntheticAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitSyntheticAttribute(clazz, field, syntheticAttribute);
        }
    }


    public void visitSignatureAttribute(Clazz clazz, Field field, SignatureAttribute syntheticAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitSignatureAttribute(clazz, field, syntheticAttribute);
        }
    }


    public void visitDeprecatedAttribute(Clazz clazz, Method method, DeprecatedAttribute deprecatedAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitDeprecatedAttribute(clazz, method, deprecatedAttribute);
        }
    }


    public void visitSyntheticAttribute(Clazz clazz, Method method, SyntheticAttribute syntheticAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitSyntheticAttribute(clazz, method, syntheticAttribute);
        }
    }


    public void visitSignatureAttribute(Clazz clazz, Method method, SignatureAttribute syntheticAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitSignatureAttribute(clazz, method, syntheticAttribute);
        }
    }


    public void visitConstantValueAttribute(Clazz clazz, Field field, ConstantValueAttribute constantValueAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitConstantValueAttribute(clazz, field, constantValueAttribute);
        }
    }


    public void visitExceptionsAttribute(Clazz clazz, Method method, ExceptionsAttribute exceptionsAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitExceptionsAttribute(clazz, method, exceptionsAttribute);
        }
    }


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitCodeAttribute(clazz, method, codeAttribute);
        }
    }


    public void visitStackMapAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapAttribute stackMapAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitStackMapAttribute(clazz, method, codeAttribute, stackMapAttribute);
        }
    }


    public void visitStackMapTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, StackMapTableAttribute stackMapTableAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitStackMapTableAttribute(clazz, method, codeAttribute, stackMapTableAttribute);
        }
    }


    public void visitLineNumberTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberTableAttribute lineNumberTableAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitLineNumberTableAttribute(clazz, method, codeAttribute, lineNumberTableAttribute);
        }
    }


    public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);
        }
    }


    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitRuntimeVisibleAnnotationsAttribute(clazz, runtimeVisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitRuntimeInvisibleAnnotationsAttribute(clazz, runtimeInvisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitRuntimeVisibleAnnotationsAttribute(clazz, field, runtimeVisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Field field, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitRuntimeInvisibleAnnotationsAttribute(clazz, field, runtimeInvisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotationsAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitRuntimeVisibleAnnotationsAttribute(clazz, method, runtimeVisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotationsAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitRuntimeInvisibleAnnotationsAttribute(clazz, method, runtimeInvisibleAnnotationsAttribute);
        }
    }


    public void visitRuntimeVisibleParameterAnnotationsAttribute(Clazz clazz, Method method, RuntimeVisibleParameterAnnotationsAttribute runtimeVisibleParameterAnnotationsAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitRuntimeVisibleParameterAnnotationsAttribute(clazz, method, runtimeVisibleParameterAnnotationsAttribute);
        }
    }


    public void visitRuntimeInvisibleParameterAnnotationsAttribute(Clazz clazz, Method method, RuntimeInvisibleParameterAnnotationsAttribute runtimeInvisibleParameterAnnotationsAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitRuntimeInvisibleParameterAnnotationsAttribute(clazz, method, runtimeInvisibleParameterAnnotationsAttribute);
        }
    }


    public void visitAnnotationDefaultAttribute(Clazz clazz, Method method, AnnotationDefaultAttribute annotationDefaultAttribute)
    {
        for (int index = 0; index < attributeVisitors.length; index++)
        {
            attributeVisitors[index].visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        }
    }
}
