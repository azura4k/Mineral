package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Options;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import com.sun.org.apache.xpath.internal.operations.Mod;
import ru.contentforge.formconstructor.form.ModalForm;

public class QuitJob {

    PayRollAPI api = new PayRollAPI();


    ModalForm QuitJob = new ModalForm();
    public void Initialize(Player player, Business business, Employee employee){

        QuitJob.setContent(PayRollAPI.getLanguage("QuitPositionFormContent") + employee.EmployerName);
        QuitJob.setPositiveButton(PayRollAPI.getLanguage("QuitPositionFormYes"));
        QuitJob.setNegativeButton("QuitPositionFormNo");

        QuitJob.setHandler((p, result) -> {
            if (result){
                api.FireEmployee(employee);
            }
            else if (!result){
                Options options = new Options();
                options.initialize(player,business);
            }
        });

        QuitJob.send(player);
    }
}
