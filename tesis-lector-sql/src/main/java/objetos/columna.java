package objetos;


public class columna {
	Integer idColumna;
	String descripcion;
	Integer longitud;
	Boolean llavePrimaria;
	Boolean llaveForanea;
	columna columnaReferencia;
	Boolean nuleable;
	tabla tabla;
	tipo tipo;
	
	public tabla getTabla() {
		return tabla;
	}
	public void setTabla(tabla tabla) {
		this.tabla = tabla;
	}
	public tipo getTipo() {
		return tipo;
	}
	public void setTipo(tipo tipo) {
		this.tipo = tipo;
	}
	public Integer getIdColumna() {
		return idColumna;
	}
	public void setIdColumna(Integer idColumna) {
		this.idColumna = idColumna;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getLongitud() {
		return longitud;
	}
	public void setLongitud(Integer longitud) {
		this.longitud = longitud;
	}
	public Boolean getLlavePrimaria() {
		return llavePrimaria;
	}
	public void setLlavePrimaria(Boolean llavePrimaria) {
		this.llavePrimaria = llavePrimaria;
	}
	public Boolean getNuleable() {
		return nuleable;
	}
	public void setNuleable(Boolean nuleable) {
		this.nuleable = nuleable;
	}
	public columna getColumnaReferencia() {
		return columnaReferencia;
	}
	public void setColumnaReferencia(columna columnaReferencia) {
		this.columnaReferencia = columnaReferencia;
	}
	public Boolean getLlaveForanea() {
		return llaveForanea;
	}
	public void setLlaveForanea(Boolean llaveForanea) {
		this.llaveForanea = llaveForanea;
	}
}
