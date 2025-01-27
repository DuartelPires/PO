package hva.app.habitat;

import hva.core.Hotel;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Associate (positive or negatively) a species to a given habitat.
 **/
class DoChangeHabitatInfluence extends Command<Hotel> {

  DoChangeHabitatInfluence(Hotel receiver) {
    super(Label.CHANGE_HABITAT_INFLUENCE, receiver);

    addStringField("habitatId", Prompt.habitatKey());
    addStringField("especieID", hva.app.animal.Prompt.speciesKey());
    addStringField("influencia", Prompt.habitatInfluence());
  }
  
  @Override
  protected void execute() throws CommandException {
    //  idhab id especie, Prompt.habitatInfluence

    Form influence = new Form("Change Influence");
    influence.parse();
    
    String habitatKey = stringField("habitatId");
    String especieKey = stringField("especieID");
    String novaInfluencia = stringField("influencia");

    while (!novaInfluencia.equals("POS") && !novaInfluencia.equals("NEG") && !novaInfluencia.equals("NEU")) {
      _display.popup("Tipo de influencia inválida. Deve ser 'POS' 'NEG' ou 'NEU'.");
      novaInfluencia = Form.requestString(Prompt.habitatInfluence());
    }

    _receiver.alterarInfluenciaHabitat(habitatKey, especieKey, novaInfluencia);
    //_display.popup("Influencia alterada");


  }
}
