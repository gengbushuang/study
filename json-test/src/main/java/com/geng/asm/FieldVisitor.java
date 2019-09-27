package com.geng.asm;

/**
 * A visitor to visit a Java field. The methods of this interface must be called in the following order: (
 * <tt>visitAnnotation</tt> | <tt>visitAttribute</tt> )* <tt>visitEnd</tt>.
 * 
 * @author Eric Bruneton
 */
public interface FieldVisitor {

    /**
     * Visits the end of the field. This method, which is the last one to be called, is used to inform the visitor that
     * all the annotations and attributes of the field have been visited.
     */
    void visitEnd();
}