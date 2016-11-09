/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlreader;

import entities.Callout;
import entities.Size;
import entities.Stroke;
import entities.UPC;
import entities.Colour;
import labels.UPCLabel;
import labels.SizeLabel;
import labels.ColourLabel;
import labels.StrokeLabel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import managers.StrokeManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author michaelgoode
 */
public class StrokeXMLParser {

    public ArrayList parseXML_SAX(File xmlFile, StrokeManager strokeManager) {

        ArrayList<Stroke> strokes = new ArrayList<Stroke>();

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();

            SAXParser saxParser = factory.newSAXParser();

            FileInputStream inputStream = new FileInputStream(xmlFile);

            Reader reader = new InputStreamReader(inputStream, "UTF-8");

            InputSource is = new InputSource(reader);

            is.setEncoding("UTF-8");

            SAXParserHandler handler = new SAXParserHandler(strokeManager);

            saxParser.parse(is, handler);

            strokes = handler.getStrokes();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return strokes;

    }

    public void writeCSV(ArrayList<Stroke> strokes, BufferedWriter out) {

        for (int i = 0; i < strokes.size(); i++) {

            Stroke aStroke = strokes.get(i);

            aStroke.writeCSV(out);

        }

    }

    public void writeXML(ArrayList<Stroke> strokes, File xmlFile) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("strokes");
            Node node = null;
            node = doc.appendChild(rootElement);

            Iterator iter = strokes.iterator();
            while (iter.hasNext()) {
                Stroke stroke = (Stroke) iter.next();
                Node strokeNode = addChildTextNode(doc, node, "stroke", stroke.getStrokeNumber());

                Node calloutNode = addChildNode(doc, strokeNode, "callouts");

                Iterator calloutIter = stroke.getCallouts().iterator();
                while (calloutIter.hasNext()) {

                    Callout callout = (Callout) calloutIter.next();

                    addChildTextNode(doc, calloutNode, "product", callout.getProductCallout());

                }

            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            transformer.transform(source, new StreamResult(xmlFile));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Node getChildStroke(Document doc, String value) {

        Element node = doc.createElement("stroke");
        node.appendChild(doc.createTextNode(value));
        return node;

    }

    public Node addChildTextNode(Document doc, Node node, String name, String value) {

        Element nameNode = doc.createElement(name);
        nameNode.appendChild(doc.createTextNode(value));
        return node.appendChild(nameNode);

    }

    public Node addChildNode(Document doc, Node node, String name) {

        Element nameNode = doc.createElement(name);
        return node.appendChild(nameNode);

    }

    public ArrayList parseXML_DOM(File xmlFile/*, File dtdFile*/) {

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setValidating(true);

            DocumentBuilder builder = factory.newDocumentBuilder();

            builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {
                    //System.out.println(exception.getMessage());
                    System.exit(0);
                }

                @Override
                public void error(SAXParseException e) throws SAXException {

                    //System.out.println("Error at " + e.getLineNumber() + " line.");
                    //System.out.println(e.getMessage());
                    System.exit(0);
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                }
            });





            InputStream inputStream = new FileInputStream(xmlFile);
            Reader reader = new InputStreamReader(inputStream, "UTF-8");

            InputSource is = new InputSource(reader);
            is.setEncoding("UTF-8");


            ArrayList<Stroke> strokeList = new ArrayList();

            Document document = builder.parse(is);

            DOMSource source = new DOMSource(document);

            StreamResult result = new StreamResult(System.out);

            //TransformerFactory tf = TransformerFactory.newInstance();

            //Transformer transformer = tf.newTransformer();

            //transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "C:\\MandS\\Label-Information modified.dtd");

            //transformer.transform(source, result);



            document.getDocumentElement().normalize();

            NodeList nodeList = document.getDocumentElement().getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Stroke stroke = getStroke(nodeList.item(i)); // we have a stroke

                    Element strokeElement = (Element) nodeList.item(i);

                    if (stroke.getStrokeNumber().equals("0801")) {
                        int xx = 0;
                    }

                    getStrokeLabel(stroke, strokeElement);

                    getColour(stroke, strokeElement);

                    //stroke.getCallouts();

                    strokeList.add(stroke);

                }

            }

            return strokeList;

        } catch (SAXException ex) {

            ex.printStackTrace();

            return null;

        } catch (ParserConfigurationException ex) {

            ex.printStackTrace();

            return null;

        } catch (IOException ex) {

            ex.printStackTrace();

            return null;

        } /*catch (TransformerConfigurationException ex) {

         ex.printStackTrace();

         return null;

         } catch (TransformerException ex) {

         ex.printStackTrace();

         return null;

         }*/
    }

    private Stroke getStroke(Node node) {

        if (node.getNodeType() == Node.ELEMENT_NODE) {

            Element strokeElement = (Element) node;

            Stroke stroke = new Stroke();
            stroke.setDateModified(strokeElement.getAttribute("date-last-modified"));
            stroke.setStrokeNumber(strokeElement.getElementsByTagName("stroke-number").item(0).getTextContent());
            stroke.setStrokeDescription(strokeElement.getElementsByTagName("stroke-description").item(0).getTextContent());
            stroke.setContractNumber(strokeElement.getElementsByTagName("contract-number").item(0).getTextContent());
            stroke.setContractStatus(strokeElement.getElementsByTagName("contract-status").item(0).getTextContent());
            stroke.setDepartmentNumber(strokeElement.getElementsByTagName("department-number").item(0).getTextContent());
            stroke.setSeason(strokeElement.getElementsByTagName("season").item(0).getTextContent());
            stroke.setSupplierSeries(strokeElement.getElementsByTagName("supplier-series").item(0).getTextContent());
            stroke.setCountryCode(strokeElement.getElementsByTagName("country-code").item(0).getTextContent());
            stroke.setProductDesc(strokeElement.getElementsByTagName("productdesc").item(0).getTextContent());

            return stroke;

        } else {
            return null;
        }

    }

    private void getStrokeLabel(Stroke stroke, Element strokeElement) {

        NodeList strokeLabelList = strokeElement.getElementsByTagName("stroke-label");
        for (int i = 0; i < strokeLabelList.getLength(); i++) {

            Node strokeLabelNode = strokeLabelList.item(i);

            if (strokeLabelNode.getNodeType() == Node.ELEMENT_NODE) {

                Element strokeLabelElement = (Element) strokeLabelNode;

                StrokeLabel strokeLabel = new StrokeLabel();

                strokeLabel.setLabelRef(strokeLabelElement.getElementsByTagName("label-ref").item(0).getTextContent());
                strokeLabel.setLabelCategory(strokeLabelElement.getElementsByTagName("label-category").item(0).getTextContent());
                strokeLabel.setLabelType(strokeLabelElement.getElementsByTagName("label-type").item(0).getTextContent());

                if (strokeLabelElement.getElementsByTagName("label-order").item(0) != null) {
                    strokeLabel.setLabelOrder(Integer.parseInt(strokeLabelElement.getElementsByTagName("label-order").item(0).getTextContent()));
                }
                if (strokeLabelElement.getElementsByTagName("set-name").item(0) != null) {
                    strokeLabel.setSetName(strokeLabelElement.getElementsByTagName("set-name").item(0).getTextContent());
                }
                if (strokeLabelElement != null) {
                    if (strokeLabelElement.getElementsByTagName("box-quantity").item(0) != null) {
                        strokeLabel.setBoxQty(strokeLabelElement.getElementsByTagName("box-quantity").item(0).getTextContent());
                        //if (stroke.getBoxQuantity().equals("")) {
                        stroke.setBoxQuantity(strokeLabelElement.getElementsByTagName("box-quantity").item(0).getTextContent());
                        //}
                    }
                }

                stroke.getLabelList().add(strokeLabel);

            }

        }

    }

    private void getColour(Stroke stroke, Element strokeElement) {

        NodeList colourList = strokeElement.getElementsByTagName("colour");
        for (int j = 0; j < colourList.getLength(); j++) {

            Node colourNode = colourList.item(j);

            if (colourNode.getNodeType() == Node.ELEMENT_NODE) {

                Element colourElement = (Element) colourNode;

                Colour colour = new Colour();

                colour.setColourName(colourElement.getElementsByTagName("colour-name").item(0).getTextContent());
                colour.setColourDescription(colourElement.getElementsByTagName("colour-description").item(0).getTextContent());
                colour.setStoryDescription(colourElement.getElementsByTagName("story-description").item(0).getTextContent());



                getColourLabel(stroke, colour, colourElement);

                getSizeList(stroke, colour, colourElement);

                stroke.colourList.add(colour);

            }
        }

    }

    private void getColourLabel(Stroke stroke, Colour colour, Element colourElement) {

        NodeList colourLabelList = colourElement.getElementsByTagName("colour-label");

        try {

            for (int i = 0; i < colourLabelList.getLength(); i++) {

                Node colourLabelNode = colourLabelList.item(i);

                if (colourLabelNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element colourLabelElement = (Element) colourLabelNode;

                    ColourLabel colourLabel = new ColourLabel();

                    colourLabel.setLabelRef(colourLabelElement.getElementsByTagName("label-ref").item(0).getTextContent());
                    colourLabel.setLabelCategory(colourLabelElement.getElementsByTagName("label-category").item(0).getTextContent());
                    colourLabel.setLabelType(colourLabelElement.getElementsByTagName("label-type").item(0).getTextContent());

                    if (colourLabelElement.getElementsByTagName("label-order").item(0) != null) {
                        colourLabel.setLabelOrder(Integer.parseInt(colourLabelElement.getElementsByTagName("label-order").item(0).getTextContent()));
                    } else {
                        colourLabel.setLabelOrder(0);
                    }
                    if (colourLabelElement.getElementsByTagName("set-name").item(0) != null) {
                        colourLabel.setSetName(colourLabelElement.getElementsByTagName("set-name").item(0).getTextContent());
                    } else {
                        colourLabel.setSetName("");
                    }
                    if (colourLabelElement.getElementsByTagName("box-quantity").item(0) != null) {
                        colourLabel.setBoxQty(colourLabelElement.getElementsByTagName("box-quantity").item(0).getTextContent());
                        //if (colour.getBoxQuantity().equals("")) {
                        colour.setBoxQuantity(colourLabel.getBoxQty());
                        //}

                    }

                    colour.getLabelList().add(colourLabel);

                }

            }
        } catch (Exception ex) {
            //System.out.println(stroke.getStrokeNumber());
            ex.printStackTrace();

        }

    }

    private void getSizeList(Stroke stroke, Colour colour, Element colourElement) {

        NodeList sizeList = colourElement.getElementsByTagName("size");

        for (int i = 0; i < sizeList.getLength(); i++) {

            Node sizeNode = sizeList.item(i);

            if (sizeNode.getNodeType() == Node.ELEMENT_NODE) {

                Element sizeElement = (Element) sizeNode;

                Size size = new Size(colour);

                size.setPrimarySize(sizeElement.getElementsByTagName("primary-size").item(0).getTextContent());

                NodeList sizeLabelList = sizeElement.getElementsByTagName("size-label");

                for (int j = 0; j < sizeLabelList.getLength(); j++) {

                    Node sizeLabelNode = sizeLabelList.item(j);

                    Element sizeLabelElement = (Element) sizeLabelNode;

                    if (sizeElement.getElementsByTagName("size-label").item(0) != null) {
                        SizeLabel sizeLabel = new SizeLabel();
                        sizeLabel.setLabelRef(sizeLabelElement.getElementsByTagName("label-ref").item(0).getTextContent());
                        sizeLabel.setLabelCategory(sizeLabelElement.getElementsByTagName("label-category").item(0).getTextContent());
                        sizeLabel.setLabelType(sizeLabelElement.getElementsByTagName("label-type").item(0).getTextContent());
                        if (sizeLabelElement.getElementsByTagName("label-order").item(0) != null) {
                            sizeLabel.setLabelOrder(Integer.parseInt(sizeLabelElement.getElementsByTagName("label-order").item(0).getTextContent()));
                        } else {
                            sizeLabel.setLabelOrder(0);
                        }
                        if (sizeLabelElement.getElementsByTagName("set-name").item(0) != null) {
                            sizeLabel.setSetName(sizeLabelElement.getElementsByTagName("set-name").item(0).getTextContent());
                        } else {
                            sizeLabel.setSetName("");
                        }
                        if (sizeLabelElement.getElementsByTagName("box-quantity").item(0) != null) {
                            sizeLabel.setBoxQty(sizeLabelElement.getElementsByTagName("box-quantity").item(0).getTextContent());
                            size.setBoxQuantity(sizeLabel.getBoxQty());
                        }
                        size.getLabelList().add(sizeLabel);
                    }
                }

                NodeList upcList = sizeElement.getElementsByTagName("upc");

                for (int j = 0; j < upcList.getLength(); j++) {

                    Node upcNode = upcList.item(j);

                    if (upcNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element upcElement = (Element) upcNode;

                        UPC upc = new UPC(size);

                        upc.setUpcNumber(upcElement.getElementsByTagName("upc-number").item(0).getTextContent());
                        upc.setSecondarySize(upcElement.getElementsByTagName("secondary-size").item(0).getTextContent());
                        upc.setSellingPrice(upcElement.getElementsByTagName("selling-price").item(0).getTextContent());
                        size.upcList.add(upc);
                        NodeList upcLabelList = upcElement.getElementsByTagName("upc-label");
                        for (int k = 0; k < upcLabelList.getLength(); k++) {

                            Node upcLabelNode = upcLabelList.item(k);
                            if (upcLabelNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element upcLabelElement = (Element) upcLabelNode;

                                UPCLabel upcLabel = new UPCLabel();

                                upcLabel.setLabelRef(upcLabelElement.getElementsByTagName("label-ref").item(0).getTextContent());
                                upcLabel.setLabelCategory(upcLabelElement.getElementsByTagName("label-category").item(0).getTextContent());
                                upcLabel.setLabelType(upcLabelElement.getElementsByTagName("label-type").item(0).getTextContent());

                                if (upcLabelElement.getElementsByTagName("label-order").item(0) != null) {
                                    upcLabel.setLabelOrder(Integer.parseInt(upcLabelElement.getElementsByTagName("label-order").item(0).getTextContent()));
                                } else {
                                    upcLabel.setLabelOrder(0);
                                }

                                if (upcLabelElement.getElementsByTagName("set-name").item(0) != null) {
                                    upcLabel.setSetName(upcLabelElement.getElementsByTagName("set-name").item(0).getTextContent());
                                } else {
                                    upcLabel.setSetName("");
                                }

                                if (upcLabelElement.getElementsByTagName("box-quantity").item(0) != null) {
                                    upcLabel.setBoxQty(upcLabelElement.getElementsByTagName("box-quantity").item(0).getTextContent());
                                    upc.setBoxQuantity(upcLabel.getBoxQty());
                                }
                                upc.getLabelList().add(upcLabel);
                            }
                        }
                    }
                }
                colour.sizeList.add(size);
                if ( colour.internationalSizeList.isEmpty() ) {
                    colour.internationalSizeList.add(size);
                }
            }
        }
    }
}
