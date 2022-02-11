package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.CRUD.Employees;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.ModalForm;

public class QuitJob {
    PayRollAPI api = new PayRollAPI();
    ModalForm form = new ModalForm(PayRollAPI.getLanguage("QuitJobFormTitle"));

    public void initialize(Player player, Business business, Employee employee){


        form.setContent(PayRollAPI.getLanguage("QuitJobFormContent" + business.BusinessName));
        form.setPositiveButton(PayRollAPI.getLanguage("QuitJobFormYesButton"));
        form.setNegativeButton(PayRollAPI.getLanguage("QuitJobFormNoButton"));

        form.setHandler((p, result) -> {
            if (result){
                api.FireEmployee(employee);
            }
            else {
                player.sendMessage("Canceled Action");
            }});
        form.send(player);
    }
}
