/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Axis" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.axis.wsdl;

import javax.wsdl.Binding;
import javax.wsdl.Operation;
import javax.wsdl.PortType;
import javax.wsdl.Fault;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

/**
 * Generate the TestCase code for use in testing services derived from the
 * generated stubs.
 *
 * @author <a href="bloritsch@apache.org">Berin Loritsch</a>
 * @version CVS $Revision$ $Date$
 */

public class TestCaseEmitter {
    private final static int IMPORTS = 1;
    private final static int HEADER = 2;
    private final static int TESTS = 3;
    private final static int DONE = 4;

    private final static String INDENT = "            ";

    private final PrintWriter writer;
    private final String className;
    private final String packageName;
    private final Emitter emitter;
    private int state = IMPORTS;

    public TestCaseEmitter(PrintWriter testCase, String packageName, String className, Emitter emit) {
        this.emitter = emit;
        this.writer = testCase;
        this.className = className;
        if (packageName == null) {
            this.packageName = null;
        } else {
            this.packageName = packageName;
        }
        this.initFile();
    }

    private final void initFile() {
         if (this.packageName != null) {
            writer.print("package ");
            writer.print(this.packageName);
            writer.println(";");
        }

        writer.println();
    }

    public final void writeHeader(String filename) {
        if (this.state > IMPORTS) {
            throw new IllegalStateException("You cannot write the header now!");
        }

        this.state = HEADER;
        writer.println("/**");
        writer.print(" * ");
        writer.println(filename);
        writer.println(" *");
        writer.println(" * This file was auto-generated from WSDL");
        writer.println(" * by the Apache Axis Wsdl2java emitter.");
        writer.println(" */");
        writer.print("public class ");
        writer.print(this.className);
        writer.println(" extends junit.framework.TestCase {");

        writer.print("    public ");
        writer.print(this.className);
        writer.println("(String name) {");
        writer.println("        super(name);");
        writer.println("    }");
    }

    public final void finish() {
        this.state = DONE;
        writer.println("}");
        writer.println();
        writer.flush();
        writer.close();
    }

    /**
     * Generate setUp()/tearDown() code for the TestCase
     */
    public final void writeInitCode() throws IOException {
        if (this.state == DONE) {
            throw new IllegalStateException("The test case is already done!");
        }

        this.state = TESTS;
    }

    public final void writeServiceTestCode(String portName, Binding binding) throws IOException {
        if (this.state > TESTS) {
            throw new IllegalStateException("You may not write any more tests!");
        }

        this.state = TESTS;
        PortType portType = binding.getPortType();
        String bindingType = portType.getQName().getLocalPart();

        writer.println();
        writer.println("    public void test" + portName + "() {");
        writer.print("        ");
        writer.print(bindingType);
        writer.print(" binding = new ");
        writer.print(this.className.substring(0, this.className.length() - "TestCase".length()));
        writer.print("().get");
        writer.print(portName);
        writer.println("();");

        writer.println("        assertTrue(binding != null);");

        this.writePortTestCode(portType);

        writer.println("    }");
    }

    private final void writePortTestCode(PortType port) throws IOException {
        Iterator ops = port.getOperations().iterator();

        while (ops.hasNext()) {
            writer.println("        try {");
            Operation op = (Operation) ops.next();
            Emitter.Parameters params = this.emitter.parameters(op);

            if ( !"void".equals( params.returnType ) ) {
                writer.print(INDENT);
                writer.print(params.returnType);
                writer.print(" value = ");

                if (  this.emitter.isPrimitiveType( params.returnType ) ) {
                    if ( "boolean".equals( params.returnType ) ) {
                        writer.println("false;");
                    } else {
                        writer.println("-3;");
                    }
                } else {
                    writer.println("null;");
                }
            }

            writer.print(INDENT);

            if ( !"void".equals(params.returnType) ) {
                writer.print("value = ");
            }

            writer.print("binding.");
            writer.print(op.getName());
            writer.print("(");

            Iterator iparam = params.list.iterator();
            boolean isFirst = true;

            while (iparam.hasNext()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    writer.print(", ");
                }

                Emitter.Parameter param = (Emitter.Parameter) iparam.next();
                String paramType = null;

                switch (param.mode) {
                    case Emitter.Parameter.IN:
                        paramType = param.type;
                        break;

                    default:
                        paramType = emitter.holder(param.type);
                }
                
                if ( this.emitter.isPrimitiveType(paramType) ) {
                    if ( "boolean".equals(paramType) ) {
                        writer.print("true");
                    } else {
                        writer.print("0");
                    }
                } else {
                    writer.print("new ");
                    writer.print(paramType);
                    writer.print("()");
                }
            }

            writer.println(");");

            if ( !"void".equals(params.returnType) ) {
                writer.print(INDENT);

                if ( this.emitter.isPrimitiveType( params.returnType ) ) {
                    if ( "boolean".equals( params.returnType ) ) {
                        writer.println("assertTrue(value != false);");
                    } else {
                        writer.println("assertTrue(value != -3);");
                    }
                } else {
                    writer.println("assertTrue(value != null);");
                }
            }

            writer.println("        } catch (java.rmi.RemoteException re) {");
            writer.print(INDENT);
            writer.println("throw new junit.framework.AssertionFailedError(\"Remote Exception caught: \" + re );");
            writer.print("        }");
            
            Map faultMap = op.getFaults();

            if (faultMap != null) {
                Iterator i = faultMap.values().iterator();
                int count = 0;

                while (i.hasNext()) {
                    count++;
                    Fault f = (Fault) i.next();
                    writer.print(" catch (");
                    writer.print(f.getName());
                    writer.println(" e" + count + ") {");
                    writer.print(INDENT);
                    writer.println("throw new junit.framework.AssertionFailedError(\"" + f.getName() + " Exception caught: \" + e" + count + ");");
                    writer.print("        }");
                }
            }

            writer.println();
        }
    }

    /**
     * Get the writer
     */
    public final PrintWriter getWriter() {
        return this.writer;
    }
}
