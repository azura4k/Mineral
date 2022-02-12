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

        QuitJob.setContent("Are you sure you wish to quit your position at: " + employee.EmployerName);
        QuitJob.setPositiveButton("Yes");
        QuitJob.setNegativeButton("No");

        QuitJob.setHandler((p, result) -> {
            if (result){
                api.FireEmployee(employee);
            }
            else if (!result){
                p.sendMessage("Not Quiting");
                Options options = new Options();
                options.initialize(player,business);
            }
        });

        QuitJob.send(player);
    }
}
