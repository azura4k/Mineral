package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.element.Input;

public class EmployeeSelectionByName {

    PayRollAPI api = new PayRollAPI();
    CustomForm form = new CustomForm(PayRollAPI.getLanguage("EmployeeSearchFormTitle"));

    public void initalize(Player player, Business business) {


        form.addElement("Search",Input.builder().setName(PayRollAPI.getLanguage("EmployeeSearchFormInstructions")).build());
        form.setHandler((p, response) -> {
            if (response.getInput("Search").getValue() == null){
                p.sendMessage(PayRollAPI.getLanguage("NoValueDetected"));
            }
            else {
                String Query = response.getInput("Search").getValue();
                Employee employee =  api.LoadEmployee(business, Query);
                if (!(employee == null)) {
                    EmployeeSelectionOptions form = new EmployeeSelectionOptions();
                    form.initialize(p, business, employee);
                }
                else{
                    p.sendMessage(PayRollAPI.getLanguage("NoEmployeeFound"));
                    EmployeeSelection form2 = new EmployeeSelection();
                    form2.Initialize(player, business);
                }

            }

        });
        form.send(player);

    }

}
