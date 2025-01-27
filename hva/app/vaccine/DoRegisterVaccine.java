package hva.app.vaccine;

import hva.core.Hotel;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Apply a vaccine to a given animal.
 **/
class DoRegisterVaccine extends Command<Hotel> {

  DoRegisterVaccine(Hotel receiver) {
    super(Label.REGISTER_VACCINE, receiver);
    addStringField("vaccineId", Prompt.vaccineKey());
    addStringField("name", Prompt.vaccineName());
    addStringField("speciesId", Prompt.listOfSpeciesKeys());    
  }

  @Override
  protected final void execute() throws CommandException {
    String vaccineKey = stringField("vaccineId");
    String vaccineName = stringField("name");
    String speciesInput = stringField("speciesId");

    String[] listOfSpeciesKeys = speciesInput.split("\\s*,\\s*");

    _receiver.registerVaccine(vaccineKey, vaccineName, listOfSpeciesKeys);
  }
}
