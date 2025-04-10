/*
 * Copyright (C) 2007, 2009, 2011, 2025 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 *
 * Created on 29. September 2011 by Joerg Schaible, renamed from WoodstoxStaxWriterTest
 */
package com.thoughtworks.xstream.io.xml;

import com.ctc.wstx.stax.WstxOutputFactory;
import com.thoughtworks.xstream.core.JVM;

import javax.xml.stream.XMLOutputFactory;

public final class WstxWriterTest extends AbstractStaxWriterTest {
    protected void assertXmlProducedIs(String expected) {
        if (!staxDriver.isRepairingNamespace() || expected.matches("<\\w+:\\w+ xmlns:\\w+=.+")) {
            expected = expected.replaceAll(" xmlns=\"\"", "");
        }
        if (!JVM.isVersion(6)) { // uses by default no longer org.codehaus.woodstox:wstx-asl:3.2.7
            expected = expected.replaceAll("<(\\w+)([^>]*)/>", "<$1$2 />");
            expected = replaceAll(expected, "&#x0D;", "&#xd;");
        }
        expected = replaceAll(expected, "&gt;", ">"); // Woodstox behavior !!
        expected = expected.replaceAll("<(\\w+)([^>]*)/>", "<$1$2 />");
        expected = getXMLHeader() + expected;
        assertEquals(expected, buffer.toString());
    }

    protected String getXMLHeader() {
        return "<?xml version='1.0' encoding='UTF-8'?>";
    }

    protected XMLOutputFactory getOutputFactory() {
        return new WstxOutputFactory();
    }

    protected StaxDriver getStaxDriver() {
        return new WstxDriver();
    }
}
