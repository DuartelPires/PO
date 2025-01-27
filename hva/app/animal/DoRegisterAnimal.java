package hva.app.animal;


import hva.core.Hotel;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Register a new animal in this zoo hotel.
 */
class DoRegisterAnimal 

extends Command<Hotel> {

  DoRegisterAnimal(Hotel receiver) {
    super(Label.REGISTER_ANIMAL, receiver);
  }
  
  @Override
  protected final void execute() throws CommandException {

    Form animal = new Form("Animal");

    animal.addStringField("animalId", Prompt.animalKey());
    animal.addStringField("name", Prompt.animalName());
    animal.addStringField("speciesId", Prompt.speciesKey());
    animal.addStringField("habitatId", hva.app.habitat.Prompt.habitatKey());

    animal.parse();

    String Keyanimal = animal.stringField("animalId");
    String Nameanimal = animal.stringField("name");
    String Keyhabitat = animal.stringField("habitatId");
    String Keyspecies = animal.stringField("speciesId");

      
  
    if(_receiver.checkExistsSpecies(Keyspecies)){
      _receiver.addAnimalSpecie(Keyanimal, _receiver.validarSpecies(Keyspecies));
    }else{
      Form _especie = new Form("Specie");
      _especie.addStringField("specieName", Prompt.speciesName());
      _especie.parse();
      String specieName = _especie.stringField("specieName");
      _receiver.registerSpecies(Keyspecies, specieName);
    }
    _receiver.registerAnimal(Keyanimal, Nameanimal, Keyhabitat, Keyspecies);
    //_display.popup("Animal registado");
  }
}
