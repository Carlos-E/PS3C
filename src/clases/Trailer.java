package clases;

public class Trailer {
	
	String patente, tipo, peso, espacio, estado, destino, origen, camion, empresa;

	public Trailer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Trailer(String patente, String tipo, String peso, String espacio, String estado, String destino,
			String origen, String camion, String empresa) {
		super();
		this.patente = patente;
		this.tipo = tipo;
		this.peso = peso;
		this.espacio = espacio;
		this.estado = estado;
		this.destino = destino;
		this.origen = origen;
		this.camion = camion;
		this.empresa = empresa;
	}

	public String getPatente() {
		return patente;
	}

	public void setPatente(String patente) {
		this.patente = patente;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getPeso() {
		return peso;
	}

	public void setPeso(String peso) {
		this.peso = peso;
	}

	public String getEspacio() {
		return espacio;
	}

	public void setEspacio(String espacio) {
		this.espacio = espacio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getCamion() {
		return camion;
	}

	public void setCamion(String camion) {
		this.camion = camion;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	@Override
	public String toString() {
		return "trailer [patente=" + patente + ", tipo=" + tipo + ", peso=" + peso + ", espacio=" + espacio
				+ ", estado=" + estado + ", destino=" + destino + ", origen=" + origen + ", camion=" + camion
				+ ", empresa=" + empresa + "]";
	}
	


	

}
