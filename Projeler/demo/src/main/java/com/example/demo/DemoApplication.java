package com.example.demo;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

@RestController
public class DemoApplication {

	Connection connection = null;
	Statement stat = null;
	PreparedStatement preparedStatement = null;

	// TODO EKLENEN OBJEYI DON
	@CrossOrigin
	@PostMapping ("/POST")
	public String set(@RequestParam(value = "Name", defaultValue = "-", required = true) String name,
					   @RequestParam(value = "Bool", defaultValue = "false", required = true) boolean bool){
		boolean flag = true;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:test.db");
			stat = connection.createStatement();
			stat.execute("CREATE TABLE IF NOT EXISTS veriler(Name TEXT, ID INTEGER PRIMARY KEY AUTOINCREMENT, Bool INTEGER)");
			preparedStatement = connection.prepareStatement("INSERT INTO veriler (Name,Bool) VALUES (?,?)");
			preparedStatement.setString(1,name);
			if(bool == false){
				preparedStatement.setInt(2,0);
			}
			else{
				preparedStatement.setInt(2,1);
			}
			preparedStatement.executeUpdate();
			stat.close();
			preparedStatement.close();
			connection.close();
		} catch (Exception error) {
			flag = false;
			error.printStackTrace();
		}
		if(flag == false){
			return "Data could not be registered!";
		}
		return "Data is written successfully!";
	}

	//TODO HEROKU APP ARAŞTIR
	//TODO OBJE ICINDE BOOLEAN DON TAMAMLANDI
	@CrossOrigin
	@PostMapping("/POSTIMAGE")
	public BooleanClass postImage(@RequestParam(value = "NewImage")MultipartFile imageFile) {
		boolean flag = true;
		try {
			String location = "src/images/";
			byte[] bytes = imageFile.getBytes();
			Path path = Paths.get(location + imageFile.getOriginalFilename());
			Files.write(path, bytes);
		} catch (IOException e) {
			flag = false;
			e.printStackTrace();
		}
		BooleanClass booleanClass = new BooleanClass(flag);
		return booleanClass;
	}

	@CrossOrigin
	@DeleteMapping("/DELETEIMAGE")
	public String deleteImage(@RequestParam(value = "ImageName") String imageName){
		File file = new File(String.format("src/images/%s",imageName));
		if(file.exists() == false){
			return "No image has been found with the given name!!!";
		}
		else if(file.delete()){
			return "Image is deleted successfully";
		}
		return "An error has been occurred while deleting the image!";
	}

	//TODO OBJE ICINDE BOOLEAN DON TAMAMLANDI
	@CrossOrigin
	@DeleteMapping ("/DELETE")
	public BooleanClass delete(@RequestParam(value = "ID", required = true) Integer id){
		boolean flag = true;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:test.db");
			preparedStatement = 	connection.prepareStatement("DELETE FROM veriler WHERE ID = ?");
			preparedStatement.setInt(1,id.intValue());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.close();
		}catch (Exception error){
			flag = false;
			error.printStackTrace();
		}
		BooleanClass booleanClass = new BooleanClass(flag);
		return booleanClass;
	}

	// TODO throws yerine try catch bloğu kur
	@CrossOrigin
	@GetMapping(value = "/GETIMAGE", produces = {MediaType.IMAGE_PNG_VALUE,
			MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_GIF_VALUE})
	public @ResponseBody byte[] getImage(@RequestParam(value = "ImageName") String imageName) throws IOException {
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(String.format("src/images/%s", imageName)));
		}
		catch (FileNotFoundException error){
			in = new BufferedInputStream(new FileInputStream(String.format("src/images/Error.jpg")));
		}
		return IOUtils.toByteArray(in);
	}

	@CrossOrigin
	@GetMapping(value = "/GETSVG", produces = {MediaType.APPLICATION_XHTML_XML_VALUE})
	public @ResponseBody byte[] getSVG(@RequestParam(value = "ImageName") String imageName) throws IOException {
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(String.format("src/images/%s", imageName)));
		}
		catch (FileNotFoundException error){
			in = new BufferedInputStream(new FileInputStream(String.format("src/images/Warning.svg")));
		}
		return IOUtils.toByteArray(in);
	}


	@CrossOrigin
	@GetMapping("/GETALL")
	public ArrayList get(){
		MyClass toReturn;
		ArrayList objectArray = new ArrayList();
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:test.db");
			stat = connection.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM veriler");
			while (rs.next()){
				toReturn = new MyClass(rs.getInt(2),rs.getString(1),rs.getInt(3) == 0);
				objectArray.add(toReturn);
			}
			stat.close();
			connection.close();
		} catch (Exception error) {
			error.printStackTrace();
		}
		return objectArray;
	}

	// TODO TEK OBJE DÖN TAMAMLANDI
	@CrossOrigin
	@GetMapping(value = "/GETBYID")
	public MyClass getSpecial(@RequestParam(value = "ID", required = true) Integer id){
		MyClass myClass = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:test.db");
			preparedStatement = connection.prepareStatement("SELECT * FROM veriler WHERE ID = ?");
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()){
				myClass = new MyClass(resultSet.getInt(2),resultSet.getString(1),resultSet.getInt(3) == 0);
			}
			preparedStatement.close();
			connection.close();
		} catch (Exception error) {
			error.printStackTrace();
		}
		if(myClass == null){
			myClass = new MyClass();
		}
		return myClass;
	}

	@CrossOrigin
	@GetMapping("/GETBYORDER")
	public ArrayList getByOrder(@RequestParam(value = "FirstIndex", required = true) int firstIndex,
								@RequestParam(value = "SecondIndex", required = true) int secondIndex){
		ArrayList dataToReturn = new ArrayList();
		try {
			int counter = 1;
			connection = DriverManager.getConnection("jdbc:sqlite:test.db");
			stat = connection.createStatement();
			stat.execute("SELECT * FROM veriler");
			ResultSet resultSet = stat.getResultSet();
			while (resultSet.next()){
				if(counter >= firstIndex && counter <= secondIndex) {
					MyClass temp = new MyClass(resultSet.getInt(2), resultSet.getString(1), resultSet.getInt(3) == 0);
					dataToReturn.add(temp);
				}
				counter++;
			}
			stat.close();
			connection.close();
		} catch (Exception error) {
			error.printStackTrace();
		}
		return dataToReturn;
	}

	@CrossOrigin
	@PutMapping("/RENAMEIMAGE")
	public String renameImage(@RequestParam(value = "oldImageName", required = true) String oldImageName,
							  @RequestParam(value = "newImageName", required = true) String newImageName){
		File oldFile = new File(String.format("src/images/%s",oldImageName));
		File newFile = new File(String.format("src/images/%s",newImageName));
		if(oldFile.renameTo(newFile)){
			return String.format("%s renamed to %s.",oldImageName,newImageName);
		}
		return String.format("Name of the file(%s) remained the same!!!",oldImageName);
	}

	// TODO SLACKTEN GUNCELL
	@CrossOrigin
	@PutMapping("/PUT")
	public String put(@RequestParam(value = "ID", required = true) int ID,
					@RequestParam(value = "NewName", required = false) String newName,
					@RequestParam(value = "NewBool",required = false) Boolean newBool)
	{
		boolean flag = true;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:test.db");
			if(newName != null){
				preparedStatement = connection.prepareStatement("UPDATE veriler SET Name = ? WHERE ID = ?");
				preparedStatement.setString(1,newName);
				preparedStatement.setInt(2,ID);
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}
			if(newBool != null){
				preparedStatement = connection.prepareStatement("UPDATE veriler SET Bool = ? WHERE ID = ?");
				if(newBool == true){
					preparedStatement.setBoolean(1,true);
				}
				else{
					preparedStatement.setBoolean(1,false);
				}
				preparedStatement.setInt(2,ID);
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}
		}catch (Exception error){
			flag = false;
			error.printStackTrace();
		}
		if(flag == true){
			return ID + " has been updated successfully!";
		}
		return ID + " could not be updated, an error is occurred!";
	}
}

class IDClass{

	protected int id;

	public IDClass(){
	}

	public IDClass(int id){
		this.id = id;
	}

	public int getInteger() {
		return id;
	}

}

class MyClass extends IDClass{
	private String nameSurname;
	private boolean bool;

	public MyClass(){
	}

	public MyClass(int id, String nameSurname, boolean bool){
		this.id = id;
		this.nameSurname = nameSurname;
		this.bool = bool;
	}

	public String getString() {
		return nameSurname;
	}

	public boolean isBool() {
		return bool;
	}
}

class BooleanClass{
	public boolean success;

	public BooleanClass(boolean flag) {
		this.success = flag;
	}
}