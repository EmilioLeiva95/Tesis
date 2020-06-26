package objetos;


public class plantilla {
	Integer idplantilla;
	Integer tipo;
	String descripcion;
	tabla tabla;
	columna columna;
	String almacenar;
	String mostrar;
	
	public Integer getIdplantilla() {
		return idplantilla;
	}
	public void setIdplantilla(Integer idplantilla) {
		this.idplantilla = idplantilla;
	}
	public Integer getTipo() {
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public tabla getTabla() {
		return tabla;
	}
	public void setTabla(tabla tabla) {
		this.tabla = tabla;
	}
	public columna getColumna() {
		return columna;
	}
	public void setColumna(columna columna) {
		this.columna = columna;
	}
	public String getAlmacenar() {
		return almacenar;
	}
	public void setAlmacenar(String almacenar) {
		this.almacenar = almacenar;
	}
	public String getMostrar() {
		return mostrar;
	}
	public void setMostrar(String mostrar) {
		this.mostrar = mostrar;
	}
}
