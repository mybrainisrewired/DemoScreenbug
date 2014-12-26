package com.mopub.mobileads.util.vast;

import com.mopub.common.Preconditions;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

class VastXmlManager {
    private static final String CLICK_THROUGH = "ClickThrough";
    private static final String CLICK_TRACKER = "ClickTracking";
    private static final String COMPANION = "Companion";
    private static final String COMPLETE = "complete";
    private static final String EVENT = "event";
    private static final String FIRST_QUARTILE = "firstQuartile";
    private static final String HEIGHT = "height";
    private static final String IMPRESSION_TRACKER = "Impression";
    private static final String MEDIA_FILE = "MediaFile";
    private static final String MIDPOINT = "midpoint";
    private static final String MP_IMPRESSION_TRACKER = "MP_TRACKING_URL";
    private static final String ROOT_TAG = "MPMoVideoXMLDocRoot";
    private static final String ROOT_TAG_CLOSE = "</MPMoVideoXMLDocRoot>";
    private static final String ROOT_TAG_OPEN = "<MPMoVideoXMLDocRoot>";
    private static final String START = "start";
    private static final String THIRD_QUARTILE = "thirdQuartile";
    private static final String VAST_AD_TAG = "VASTAdTagURI";
    private static final String VIDEO_TRACKER = "Tracking";
    private static final String WIDTH = "width";
    private Document mVastDoc;

    class ImageCompanionAdXmlManager {
        private static final String COMPANION_CLICK_THROUGH = "CompanionClickThrough";
        private static final String COMPANION_STATIC_RESOURCE = "StaticResource";
        private static final String CREATIVE_TYPE = "creativeType";
        private static final String CREATIVE_VIEW = "creativeView";
        private static final String TRACKING_EVENTS = "TrackingEvents";
        private final Node mCompanionNode;

        ImageCompanionAdXmlManager(Node companionNode) throws IllegalArgumentException {
            if (companionNode == null) {
                throw new IllegalArgumentException("Companion node cannot be null");
            }
            this.mCompanionNode = companionNode;
        }

        String getClickThroughUrl() {
            return XmlUtils.getNodeValue(XmlUtils.getFirstMatchingChildNode(this.mCompanionNode, COMPANION_CLICK_THROUGH));
        }

        List<String> getClickTrackers() {
            List<String> companionAdClickTrackers = new ArrayList();
            Node node = XmlUtils.getFirstMatchingChildNode(this.mCompanionNode, TRACKING_EVENTS);
            if (node != null) {
                Iterator it = XmlUtils.getMatchingChildNodes(node, VIDEO_TRACKER, EVENT, Arrays.asList(new String[]{CREATIVE_VIEW})).iterator();
                while (it.hasNext()) {
                    Node trackerNode = (Node) it.next();
                    if (trackerNode.getFirstChild() != null) {
                        companionAdClickTrackers.add(trackerNode.getFirstChild().getNodeValue().trim());
                    }
                }
            }
            return companionAdClickTrackers;
        }

        Integer getHeight() {
            return XmlUtils.getAttributeValueAsInt(this.mCompanionNode, HEIGHT);
        }

        String getImageUrl() {
            return XmlUtils.getNodeValue(XmlUtils.getFirstMatchingChildNode(this.mCompanionNode, COMPANION_STATIC_RESOURCE));
        }

        String getType() {
            return XmlUtils.getAttributeValue(XmlUtils.getFirstMatchingChildNode(this.mCompanionNode, COMPANION_STATIC_RESOURCE), CREATIVE_TYPE);
        }

        Integer getWidth() {
            return XmlUtils.getAttributeValueAsInt(this.mCompanionNode, WIDTH);
        }
    }

    class MediaXmlManager {
        private static final String DELIVERY = "delivery";
        private static final String VIDEO_TYPE = "type";
        private final Node mMediaNode;

        MediaXmlManager(Node mediaNode) throws IllegalArgumentException {
            if (mediaNode == null) {
                throw new IllegalArgumentException("Media node cannot be null");
            }
            this.mMediaNode = mediaNode;
        }

        String getDelivery() {
            return XmlUtils.getAttributeValue(this.mMediaNode, DELIVERY);
        }

        Integer getHeight() {
            return XmlUtils.getAttributeValueAsInt(this.mMediaNode, HEIGHT);
        }

        String getMediaUrl() {
            return XmlUtils.getNodeValue(this.mMediaNode);
        }

        String getType() {
            return XmlUtils.getAttributeValue(this.mMediaNode, VIDEO_TYPE);
        }

        Integer getWidth() {
            return XmlUtils.getAttributeValueAsInt(this.mMediaNode, WIDTH);
        }
    }

    VastXmlManager() {
    }

    private List<String> getVideoTrackerByAttribute(String attributeValue) {
        return XmlUtils.getStringDataAsList(this.mVastDoc, VIDEO_TRACKER, EVENT, attributeValue);
    }

    String getClickThroughUrl() {
        List<String> clickUrlWrapper = XmlUtils.getStringDataAsList(this.mVastDoc, CLICK_THROUGH);
        return clickUrlWrapper.size() > 0 ? (String) clickUrlWrapper.get(0) : null;
    }

    List<String> getClickTrackers() {
        return XmlUtils.getStringDataAsList(this.mVastDoc, CLICK_TRACKER);
    }

    List<ImageCompanionAdXmlManager> getCompanionAdXmlManagers() {
        NodeList nodes = this.mVastDoc.getElementsByTagName(COMPANION);
        List<ImageCompanionAdXmlManager> imageCompanionAdXmlManagers = new ArrayList(nodes.getLength());
        int i = 0;
        while (i < nodes.getLength()) {
            imageCompanionAdXmlManagers.add(new ImageCompanionAdXmlManager(nodes.item(i)));
            i++;
        }
        return imageCompanionAdXmlManagers;
    }

    List<String> getImpressionTrackers() {
        List<String> impressionTrackers = XmlUtils.getStringDataAsList(this.mVastDoc, IMPRESSION_TRACKER);
        impressionTrackers.addAll(XmlUtils.getStringDataAsList(this.mVastDoc, MP_IMPRESSION_TRACKER));
        return impressionTrackers;
    }

    String getMediaFileUrl() {
        List<String> urlWrapper = XmlUtils.getStringDataAsList(this.mVastDoc, MEDIA_FILE);
        return urlWrapper.size() > 0 ? (String) urlWrapper.get(0) : null;
    }

    List<MediaXmlManager> getMediaXmlManagers() {
        NodeList nodes = this.mVastDoc.getElementsByTagName(MEDIA_FILE);
        List<MediaXmlManager> mediaXmlManagers = new ArrayList(nodes.getLength());
        int i = 0;
        while (i < nodes.getLength()) {
            mediaXmlManagers.add(new MediaXmlManager(nodes.item(i)));
            i++;
        }
        return mediaXmlManagers;
    }

    String getVastAdTagURI() {
        List<String> uriWrapper = XmlUtils.getStringDataAsList(this.mVastDoc, VAST_AD_TAG);
        return uriWrapper.size() > 0 ? (String) uriWrapper.get(0) : null;
    }

    List<String> getVideoCompleteTrackers() {
        return getVideoTrackerByAttribute(COMPLETE);
    }

    List<String> getVideoFirstQuartileTrackers() {
        return getVideoTrackerByAttribute(FIRST_QUARTILE);
    }

    List<String> getVideoMidpointTrackers() {
        return getVideoTrackerByAttribute(MIDPOINT);
    }

    List<String> getVideoStartTrackers() {
        return getVideoTrackerByAttribute(START);
    }

    List<String> getVideoThirdQuartileTrackers() {
        return getVideoTrackerByAttribute(THIRD_QUARTILE);
    }

    void parseVastXml(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        String documentString = new StringBuilder(ROOT_TAG_OPEN).append(xmlString.replaceFirst("<\\?.*\\?>", Preconditions.EMPTY_ARGUMENTS)).append(ROOT_TAG_CLOSE).toString();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setCoalescing(true);
        this.mVastDoc = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(new StringReader(documentString)));
    }
}