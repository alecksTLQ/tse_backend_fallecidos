package com.example.springsocial.tools;

public class FileExtension {
	private String base64, extension;
	
	public String getBase64() {		return base64;	}
	public void setBase64(String base64) {		this.base64 = base64;	}

	public String getExtension() {		return extension;	}
	public void setExtension() {		
		String[] strings = this.base64.split(",");
		
		switch (strings[0]) {//check image's extension
		    case "data:image/jpeg;base64":
		        this.extension = "jpeg";
		        break;
		    case "data:image/png;base64":
		        this.extension = "png";
		        break;
		    case "data:video/mp4;base64":
		        this.extension = "mp4";
		        break;
		    case "data:application/pdf;base64":
		        this.extension = "pdf";
		        break;
		    default://should write cases for more images types
		        this.extension = "jpg";
		        break;
		}
	}
}
