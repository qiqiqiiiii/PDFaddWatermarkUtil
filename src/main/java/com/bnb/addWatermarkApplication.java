package com.bnb;

import com.bnb.view.PrimaryStageView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class addWatermarkApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launchApp(addWatermarkApplication.class, PrimaryStageView.class, args);
        SpringApplication.run(addWatermarkApplication.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
    }
}
