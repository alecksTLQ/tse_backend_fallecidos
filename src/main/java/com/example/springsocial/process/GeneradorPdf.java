package com.example.springsocial.process;

import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springsocial.crud.ObjectSetGet;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class GeneradorPdf {
	
	private ObjectSetGet objeto = new ObjectSetGet();
	public String Rutas(String documento) throws Exception {    
		 String newPath=null;
		try{
			String current = new java.io.File( "." ).getAbsolutePath();
	        System.out.println("Current dir:"+current);
	        
	        ClassLoader classLoader = getClass().getClassLoader();
	        File file = new File(classLoader.getResource(documento).getFile());
	        newPath=file.getCanonicalPath();
	        System.out.println(newPath);
	        newPath= "target/classes/"+documento;
	        if(file.exists()) {System.out.println("Se creo el archivo");}
	        else {System.out.println("No se creo el archivo");}
	        System.out.println(newPath);
		}catch(Exception ex) {
			ex.printStackTrace();
		}		
    return newPath;
	}
	
	public String GeneradorGenerico(Object model,String archivo, String fechaInicio, String fechaFinal) throws Exception {
		String pathRuta=null,path=null;		
		try {
		pathRuta=Rutas(archivo+".jrxml");		
		objeto.setObject(model);
		JSONArray objetoJSON = objeto.convertAtJSONType(JSONArray.class);					
		System.out.println(objetoJSON.toJSONString());
		
		JasperReport jasperReport = JasperCompileManager.compileReport(pathRuta);
		System.out.println("pathRuta:--> Empezando a crear la ruta del archivo "+pathRuta);
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(objetoJSON);
		Map<String,Object> parameters = new HashMap<>();
		parameters.put("fechaDe", fechaInicio);
		parameters.put("fechaA", fechaFinal);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,dataSource);
		path= Base64.getEncoder().encodeToString(JasperExportManager.exportReportToPdf(jasperPrint));		
		}catch(Exception e) {
			throw new Exception(e);
		}
		return path;
	}

}
