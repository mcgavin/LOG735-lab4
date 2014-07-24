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
	
//	public static void main(String[] args) {
//		 
//		DataObject dataobj = new DataObject(212,"asdasdas","125.0.01.2",51654,"aasdas","jonh/root/","mathieu") ;
//
// 
//		try {
//			
//			File file = new File("J:\\file.xml");
//			JAXBContext jaxbContext = JAXBContext.newInstance(DataObject.class);
//			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//			 
//			// output pretty printed
//			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//	 
//			jaxbMarshaller.marshal(dataobj, file);
//			jaxbMarshaller.marshal(dataobj, System.out);
// 
//			
//			
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//				 
//			File file = new File("J:\\file.xml");
//			JAXBContext jaxbContext = JAXBContext.newInstance(DataObject.class);
//			 
//			
//			StringReader reader = new StringReader("xml string here");
//			
//			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//			DataObject customer = (DataObject) jaxbUnmarshaller.unmarshal(reader);
//			System.out.println(customer);
//			
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
// 
//	}
//	
	
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
