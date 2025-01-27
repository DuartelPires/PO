package hva.app.employee;

import hva.core.Hotel;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Adds a new employee to this zoo hotel.
 **/
class DoRegisterEmployee extends Command<Hotel> {

  DoRegisterEmployee(Hotel receiver) {
    super(Label.REGISTER_EMPLOYEE, receiver);
    
    addStringField("employeeId", Prompt.employeeKey());
    addStringField("name", Prompt.employeeName());
    addStringField("employeeType", Prompt.employeeType());

  }
  
  @Override
  protected void execute() throws CommandException {
    String employeeKey = stringField("employeeId");
    String employeeName = stringField("name");
    String employeeType = stringField("employeeType");

    while (!employeeType.equals("VET") && !employeeType.equals("TRT")) {
      _display.popup("Tipo de employee inválido. Deve ser 'VET' ou 'TRT'.");
      employeeType = Form.requestString(Prompt.employeeType());
    }

    _receiver.registerEmployee(employeeKey, employeeName, employeeType);
    //_display.popup("Funcionário registado");
    
  }
}
