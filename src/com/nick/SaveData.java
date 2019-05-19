package com.nick;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public final class SaveData {

    private SaveData() { }

    public static void writeFile(File file, String[] data) {

        try {

            /**
             * Setting up all the classes needed to save
             * the encrypted data in XML format
             * (It was easier to save the file as XML
             * after it was encrypted)
             */
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            //Setting up the root element of the document
            Element root = doc.createElement("encrypteddata");
            //The two attributes of the encrypted data needed to save is the
            //size of the message and the size of the data blocks
            Attr ml = doc.createAttribute("messagelength");
            Attr bs = doc.createAttribute("blocksize");
            ml.setValue(data[0]);
            bs.setValue(data[1]);
            root.setAttributeNode(bs);
            root.setAttributeNode(ml);
            doc.appendChild(root);
            //creating an XML element for each of the
            //data blocks of the encrypted message
            for (int i = 2; i < data.length; i++) {
                Element dataBlock = doc.createElement("datablock");
                dataBlock.appendChild(doc.createTextNode(data[i]));
                root.appendChild(dataBlock);
            }

            //Saving the encrypted message to the XML formatted .cry file
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            t.transform(source, result);

        } catch (Exception ex) { ex.printStackTrace(); }

    }

}
