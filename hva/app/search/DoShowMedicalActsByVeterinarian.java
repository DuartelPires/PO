package hva.app.search;

import hva.core.Hotel;
import java.util.*;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

//FIXME add more imports if needed

/**
 * Show all medical acts of a given veterinarian.
 **/
class DoShowMedicalActsByVeterinarian extends Command<Hotel> {

  DoShowMedicalActsByVeterinarian(Hotel receiver) {
    super(Label.MEDICAL_ACTS_BY_VET, receiver);
    addStringField("vetId", hva.app.employee.Prompt.employeeKey());
  }

  @Override
  protected void execute() throws CommandException {
    String vetKey = stringField("vetId");

    List<String>list = _receiver.getAtosMedicosFuncionario(vetKey);
    list.sort(Comparator.comparing(s -> s.split("\\|")[1].trim()));
        
    for(String f: list){
      _display.addLine(f);
    }
    _display.display();
  }
}
