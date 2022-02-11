package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.BusinessCRUD;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.ModalForm;

public class DeleteBusiness {
    PayRollAPI api = new PayRollAPI();
    ModalForm form = new ModalForm(PayRollAPI.getLanguage("DeleteBusinessFormTitle"));

    public void initialize(Player player, Business business){

        form.setContent(PayRollAPI.getLanguage("DeleteBusinessFormConfirmationText" + business.BusinessName));
        form.setPositiveButton(PayRollAPI.getLanguage("DeleteBusinessFormYesButton"));
        form.setNegativeButton(PayRollAPI.getLanguage("DeleteBusinessFormNoButton"));

        form.setHandler((p, result) -> {
            if (result){
                api.DeleteBusiness(business, player);
            }
            else {
                player.sendMessage(PayRollAPI.getLanguage("DeleteBusinessFormAverted"));
            }});
        form.send(player);
    }
}
