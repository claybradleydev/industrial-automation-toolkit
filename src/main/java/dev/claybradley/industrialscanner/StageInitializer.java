package dev.claybradley.industrialscanner;

import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageInitializer implements ApplicationListener<JavaFxApplication.StageReadyEvent> {
    @Override
    public void onApplicationEvent(JavaFxApplication.StageReadyEvent event) {
        Stage stage = event.getStage();
    }
}
