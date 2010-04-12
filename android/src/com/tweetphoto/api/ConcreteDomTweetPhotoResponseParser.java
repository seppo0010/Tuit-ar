package com.tweetphoto.api;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// FIXME: replacing TweetPhoto library because of inconsistency with the response

public class ConcreteDomTweetPhotoResponseParser  {
	String m_Xml;

	public ConcreteTweetPhotoResponse parse() {
		
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ConcreteTweetPhotoResponse response = new ConcreteTweetPhotoResponse();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            ByteArrayInputStream is = new ByteArrayInputStream(m_Xml.getBytes());
            
            Document dom   			= builder.parse(is);
            Element root   			= dom.getDocumentElement();
            //NodeList items = root.getElementsByTagName("SessionKeyOperationResponse");
            
            //for (int i=0;i<items.getLength();i++)
            {
                Node item 			= root;
                NodeList properties = item.getChildNodes();
                for (int j=0;j<properties.getLength();j++){
                    Node property = properties.item(j);
                    String name = property.getNodeName();
                    if (name.equalsIgnoreCase("Large")){
                    	response.m_large = property.getFirstChild().getNodeValue(); 
                    } else if (name.equalsIgnoreCase("MediaId")){
                    	response.m_mediaId = property.getFirstChild().getNodeValue(); 
                    } else if (name.equalsIgnoreCase("MediaUrl")){
                    	response.m_mediaUrl = property.getFirstChild().getNodeValue(); 
                    } else if (name.equalsIgnoreCase("Medium")){
                    	response.m_medium = property.getFirstChild().getNodeValue(); 
                    } else if (name.equalsIgnoreCase("Original")){
                    	response.m_original = property.getFirstChild().getNodeValue(); 
                    } else if (name.equalsIgnoreCase("PhotoId")){
                    	response.m_photoId = Long.parseLong(property.getFirstChild().getNodeValue()); 
//                    } else if (name.equalsIgnoreCase("SessionKeyResponse")){
//                    	response.m_sessionKeyResponse = property.getFirstChild().getNodeValue(); 
                    } else if (name.equalsIgnoreCase("Thumbnail")){
                    	response.m_thumbnail = property.getFirstChild().getNodeValue(); 
                    } else if (name.equalsIgnoreCase("Status")){
                    	response.m_status = property.getFirstChild().getNodeValue(); 
                    } else if (name.equalsIgnoreCase("UserId")){
                    	response.m_userId = Long.parseLong(property.getFirstChild().getNodeValue()); 
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
        return response;
    }
}
