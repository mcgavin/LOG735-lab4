package XMLtool;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;

import org.xml.sax.InputSource;

import Client.DataObject;


public class xmlParser {
	
	public static String ObjectToXMLString(DataObject fileObject){
		DataObject dataObject = null;

		String out = "";
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(DataObject.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			 
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	 
			StringWriter sw = new StringWriter();
			
			jaxbMarshaller.marshal(fileObject, sw);
			
			out += sw.toString();
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return out;
	}
	
	
	public static DataObject xmlStringToObject(String xmlString){
		DataObject dataObject = null;

		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(DataObject.class);
			 				
			StringReader reader = new StringReader(xmlString);
			
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			dataObject = (DataObject) jaxbUnmarshaller.unmarshal(reader);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return dataObject;
	}
}
