import java.util.ArrayList;

    public class CadastroPartido {
	   private ArrayList<Partido> partidos;

	public CadastroPartido(){
		this.partidos = new ArrayList<>();
	}

	public boolean cadastraPartido(Partido p) {
		for(int i=0; i<partidos.size(); i++){
			Partido partido = partidos.get(i);
			if(partido.getNumero() == p.getNumero() || partido.getNome().equals(p.getNome())){
				return false;
			}
		}
		partidos.add(p);
		return true;
       }

	public Partido consultaPartido(String nome) {
		for(int i=0; i<partidos.size(); i++){
			Partido partido = partidos.get(i);
			if(partido.getNome().equals(nome)){
				return partido;
			}
		}
		return null;
		}

	public Partido consultaPartido(int numero) {
		for(int i=0; i<partidos.size(); i++){
			Partido partido = partidos.get(i);
			if(partido.getNumero()==numero){
				return partido;
			}
	    }
		return null;
    }

	public ArrayList<Partido> getPartidos() {
		return partidos;
	}

}
