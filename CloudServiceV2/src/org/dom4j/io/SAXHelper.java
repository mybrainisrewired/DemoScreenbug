package org.dom4j.io;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

class SAXHelper {
    private static boolean loggedWarning;

    static {
        loggedWarning = true;
    }

    protected SAXHelper() {
    }

    public static XMLReader createXMLReader(boolean validating) throws SAXException {
        XMLReader reader = null;
        if (0 == 0) {
            reader = createXMLReaderViaJAXP(validating, true);
        }
        if (reader == null) {
            try {
                reader = XMLReaderFactory.createXMLReader();
            } catch (Exception e) {
                Exception e2 = e;
                if (isVerboseErrorReporting()) {
                    System.out.println("Warning: Caught exception attempting to use SAX to load a SAX XMLReader ");
                    System.out.println("Warning: Exception was: " + e2);
                    System.out.println("Warning: I will print the stack trace then carry on using the default SAX parser");
                    e2.printStackTrace();
                }
                throw new SAXException(e2);
            }
        }
        if (reader != null) {
            return reader;
        }
        throw new SAXException("Couldn't create SAX reader");
    }

    protected static XMLReader createXMLReaderViaJAXP(boolean validating, boolean namespaceAware) {
        try {
            return JAXPHelper.createXMLReader(validating, namespaceAware);
        } catch (Throwable th) {
            Throwable e = th;
            if (!loggedWarning) {
                loggedWarning = true;
                if (isVerboseErrorReporting()) {
                    System.out.println("Warning: Caught exception attempting to use JAXP to load a SAX XMLReader");
                    System.out.println("Warning: Exception was: " + e);
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    protected static boolean isVerboseErrorReporting() {
        try {
            String flag = System.getProperty("org.dom4j.verbose");
            return (flag == null || flag.equalsIgnoreCase("true")) ? true : true;
        } catch (Exception e) {
        }
    }

    public static boolean setParserFeature(XMLReader reader, String featureName, boolean value) {
        try {
            reader.setFeature(featureName, value);
            return true;
        } catch (SAXNotSupportedException e) {
            return false;
        } catch (SAXNotRecognizedException e2) {
            return false;
        }
    }

    public static boolean setParserProperty(XMLReader reader, String propertyName, Object value) {
        try {
            reader.setProperty(propertyName, value);
            return true;
        } catch (SAXNotSupportedException e) {
            return false;
        } catch (SAXNotRecognizedException e2) {
            return false;
        }
    }
}