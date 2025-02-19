import java.util.ArrayList;
public class Candidatura {
	private ArrayList<Candidato> candidatos;

	public Candidatura(){
		this.candidatos = new ArrayList<>();
	}

	public boolean cadastraCandidato(Candidato c) {
		for(int i=0; i<candidatos.size(); i++){
			Candidato candidato = candidatos.get(i);
			if(candidato.getNumero() == c.getNumero() && candidato.getMunicipio().equals(c.getMunicipio())){
				return false;
			}
		}
		candidatos.add(c);
		return true;
	}



	public Candidato consultaCandidato(int numero, String municipio) {
        for (int i = 0; i < candidatos.size(); i++) {
            Candidato candidato = candidatos.get(i);
            if (candidato.getNumero() == numero && candidato.getMunicipio().equals(municipio)) {
                return candidato;
            }
        }
        return null; 
    }

	public ArrayList<Candidato> getCandidatos() {
        return candidatos;
    }

}
