package hva.app.employee;

import hva.core.Hotel;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show the satisfaction of a given employee.
 **/
class DoShowSatisfactionOfEmployee extends Command<Hotel> {

  DoShowSatisfactionOfEmployee(Hotel receiver) {
    super(Label.SHOW_SATISFACTION_OF_EMPLOYEE, receiver);
    addStringField("employeeId", Prompt.employeeKey());
  }
  
  @Override
  protected void execute() throws CommandException {
    String employeeKey = stringField("employeeId");

    _display.popup(_receiver.calcularSatisfacaoFuncionarios(employeeKey));
  }
}
