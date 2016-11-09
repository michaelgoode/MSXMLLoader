/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlreader;

import Utilities.MD5;
import entities.Colour;
import entities.Size;
import entities.Stroke;
import entities.UPC;
import java.sql.Connection;
import labels.StrokeLabel;
import java.util.ArrayList;
import java.util.TreeSet;
import labels.ColourLabel;
import labels.SizeLabel;
import labels.UPCLabel;
import managers.ConnectionType;
import managers.DBManager;
import managers.StrokeManager;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author michaelgoode
 */
public class SAXParserHandler extends DefaultHandler {

    StrokeManager strokeManager = null; // manage the list by filtering
    Connection connection = null;
    String currentLevel = "";
    private final ArrayList strokeList = new ArrayList();
    Stroke stroke = null;
    StrokeLabel strokeLabel = null;
    Colour colour = null;
    Size size = null;
    ColourLabel colourLabel = null;
    SizeLabel sizeLabel = null;
    UPCLabel upcLabel = null;
    UPC upc = null;
    String content = null;
    String elementName;
    StringBuilder buf = new StringBuilder();
    //TreeSet<String> rawItems = new TreeSet<String>();

    public SAXParserHandler(StrokeManager strokeManager) {
        this.strokeManager = strokeManager;
        DBManager db = DBManager.getInstance();
        this.connection = db.getConnection(ConnectionType.LOOKUP);
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        if ("stroke".equals(qName)) {

            stroke = new Stroke();

            stroke.setDateModified(attributes.getValue("date-last-modified"));

            currentLevel = "stroke";

        } else if ("stroke-label".equals(qName)) {

            strokeLabel = new StrokeLabel();

            currentLevel = "stroke-label";

        } else if ("colour".equals(qName)) {

            colour = new Colour();

            currentLevel = "colour";

        } else if ("size".equals(qName)) {

            size = new Size(colour);

            currentLevel = "size";

        } else if ("colour-label".equals(qName)) {

            colourLabel = new ColourLabel();

            currentLevel = "colour-label";

        } else if ("size-label".equals(qName)) {

            sizeLabel = new SizeLabel();

            currentLevel = "size-label";

        } else if ("upc".equals(qName)) {

            upc = new UPC(size);

            currentLevel = "upc";

        } else if ("upc-label".equals(qName)) {

            upcLabel = new UPCLabel();

            currentLevel = "upc-label";

        }

    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        content = buf.toString();
        buf.setLength(0);

        if (stroke != null) {

            stroke.addRawItem(content);

        }

        
        if ("stroke-number".equals(qName)) {
            stroke.setStrokeNumber(content);
        } else if ("stroke-description".equals(qName)) {
            stroke.setStrokeDescription(content);
        } else if ("contract-number".equals(qName)) {
            stroke.setContractNumber(content);
            if (content.equals("1255525")) {
                int c = 0;
            }
        } else if ("contract-status".equals(qName)) {
            stroke.setContractStatus(content);
        } else if ("department-number".equals(qName)) {
            stroke.setDepartmentNumber(content);
        } else if ("season".equals(qName)) {
            stroke.setSeason(content);
        } else if ("supplier-series".equals(qName)) {
            stroke.setSupplierSeries(content);
        } else if ("country-code".equals(qName)) {
            stroke.setCountryCode(content);;
        } else if ("productdesc".equals(qName)) {
            stroke.setProductDesc(content);
        } else if ("primary-size".equals(qName)) {
            size.setPrimarySize(content);
        } else if ("upc-number".equals(qName)) {
            upc.setUpcNumber(content);
        } else if ("secondary-size".equals(qName)) {
            upc.setSecondarySize(content);
        } else if ("selling-price".equals(qName)) {
            upc.setSellingPrice(content);
        } else if (currentLevel.equals("stroke-label")) {
            switch (qName) {
                case "label-ref":
                    strokeLabel.setLabelRef(content);
                    break;
                case "label-category":
                    strokeLabel.setLabelCategory(content);
                    break;
                case "label-type":
                    strokeLabel.setLabelType(content);
                    break;
                case "box-quantity":
                    strokeLabel.setBoxQty(content);
                    stroke.setBoxQuantity(content);
                    break;
                case "set-name":
                    strokeLabel.setSetName(content);
                    break;
                case "label-order":
                    strokeLabel.setLabelOrder(Integer.parseInt(content));
                    break;
            }
        } else if (currentLevel.equals("colour-label")) {
            switch (qName) {
                case "label-ref":
                    colourLabel.setLabelRef(content);
                    break;
                case "label-category":
                    colourLabel.setLabelCategory(content);
                    break;
                case "label-type":
                    colourLabel.setLabelType(content);
                    break;
                case "box-quantity":
                    colourLabel.setBoxQty(content);
                    colour.setBoxQuantity(content);
                    break;
                case "set-name":
                    colourLabel.setSetName(content);
                    break;
                case "label-order":
                    colourLabel.setLabelOrder(Integer.parseInt(content));
                    break;
            }
        } else if (currentLevel.equals("colour")) {
            switch (qName) {
                case "colour-name":
                    colour.setColourName(content);
                    break;
                case "colour-description":
                    colour.setColourDescription(content);
                    break;
                case "story-description":
                    colour.setStoryDescription(content);
                    break;
            }
        } else if (currentLevel.equals("size-label")) {
            switch (qName) {
                case "label-ref":
                    sizeLabel.setLabelRef(content);
                    break;
                case "label-category":
                    sizeLabel.setLabelCategory(content);
                    break;
                case "label-type":
                    sizeLabel.setLabelType(content);
                    break;
                case "box-quantity":
                    sizeLabel.setBoxQty(content);
                    size.setBoxQuantity(content);
                    break;
                case "set-name":
                    sizeLabel.setSetName(content);
                    break;
                case "label-order":
                    sizeLabel.setLabelOrder(Integer.parseInt(content));
                    break;
            }
        } else if (currentLevel.equals("upc-label")) {
            switch (qName) {
                case "label-ref":
                    upcLabel.setLabelRef(content);
                    break;
                case "label-category":
                    upcLabel.setLabelCategory(content);
                    break;
                case "label-type":
                    upcLabel.setLabelType(content);
                    break;
                case "box-quantity":
                    upcLabel.setBoxQty(content);
                    upc.setBoxQuantity(content);
                    break;
                case "set-name":
                    upcLabel.setSetName(content);
                    break;
                case "label-order":
                    upcLabel.setLabelOrder(Integer.parseInt(content));
                    break;
            }
        }

        if ("stroke-label".equals(qName)) {
            if (strokeLabel.getClass().getName().equals("labels.StrokeLabel")) {
                stroke.getLabelList().add(strokeLabel);
                currentLevel = "";
            }

        }

        if ("colour".equals(qName)) {
            if (colour.getClass().getName().equals("entities.Colour")) {
                stroke.colourList.add(colour);
                currentLevel = "";
            }
        }

        if ("size".equals(qName)) {
            if (size.getClass().getName().equals("entities.Size")) {
                colour.addSize(size);
                currentLevel = "";
            }
        }

        if ("colour-label".equals(qName)) {
            if (colourLabel.getClass().getName().equals("labels.ColourLabel")) {
                colour.getLabelList().add(colourLabel);
                currentLevel = "";
            }
        }

        if ("size-label".equals(qName)) {
            if (sizeLabel.getClass().getName().equals("labels.SizeLabel")) {
                size.getLabelList().add(sizeLabel);
                currentLevel = "";
            }
        }

        if ("upc-label".equals(qName)) {
            if (upcLabel.getClass().getName().equals("labels.UPCLabel")) {
                upc.getLabelList().add(upcLabel);
                currentLevel = "";
            }
        }

        if ("upc".equals(qName)) {
            size.upcList.add(upc);
            currentLevel = "";
        }

        if ("stroke".equals(qName)) {
            // if it is a new or modified contract for known vendors then add
            
            if (stroke.getContractNumber().equals("YX20101953")) {
                int x=0;
            }
            
            
            if (strokeManager.IsTheContractValid( stroke )) {
                strokeList.add( stroke );
            }
            currentLevel = "";
        }
        content = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (buf != null) {
            for (int i = start; i < start + length; i++) {
                buf.append(ch[i]);
            }
        }
    }

    public ArrayList getStrokes() {

        return strokeList;

    }

    private void addRawItem(String c) {

        //rawItems.add(c);
    }

}
