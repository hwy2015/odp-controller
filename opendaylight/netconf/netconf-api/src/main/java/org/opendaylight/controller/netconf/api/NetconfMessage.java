/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.netconf.api;

import org.w3c.dom.Document;

import com.google.common.base.Optional;

/**
 * NetconfMessage represents a wrapper around org.w3c.dom.Document. Needed for
 * implementing ProtocolMessage interface.
 */
public final class NetconfMessage {

    private static final long serialVersionUID = 462175939836367285L;

    private final Document doc;

    private String additionalHeader;

    public NetconfMessage(final Document doc) {
        this(doc, null);
    }

    public NetconfMessage(Document doc, String additionalHeader) {
        this.doc = doc;
        this.additionalHeader = additionalHeader;
    }

    public Document getDocument() {
        return this.doc;
    }

    public Optional<String> getAdditionalHeader() {
        return additionalHeader== null ? Optional.<String>absent() : Optional.of(additionalHeader);
    }
}
