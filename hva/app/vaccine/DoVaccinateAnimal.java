package hva.app.vaccine;

import hva.core.Hotel;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Vaccinate by a given veterinarian a given animal with a given vaccine.
 **/
class DoVaccinateAnimal extends Command<Hotel> {
  DoVaccinateAnimal(Hotel receiver) {
    super(Label.VACCINATE_ANIMAL, receiver);
    addStringField("vaccineId", Prompt.vaccineKey());
    addStringField("vetId", Prompt.veterinarianKey());
    addStringField("animalId", hva.app.animal.Prompt.animalKey());
  }

  @Override
  protected final void execute() throws CommandException {
    String vaccineKey = stringField("vaccineId");
    String vetKey = stringField("vetId");
    String animalKey = stringField("animalId");

    if(!_receiver.checkVaccineHasSpecie(vaccineKey, animalKey)){
      _display.popup(Message.wrongVaccine(vaccineKey, animalKey));
    }
    _receiver.vacinarAnimal(vaccineKey, vetKey, animalKey);
  }
}
