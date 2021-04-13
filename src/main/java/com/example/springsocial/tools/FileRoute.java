package com.example.springsocial.tools;

public class FileRoute {
	private String fileType, route;
	
	public String getFileType() {		return fileType;	}
	public void setFileType(String base64) {		this.fileType = base64;	}

	public String getRoute() {		return route;	}
	public void setRoute() {		
		String string = this.fileType;
		
		switch (string) {//check image's extension
		    case "data:image/jpeg;base64":
		        this.route = "jpg";
		        break;
		    case "data:image/png;base64":
		        this.route = "png";
		        break;
		    case "data:video/mp4;base64":
		        this.route = "mp4";
		        break;
		    case "data:application/pdf;base64":
		        this.route = "pdf";
		        break;
		    default://should write cases for more images types
		        this.route = "jpg";
		        break;
		}
	}
}
