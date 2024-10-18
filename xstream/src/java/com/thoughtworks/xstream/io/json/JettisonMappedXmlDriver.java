/*
 * Copyright (c) 2007, 2008, 2009, 2010, 2011, 2013, 2014, 2018, 2020, 2024 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 *
 * Created on 30. March 2007 by Joerg Schaible
 */
package com.thoughtworks.xstream.io.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

import javax.xml.stream.XMLStreamException;

import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLInputFactory;
import org.codehaus.jettison.mapped.MappedXMLOutputFactory;

import com.thoughtworks.xstream.io.AbstractDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxReader;
import com.thoughtworks.xstream.io.xml.StaxWriter;


/**
 * Simple XStream driver wrapping Jettison's Mapped reader and writer. Serializes object from and to JSON. Use exactly
 * Jettison 1.2 or at least 1.4.1. Versions in between are not compatible.
 *
 * @author Dejan Bosanac
 */
public class JettisonMappedXmlDriver extends AbstractDriver {

    protected final MappedXMLOutputFactory mof;
    protected final MappedXMLInputFactory mif;
    protected final MappedNamespaceConvention convention;
    protected final boolean useSerializeAsArray;

    /**
     * Construct a JettisonMappedXmlDriver.
     */
    public JettisonMappedXmlDriver() {
        this(null);
    }

    /**
     * Construct a JettisonMappedXmlDriver with configuration.
     * <p>
     * Note, you should turn off Jettison's root element array wrapper.
     * </p>
     *
     * @param config the Jettison configuration
     */
    public JettisonMappedXmlDriver(final Configuration config) {
        this(config, true);
    }

    /**
     * Construct a JettisonMappedXmlDriver with configuration.
     * <p>
     * This constructor has been added by special request of Jettison users to support JSON generated by older Jettison
     * versions. If the driver is setup to ignore the XStream hints for JSON arrays, there is neither support from
     * XStream's side nor are there any tests to ensure this mode. You should also turn off Jettison's root element
     * wrapper.
     * </p>
     *
     * @param config the Jettison configuration or null for XStream's defaults
     * @param useSerializeAsArray flag to use XStream's hints for collections and arrays
     * @since 1.4
     */
    public JettisonMappedXmlDriver(Configuration config, final boolean useSerializeAsArray) {
        if (config == null) {
            config = new Configuration();
            config.setRootElementArrayWrapper(false);
        }
        mof = new MappedXMLOutputFactory(config);
        mif = new MappedXMLInputFactory(config);
        convention = new MappedNamespaceConvention(config);
        this.useSerializeAsArray = useSerializeAsArray;
    }

    @Override
    public HierarchicalStreamReader createReader(final Reader reader) {
        try {
            return new StaxReader(new QNameMap(), mif.createXMLStreamReader(reader), getNameCoder());
        } catch (final XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    @Override
    public HierarchicalStreamReader createReader(final InputStream input) {
        try {
            return new StaxReader(new QNameMap(), mif.createXMLStreamReader(input), getNameCoder());
        } catch (final XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    @Override
    public HierarchicalStreamReader createReader(final URL in) {
        InputStream instream = null;
        try {
            instream = in.openStream();
            return new StaxReader(new QNameMap(), mif.createXMLStreamReader(in.toExternalForm(), instream),
                getNameCoder());
        } catch (final XMLStreamException | IOException e) {
            throw new StreamException(e);
        } finally {
            if (instream != null) {
                try {
                    instream.close();
                } catch (final IOException e) {
                    // ignore
                }
            }
        }
    }

    @Override
    public HierarchicalStreamReader createReader(final File in) {
        InputStream instream = null;
        try {
            instream = new FileInputStream(in);
            return new StaxReader(new QNameMap(), mif.createXMLStreamReader(in.toURI().toASCIIString(), instream),
                getNameCoder());
        } catch (final XMLStreamException | IOException e) {
            throw new StreamException(e);
        } finally {
            if (instream != null) {
                try {
                    instream.close();
                } catch (final IOException e) {
                    // ignore
                }
            }
        }
    }

    @Override
    public HierarchicalStreamWriter createWriter(final Writer writer) {
        try {
            if (useSerializeAsArray) {
                return new JettisonStaxWriter(new QNameMap(), mof.createXMLStreamWriter(writer), getNameCoder(),
                    convention);
            } else {
                return new StaxWriter(new QNameMap(), mof.createXMLStreamWriter(writer), getNameCoder());
            }
        } catch (final XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    @Override
    public HierarchicalStreamWriter createWriter(final OutputStream output) {
        try {
            if (useSerializeAsArray) {
                return new JettisonStaxWriter(new QNameMap(), mof.createXMLStreamWriter(output), getNameCoder(),
                    convention);
            } else {
                return new StaxWriter(new QNameMap(), mof.createXMLStreamWriter(output), getNameCoder());
            }
        } catch (final XMLStreamException e) {
            throw new StreamException(e);
        }
    }

}
