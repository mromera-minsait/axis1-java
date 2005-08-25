package org.apache.axis2.interopt.whitemesa.round4.simple;

import org.apache.axis2.addressing.EndpointReference;

import org.apache.axis2.clientapi.Call;
import org.apache.axis2.Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.interopt.whitemesa.round4.simple.utils.WhitemesaR4ClientUtil;
import org.apache.axis2.soap.SOAPEnvelope;
import org.apache.axis2.soap.impl.llom.builder.StAXSOAPModelBuilder;
import org.apache.axis2.om.OMElement;
import org.apache.axis2.om.OMXMLParserWrapper;
import org.apache.axis2.om.impl.llom.util.XMLComparator;
import org.apache.axis2.om.impl.llom.exception.XMLComparisonException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;

import java.io.*;


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
 *
 *
 */
public class EchoBlockingClient {


    public OMElement sendMsg(WhitemesaR4ClientUtil util,String soapAction){
        OMElement firstchild=null;
//        EndpointReference targetEPR = new EndpointReference("http://www.whitemesa.net:80/interop/r4/fault-rpc" );
        EndpointReference targetEPR = new EndpointReference("http://www.whitemesa.net:80/interop/r4/fault-rpc" );

        try {


            Call call = new Call();
            call.setTo(targetEPR);
            call.setExceptionToBeThrownOnSOAPFault(false);
            call.setTransportInfo(Constants.TRANSPORT_HTTP,Constants.TRANSPORT_HTTP,false);
             call.setSoapAction(soapAction);
            //Blocking invocation

            firstchild = call.invokeBlocking("",util.getEchoOMElement());

            StringWriter writer = new StringWriter();

//            firstchild.serializeWithCache(XMLOutputFactory.newInstance()
//                    .createXMLStreamWriter(writer));
//            writer.flush();

            System.out.println(writer.toString());
//
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
//        } catch (XMLStreamException e) {
//            e.printStackTrace();

        }
        return firstchild;

    }
  
}
