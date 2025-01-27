package hva.app.employee;

import hva.core.Hotel;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Add a new responsability to an employee: species to veterinarians and 
 * habitats to zookeepers.
 **/
class DoAddResponsibility extends Command<Hotel> {

  DoAddResponsibility(Hotel receiver) {
    super(Label.ADD_RESPONSABILITY, receiver);
    addStringField("employeeId", Prompt.employeeKey());
    addStringField("responsibilityId", Prompt.responsibilityKey());
  }
  
  @Override
  protected void execute() throws CommandException {
    String employeeKey = stringField("employeeId");
    String responsibilityKey = stringField("responsibilityId");
  
    _receiver.addResponsibility(employeeKey, responsibilityKey);
    //_display.popup("Responsabilidade adicionada");
  }
}
