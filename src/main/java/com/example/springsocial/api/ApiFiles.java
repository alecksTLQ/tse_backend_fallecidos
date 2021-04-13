package com.example.springsocial.api;

public class ApiFiles extends Api {
		
	public ApiFiles() {
		setApiUrl(System.getenv("FILES_API_URL"));

		//setApiUrl("http://192.168.79.66:3005");
	}
	
	public void setGetPath(String type, String folder, String name) {		setPath("/file/get/"+ type + "/"+ folder +"/"+ name);	}

	public void setPostPath() {		setPath("/file/create");	}
	
	public String getExtensionFromBase64String(String base64String) {
		String[] strings = base64String.split(",");
		String extension;
		switch (strings[0]) {//check image's extension
		    case "data:image/jpeg;base64":
		        extension = "jpeg";
		        break;
		    case "data:image/png;base64":
		        extension = "png";
		        break;
		    case "data:video/mp4;base64":
		        extension = "mp4";
		        break;
		    case "data:application/pdf;base64":
		        extension = "pdf";
		        break;
		    default://should write cases for more images types
		        extension = "jpg";
		        break;
		}
		return extension;
	}
}
