/*
 * Copyright 2004,2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package org.apache.axis.wsdl.wsdltowom;

import org.apache.wsdl.WSDLBinding;
import org.apache.wsdl.WSDLDescription;
import org.apache.wsdl.WSDLInterface;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.PortType;

/**
 * @author chathura@opensource.lk
 */
public class WSDL4JtoWOMPump {

    public void pump(WSDLDescription wsdlDefinitiios, Definition wsdl4jDefinition) {
        //Copy the Definition's Attrebute Information items and pump the
        wsdlDefinitiios.setTargetNameSpace(wsdl4jDefinition.getTargetNamespace());
        //wsdl4jDefinition.getNamespace()
        //top level components
    }

    private void pumpInterface(WSDLInterface wsdlInterface, PortType wsdl4jPortType) {
        // Copy The PortType Info to WSDLInterface info.
    }


    private void pumpBinding(WSDLBinding wsdlBinding, Binding wsdl4jBinding) {

    }

}