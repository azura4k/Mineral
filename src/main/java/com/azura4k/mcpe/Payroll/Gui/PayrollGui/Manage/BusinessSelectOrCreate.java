package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementToggle;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.handler.SimpleFormHandler;

import java.util.ArrayList;


public class BusinessSelectOrCreate {

    SimpleForm form = new SimpleForm("Testing Forms");
    Player p;
    public void initialize(Player player){
        form.setTitle("Select A Business");

        ArrayList<String> Employers = new ArrayList<String>();
        for (int i Employers.iterator()){

        }







        form.addButton("Create Business", CreateBusinessHandler);







        form.setNoneHandler(p -> {
            p.sendMessage("Goodbye");
        });
        form.send(player);
    }
    SimpleFormHandler CreateBusinessHandler = (p, button) -> {
        CreateBusiness CreateBusiness = new CreateBusiness();
        CreateBusiness.initialize(p.getPlayer());

    };


}
