package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Timesheet;
import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.BusinessCRUD.CreateBusiness;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Options;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.handler.SimpleFormHandler;

import java.util.ArrayList;


public class SelectJob {

    SimpleForm form = new SimpleForm("Testing Forms");
    PayRollAPI api = new PayRollAPI();

    public void initialize(Player player){
        form.setTitle(PayRollAPI.getLanguage("ClockInJobSelectFormTitle"));

        ArrayList<String> Employers = api.getBusinessesEmployedAt(player);

        for (int i = 0; i < Employers.size(); i++){
            final String businessName = api.LoadBusiness(Employers.get(i)).BusinessName;
            form.addButton(businessName, Handler);
        }
        form.send(player);
    }

    SimpleFormHandler Handler = (p, button) ->{
        ClockInClockOut form = new ClockInClockOut();
        form.initialize(p, api.LoadBusiness(button.getName()), api.LoadEmployee(api.LoadBusiness(button.getName()), p.getName()));

    };


}