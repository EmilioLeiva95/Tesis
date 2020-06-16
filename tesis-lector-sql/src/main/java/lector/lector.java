package lector;
import java.io.*;
import java.util.List;

import objetos.tabla;
import objetos.columna;

public class lector {
	public static void main(String [] arg) {
	      File archivo = null;
	      FileReader fr = null;
	      BufferedReader br = null;

	      try {
	         // Apertura del fichero y creacion de BufferedReader para poder
	         // hacer una lectura comoda (disponer del metodo readLine()).
	         archivo = new File ("./src/main/resources/script/script.sql");
	         fr = new FileReader (archivo);
	         br = new BufferedReader(fr);
	  	     List<tabla> tablas;
	  	     List<columna> columnas;
	         // Lectura del fichero
	         String linea;
	         String[] splitlinea = null;
	         while((linea=br.readLine())!=null) {
	        	splitlinea = linea.split(" ");
	         	for(int i = 0; i < splitlinea.length; i++) {
	         		if(splitlinea[i].equals("TABLE") && splitlinea[i-1].equals("CREATE")) {
	         			 System.out.println(splitlinea[i+1]);
	         			 i=splitlinea.length;
	         		}else {
	         			if(splitlinea[i].matches("\"(.*)") && !splitlinea[i-1].equals("TABLE") && !splitlinea[i-2].equals("ALTER") && !splitlinea[i-1].contentEquals("REFERENCES")) {
		         			 System.out.println("  "+splitlinea[i]);
		         			 i=splitlinea.length;
	         			
	         		}
	         	}
	            
	         }
	      }}
	      catch(Exception e){
	         e.printStackTrace();
	      }finally{
	         // En el finally cerramos el fichero, para asegurarnos
	         // que se cierra tanto si todo va bien como si salta 
	         // una excepcion.
	         try{                    
	            if( null != fr ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
	   }
}
