package org.apache.axis.handlers.addressing;

import java.io.File;

import junit.framework.TestCase;

import org.apache.axis.deployment.DeploymentEngine;
import org.apache.axis.deployment.DeploymentException;
import org.apache.axis.description.ModuleDescription;

/**
 * Copyright 2001-2004 The Apache Software Foundation.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * <p/>
 */
public class AddressingModuleTest extends TestCase {

    /**
     * @param testName
     */
    public AddressingModuleTest(String testName) {
        super(testName);
    }

    public void testExtractAddressingInformationFromHeaders() throws DeploymentException {
        try {
            DeploymentEngine deploymentEngine = new DeploymentEngine();
            File file = new File("target/addressing.mar");
            assertTrue(file.exists());
            ModuleDescription moduleDesc = deploymentEngine.buildModule(file);
            assertNotNull(moduleDesc);
        } catch (DeploymentException e) {
            e.printStackTrace();
        }

    }

}