package my.ais.controller.welcome;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Window;

public class WelcomeController extends SelectorComposer<Component> {
    private static final long serialVersionUID = 1L;
     
    @Listen("onClick = #devBtn")
    public void showModal1(Event e) {
        Window window = (Window)Executions.createComponents(
                "/stad_01_welcome_page/stad_01_software_info.zul", null, null);
        window.doModal();
    }
    @Listen("onClick = #authorBtn")
    public void showModal2(Event e) {
        Window window = (Window)Executions.createComponents(
                "/stad_01_welcome_page/stad_01_author_info.zul", null, null);
        window.doModal();
    }
}
